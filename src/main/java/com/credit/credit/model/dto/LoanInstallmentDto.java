package com.credit.credit.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanInstallmentDto {

	private UUID id;
	private UUID loanId;
	private BigDecimal amount;
	private BigDecimal paidAmount;
	private LocalDateTime dueDate;
	private LocalDateTime paymentDate;
	private boolean isPaid;
}