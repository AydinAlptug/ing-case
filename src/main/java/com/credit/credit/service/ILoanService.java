package com.credit.credit.service;

import com.credit.credit.model.request.CreateLoanRequest;
import com.credit.credit.model.request.PayLoanRequest;
import com.credit.credit.model.response.CreateLoanResponse;
import com.credit.credit.model.response.ListLoansResponse;
import com.credit.credit.model.response.PayLoanResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ILoanService {
	ListLoansResponse getLoans(UUID customerId);

	@Transactional
	CreateLoanResponse createLoan(CreateLoanRequest createLoanRequest, UUID customerId);

	@Transactional
	PayLoanResponse payLoan(UUID loanId, PayLoanRequest payLoanRequest);
}
