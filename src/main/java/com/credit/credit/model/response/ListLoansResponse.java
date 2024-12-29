package com.credit.credit.model.response;

import com.credit.credit.model.dto.LoanDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@RequiredArgsConstructor
public class ListLoansResponse {
	private final UUID customerId;
	private final List<LoanDto> loans;
}
