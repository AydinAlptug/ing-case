package com.credit.credit.model.response;

import com.credit.credit.model.dto.LoanDto;
import com.credit.credit.model.dto.LoanInstallmentDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@RequiredArgsConstructor
public class ListLoanInstallmentsResponse {
	private final UUID loanId;
	private final List<LoanInstallmentDto> installments;
}

