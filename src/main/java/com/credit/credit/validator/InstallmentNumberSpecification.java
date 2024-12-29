package com.credit.credit.validator;

import com.credit.credit.constants.LoanConstants;
import com.credit.credit.exception.InvalidInstallmentNumberException;
import com.credit.credit.model.request.CreateLoanRequest;

public class InstallmentNumberSpecification implements ISpecification<CreateLoanRequest> {

	@Override
	public boolean isSatisfiedBy(CreateLoanRequest createLoanRequest) {
		if (!LoanConstants.VALID_INSTALLMENT_NUMBERS.contains(createLoanRequest.getNumberOfInstallments())) {
			throw new InvalidInstallmentNumberException(
					"Invalid installment number. Provided: " + createLoanRequest.getNumberOfInstallments() +
							" Valid options: " + LoanConstants.VALID_INSTALLMENT_NUMBERS);
		}
		return true;
	}
}