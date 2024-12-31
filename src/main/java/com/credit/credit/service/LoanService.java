package com.credit.credit.service;

import com.credit.credit.constants.LoanConstants;
import com.credit.credit.enums.ChainingMethod;
import com.credit.credit.enums.SpecificationType;
import com.credit.credit.exception.common.EntityNotFoundException;
import com.credit.credit.exception.loan.InsufficientCreditLimitException;
import com.credit.credit.exception.common.InternalServerException;
import com.credit.credit.exception.loan.InvalidInstallmentNumberException;
import com.credit.credit.exception.loan.InvalidInterestRateException;
import com.credit.credit.model.dto.LoanInstallmentDto;
import com.credit.credit.model.entity.Customer;
import com.credit.credit.model.entity.Loan;
import com.credit.credit.model.entity.LoanInstallment;
import com.credit.credit.model.dto.LoanDto;
import com.credit.credit.model.request.CreateLoanRequest;
import com.credit.credit.model.request.PayLoanRequest;
import com.credit.credit.model.response.CreateLoanResponse;
import com.credit.credit.model.mapper.Mapper;
import com.credit.credit.model.response.ListLoansResponse;
import com.credit.credit.model.response.PayLoanResponse;
import com.credit.credit.repository.ICustomerRepository;
import com.credit.credit.repository.ILoanRepository;
import com.credit.credit.validator.LoanValidator;
import com.credit.credit.validator.SpecificationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LoanService {
	private final ICustomerRepository customerRepository;

	private final ILoanRepository loanRepository;

	private final LoanInstallmentService loanInstallmentService;

	private final SpecificationFactory specificationFactory;

	private final LoanValidator loanValidator;

	private final Mapper mapper;

	public ListLoansResponse getLoans(UUID customerId) {
		List<Loan> loans = loanRepository.findByCustomerId(customerId);
		List<LoanDto> loanDtos = loans.stream()
				.map(mapper::toLoanDto)
				.collect(Collectors.toList());

		return ListLoansResponse.builder()
				.customerId(customerId)
				.loans(loanDtos)
				.build();
	}

	@Transactional
	public CreateLoanResponse createLoan(CreateLoanRequest createLoanRequest, UUID customerId) {
		BigDecimal totalLoanAmount;
		try {
			Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
					new EntityNotFoundException("Customer not found"));
			BigDecimal remainingLimit = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());
			validateCreateLoan(createLoanRequest, remainingLimit);

			totalLoanAmount = calculateTotalLoanAmount(createLoanRequest);
			Loan loan = createLoanEntity(customer, createLoanRequest.getNumberOfInstallments(), totalLoanAmount);
			loanRepository.save(loan);

			List<LoanInstallmentDto> installments = createInstallments(loan, totalLoanAmount);

			customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(createLoanRequest.getLoanAmount()));
			customerRepository.save(customer);

			return CreateLoanResponse.builder()
					.loanId(loan.getId())
					.loanAmount(createLoanRequest.getLoanAmount())
					.totalLoanAmount(totalLoanAmount)
					.numberOfInstallments(createLoanRequest.getNumberOfInstallments())
					.installments(installments)
					.build();

		} catch (EntityNotFoundException |
				 InsufficientCreditLimitException |
				 InvalidInterestRateException |
				 InvalidInstallmentNumberException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalServerException("An error occurred.");
		}
	}

	@Transactional
	public PayLoanResponse payLoan(UUID loanId, PayLoanRequest payLoanRequest) {
		try {
			BigDecimal paymentAmount = payLoanRequest.getAmount();
			BigDecimal remainingAmount = paymentAmount;

			Loan loan = loanRepository.findById(loanId).orElseThrow(() ->
					new EntityNotFoundException("Loan not found"));

			List<LoanInstallment> installments = loanInstallmentService.getUnpaidInstallments(loanId, false);

			int installmentsPaid = 0;
			BigDecimal totalPaidAmount = BigDecimal.ZERO;

			for (LoanInstallment installment : installments) {
				BigDecimal adjustedAmount = adjustPayment(installment);

				if (remainingAmount.compareTo(adjustedAmount) >= 0) {
					installment.setPaidAmount(adjustedAmount);
					installment.setPaymentDate(LocalDateTime.now());
					installment.setPaid(true);
					remainingAmount = remainingAmount.subtract(adjustedAmount);
					totalPaidAmount = totalPaidAmount.add(adjustedAmount);
					installmentsPaid++;

					loanInstallmentService.save(installment);

					if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
						break;
					}
				} else {
					break;
				}
			}

			BigDecimal remainingDept = loanInstallmentService.getRemainingDept(loanId);

			processRemainingDept(remainingDept, loan);

			return PayLoanResponse.builder()
					.installmentsPaid(installmentsPaid)
					.totalAmountPaid(totalPaidAmount)
					.loanPaid(loan.isPaid())
					.remainingDept(remainingDept)
					.remainingAmount(remainingAmount)
					.build();

		} catch (Exception e) {
			throw new InternalServerException("Error processing loan payment: " + e.getMessage());
		}
	}

	private BigDecimal adjustPayment(LoanInstallment installment) {
		long daysDifference = ChronoUnit.DAYS.between(installment.getDueDate(), LocalDateTime.now());
		BigDecimal adjustmentFactor = installment.getAmount().multiply(LoanConstants.REWARD_PENALTY_COEFFICIENT).multiply(BigDecimal.valueOf(Math.abs(daysDifference)));
		return daysDifference < 0 ? installment.getAmount().subtract(adjustmentFactor) : installment.getAmount().add(adjustmentFactor);
	}

	private void processRemainingDept(BigDecimal remainingDept, Loan loan) {
		if (remainingDept == BigDecimal.ZERO) {
			loan.setPaid(true);
			loanRepository.save(loan);

			Customer customer = loan.getCustomer();
			customer.setUsedCreditLimit(customer.getUsedCreditLimit().subtract(loan.getLoanAmount())); // ?
			customerRepository.save(customer);
		}
	}

	private void validateCreateLoan(CreateLoanRequest createLoanRequest, BigDecimal creditLimit) {
		loanValidator.addSpecification(
				specificationFactory.getSpecification(SpecificationType.CREDIT_LIMIT, creditLimit),
				ChainingMethod.AND);
		loanValidator.addSpecification(specificationFactory.getSpecification(SpecificationType.INTEREST_RATE, null),
				ChainingMethod.AND);
		loanValidator.addSpecification(specificationFactory.getSpecification(SpecificationType.INSTALLMENT_NUMBER, null),
				ChainingMethod.AND);

		loanValidator.validate(createLoanRequest);
	}

	private BigDecimal calculateTotalLoanAmount(CreateLoanRequest createLoanRequest) {
		return createLoanRequest.getLoanAmount().add(createLoanRequest.getLoanAmount().multiply(createLoanRequest.getInterestRate()));
	}

	private Loan createLoanEntity(Customer customer, int numberOfInstallments, BigDecimal loanAmount) {
		return Loan.builder()
				.customer(customer)
				.loanAmount(loanAmount)
				.numberOfInstallment(numberOfInstallments)
				.createDate(LocalDateTime.now())
				.isPaid(false)
				.build();
	}

	public List<LoanInstallmentDto> createInstallments(Loan loan, BigDecimal totalLoanAmount) {
		List<LoanInstallmentDto> installments = new ArrayList<>();

		int numberOfInstallments = loan.getNumberOfInstallment();
		BigDecimal installmentAmount = totalLoanAmount.divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);

		LocalDateTime firstDueDate = LocalDateTime.now().plusMonths(1).withDayOfMonth(1).toLocalDate().atStartOfDay();

		Stream.iterate(firstDueDate, date -> date.plusMonths(1))
				.limit(numberOfInstallments)
				.forEach(dueDate -> {
					installments.add(loanInstallmentService.createLoanInstallment(loan, dueDate, installmentAmount));
				});

		return installments;
	}

}
