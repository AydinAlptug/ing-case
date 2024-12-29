package com.credit.credit.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateLoanRequest {
	private BigDecimal loanAmount;
	private BigDecimal interestRate;
	private int numberOfInstallments;
}
