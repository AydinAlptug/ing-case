package com.credit.credit.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayLoanResponse {
	private int installmentsPaid;
	private BigDecimal totalAmountPaid;
	private boolean loanPaid;
	private BigDecimal remainingDept;
	private BigDecimal remainingAmount;
}
