package com.credit.credit.model.mapper;

import com.credit.credit.model.dto.LoanDto;
import com.credit.credit.model.dto.LoanInstallmentDto;
import com.credit.credit.model.entity.Loan;
import com.credit.credit.model.entity.LoanInstallment;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

	public LoanDto toLoanDto(Loan loan) {
		if (loan == null) {
			return null;
		}
		return LoanDto.builder().id(loan.getId()).loanAmount(loan.getLoanAmount()).numberOfInstallment(loan.getNumberOfInstallment())
				.createDate(loan.getCreateDate()).isPaid(loan.isPaid()).build();
	}

	public LoanInstallmentDto toLoanInstallmentDto(LoanInstallment loanInstallment) {
		if (loanInstallment == null) {
			return null;
		}
		return LoanInstallmentDto.builder().id(loanInstallment.getId()).loanId(loanInstallment.getLoan().getId()).amount(loanInstallment.getAmount())
				.paidAmount(loanInstallment.getPaidAmount()).dueDate(loanInstallment.getDueDate()).paymentDate(loanInstallment.getPaymentDate())
				.isPaid(loanInstallment.isPaid()).build();
	}
}