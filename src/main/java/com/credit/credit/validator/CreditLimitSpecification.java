package com.credit.credit.validator;

import com.credit.credit.exception.InsufficientCreditLimitException;
import com.credit.credit.model.request.CreateLoanRequest;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreditLimitSpecification implements ISpecification<CreateLoanRequest> {

	private final BigDecimal creditLimit;

	@Override
	public boolean isSatisfiedBy(CreateLoanRequest createLoanRequest) {
		if(createLoanRequest.getLoanAmount().compareTo(creditLimit) > 0) {
			throw new InsufficientCreditLimitException("Insufficient credit limit. Requested amount: " + createLoanRequest.getLoanAmount() + " Credit limit: " + creditLimit);
		}
		return true;
	}
}