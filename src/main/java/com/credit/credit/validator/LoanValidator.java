package com.credit.credit.validator;

import com.credit.credit.enums.ChainingMethod;
import com.credit.credit.model.request.CreateLoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanValidator {

	private ISpecification<CreateLoanRequest> currentSpecification;

	public void addSpecification(ISpecification<CreateLoanRequest> specification, ChainingMethod chainingMethod) {
		if (currentSpecification == null) {
			currentSpecification = specification;
		} else {
			switch (chainingMethod) {
			case AND:
				currentSpecification = currentSpecification.and(specification);
				break;
			case OR:
				currentSpecification = currentSpecification.or(specification);
				break;
			case NOT:
				currentSpecification = currentSpecification.not();
				break;
			}
		}
	}

	public boolean validate(CreateLoanRequest createLoanRequest) {
		if (currentSpecification == null) {
			throw new IllegalStateException("No specification added.");
		}
		return currentSpecification.isSatisfiedBy(createLoanRequest);
	}
}
