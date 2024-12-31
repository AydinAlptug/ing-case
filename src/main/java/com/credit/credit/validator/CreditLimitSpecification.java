package com.credit.credit.validator;

import com.credit.credit.exception.loan.InsufficientCreditLimitException;
import com.credit.credit.model.request.CreateLoanRequest;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CreditLimitSpecification implements ISpecification<CreateLoanRequest> {

	private final BigDecimal remainingCreditLimit;

	@Override
	public boolean isSatisfiedBy(CreateLoanRequest createLoanRequest) {
		if(createLoanRequest.getLoanAmount().compareTo(remainingCreditLimit) > 0) {
			throw new InsufficientCreditLimitException("Insufficient credit limit. Requested amount: " + createLoanRequest.getLoanAmount() + " Credit limit: " + remainingCreditLimit);
		}
		return true;
	}
}