package com.credit.credit.service;

import com.credit.credit.exception.InsufficientLimitException;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

	public void createLoan() throws InsufficientLimitException {
		throw new InsufficientLimitException("");
	}
}
