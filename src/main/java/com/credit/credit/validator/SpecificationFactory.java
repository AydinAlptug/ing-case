package com.credit.credit.validator;

import com.credit.credit.enums.SpecificationType;
import com.credit.credit.model.request.CreateLoanRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SpecificationFactory {

	public ISpecification<CreateLoanRequest> getSpecification(SpecificationType specificationType, Object param) {
		switch (specificationType) {
		case INSTALLMENT_NUMBER:
			return new InstallmentNumberSpecification();
		case INTEREST_RATE:
			return new InterestRateSpecification();
		case CREDIT_LIMIT:
			if (param instanceof BigDecimal) {
				return new CreditLimitSpecification((BigDecimal) param);
			}
			throw new IllegalArgumentException("Invalid parameter for CREDIT_LIMIT specification");
		default:
			throw new IllegalArgumentException("Unknown specification type: " + specificationType);
		}
	}
}