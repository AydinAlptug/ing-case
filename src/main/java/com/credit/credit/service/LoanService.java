package com.credit.credit.service;

import com.credit.credit.enums.ChainingMethod;
import com.credit.credit.enums.SpecificationType;
import com.credit.credit.model.entity.Customer;
import com.credit.credit.model.entity.Loan;
import com.credit.credit.model.entity.LoanInstallment;
import com.credit.credit.model.request.CreateLoanRequest;
import com.credit.credit.model.response.CreateLoanResponse;
import com.credit.credit.repository.ICustomerRepository;
import com.credit.credit.repository.ILoanRepository;
import com.credit.credit.repository.ILoanInstallmentRepository;
import com.credit.credit.repository.IUserRepository;
import com.credit.credit.validator.LoanValidator;
import com.credit.credit.validator.SpecificationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LoanService {

	private final IUserRepository userRepository;

	private final ICustomerRepository customerRepository;

	private final ILoanRepository loanRepository;

	private final ILoanInstallmentRepository loanInstallmentRepository;

	private final SpecificationFactory specificationFactory;

	private final LoanValidator loanValidator;

	@Transactional
	public CreateLoanResponse createLoan(CreateLoanRequest createLoanRequest, UUID userId) {
		BigDecimal totalLoanAmount = BigDecimal.ZERO;
		try {
			Customer customer = userRepository.findById(userId).get().getCustomer();
			BigDecimal remainingLimit = customer.getCreditLimit().subtract(customer.getUsedCreditLimit());
			if(validateCreateLoan(createLoanRequest, remainingLimit)) {
				totalLoanAmount = calculateTotalLoanAmount(createLoanRequest);
				Loan loan = createLoanEntity(customer, createLoanRequest.getNumberOfInstallments(), totalLoanAmount);
				loanRepository.save(loan);

				createInstallments(loan, totalLoanAmount);

				customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(createLoanRequest.getLoanAmount()));
				customerRepository.save(customer);
			}
		} catch (Exception e) {
			throw e;
		}
		return CreateLoanResponse.builder()
				.loanAmount(createLoanRequest.getLoanAmount())
				.totalLoanAmount(totalLoanAmount)
				.numberOfInstallments(createLoanRequest.getNumberOfInstallments())
				.build(); // TODO
	}

	private boolean validateCreateLoan(CreateLoanRequest createLoanRequest, BigDecimal creditLimit) {
		loanValidator.addSpecification(
				specificationFactory.getSpecification(SpecificationType.CREDIT_LIMIT, creditLimit),
				ChainingMethod.AND);
		loanValidator.addSpecification(specificationFactory.getSpecification(SpecificationType.INTEREST_RATE, null),
				ChainingMethod.AND);
		loanValidator.addSpecification(specificationFactory.getSpecification(SpecificationType.INSTALLMENT_NUMBER, null),
				ChainingMethod.AND);

		return loanValidator.validate(createLoanRequest);
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

	private void createInstallments(Loan loan, BigDecimal totalLoanAmount) {
		int numberOfInstallments = loan.getNumberOfInstallment();
		BigDecimal installmentAmount = totalLoanAmount.divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);

		LocalDateTime firstDueDate = LocalDateTime.now().plusMonths(1).withDayOfMonth(1).toLocalDate().atStartOfDay();

		Stream.iterate(firstDueDate, date -> date.plusMonths(1))
				.limit(numberOfInstallments)
				.forEach(dueDate -> {
					LoanInstallment installment = LoanInstallment.builder()
							.isPaid(false)
							.amount(installmentAmount)
							.loan(loan)
							.dueDate(dueDate)
							.paymentDate(null)
							.paidAmount(BigDecimal.ZERO)
							.build();
					loanInstallmentRepository.save(installment);
				});
	}

}
