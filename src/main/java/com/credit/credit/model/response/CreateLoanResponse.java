package com.credit.credit.model.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CreateLoanResponse {
	private BigDecimal loanAmount;
	private BigDecimal totalLoanAmount;
	private BigDecimal monthlyInstallment;
	private int numberOfInstallments;
	// TODO private List<InstallmentDto> installments;
}
