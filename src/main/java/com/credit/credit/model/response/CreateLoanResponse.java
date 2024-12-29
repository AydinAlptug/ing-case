package com.credit.credit.model.response;

import com.credit.credit.model.dto.LoanInstallmentDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class CreateLoanResponse {
	private UUID loanId;
	private BigDecimal loanAmount;
	private BigDecimal totalLoanAmount;
	private BigDecimal monthlyInstallment;
	private int numberOfInstallments;
	private List<LoanInstallmentDto> installments;
}
