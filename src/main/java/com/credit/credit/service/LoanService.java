package com.credit.credit.service;

import com.credit.credit.enums.ChainingMethod;
import com.credit.credit.enums.SpecificationType;
import com.credit.credit.model.entity.Customer;
import com.credit.credit.model.entity.Loan;
import com.credit.credit.model.request.CreateLoanRequest;
import com.credit.credit.model.response.CreateLoanResponse;
import com.credit.credit.repository.ICustomerRepository;
import com.credit.credit.repository.ILoanRepository;
import com.credit.credit.repository.IUserRepository;
import com.credit.credit.validator.LoanValidator;
import com.credit.credit.validator.SpecificationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanService {

	private final IUserRepository userRepository;

	private final ICustomerRepository customerRepository;

	private final ILoanRepository loanRepository;

	private final SpecificationFactory specificationFactory;

	private final LoanValidator loanValidator;

	@Transactional
	public CreateLoanResponse createLoan(CreateLoanRequest createLoanRequest, UUID userId) {
		try {
			Customer customer = userRepository.findById(userId).get().getCustomer();
			if(validateCreateLoan(createLoanRequest, customer.getCreditLimit())) {
				BigDecimal totalLoanAmount = BigDecimal.valueOf(1); // TODO - calculateTotalLoanAmount(createLoanRequest);
				Loan loan = createLoanEntity(customer, createLoanRequest.getNumberOfInstallments(), totalLoanAmount);
				loanRepository.save(loan);

				// TODO createInstallments(loan, ...);

				customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(totalLoanAmount));
				customerRepository.save(customer);
			}
		} catch (Exception e) {
			throw e;
		}
		return CreateLoanResponse.builder().build(); // TO DO
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

	private Loan createLoanEntity(Customer customer, int numberOfInstallments, BigDecimal loanAmount) {
		return Loan.builder()
				.customer(customer)
				.loanAmount(loanAmount)
				.numberOfInstallment(numberOfInstallments)
				.createDate(LocalDateTime.now())
				.isPaid(false)
				.build();
	}
}
