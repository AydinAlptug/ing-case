package com.credit.credit.service;

import com.credit.credit.model.dto.LoanInstallmentDto;
import com.credit.credit.model.entity.Loan;
import com.credit.credit.model.entity.LoanInstallment;
import com.credit.credit.model.response.ListLoanInstallmentsResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ILoanInstallmentService {
	ListLoanInstallmentsResponse getLoanInstallments(UUID loanId);

	LoanInstallmentDto createLoanInstallment(Loan loan, LocalDateTime dueDate, BigDecimal installmentAmount);

	List<LoanInstallment> getUnpaidInstallments(UUID loanId, boolean all);

	BigDecimal getRemainingDept(UUID loanId);

	LoanInstallment save(LoanInstallment installment);
}
