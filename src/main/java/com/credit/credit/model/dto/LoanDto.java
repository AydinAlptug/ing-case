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
public class LoanDto {

	private UUID id;
	private BigDecimal loanAmount;
	private int numberOfInstallment;
	private LocalDateTime createDate;
	private boolean isPaid;
}