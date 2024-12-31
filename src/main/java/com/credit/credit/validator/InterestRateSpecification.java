package com.credit.credit.validator;

import com.credit.credit.constants.LoanConstants;
import com.credit.credit.exception.loan.InvalidInterestRateException;
import com.credit.credit.model.request.CreateLoanRequest;

import java.math.BigDecimal;

public class InterestRateSpecification implements ISpecification<CreateLoanRequest> {

	@Override
	public boolean isSatisfiedBy(CreateLoanRequest createLoanRequest) {
		BigDecimal interestRate = createLoanRequest.getInterestRate();

		if (interestRate.compareTo(LoanConstants.MIN_INTEREST_RATE) < 0 ||
				interestRate.compareTo(LoanConstants.MAX_INTEREST_RATE) > 0) {

			throw new InvalidInterestRateException(
					"Invalid interest rate. Provided: " + interestRate +
							" Valid range: " + LoanConstants.MIN_INTEREST_RATE + " to " + LoanConstants.MAX_INTEREST_RATE);
		}
		return true;
	}
}