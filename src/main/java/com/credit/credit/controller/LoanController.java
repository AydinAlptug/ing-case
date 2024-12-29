package com.credit.credit.controller;

import com.credit.credit.model.request.CreateLoanRequest;
import com.credit.credit.model.response.CreateLoanResponse;
import com.credit.credit.model.response.ListLoanInstallmentsResponse;
import com.credit.credit.model.response.ListLoansResponse;
import com.credit.credit.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
public class LoanController {

	private final LoanService loanService;

	@GetMapping("/{customerId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.hasAccess(authentication, #customerId)")
	public ResponseEntity<ListLoansResponse> listLoans(@PathVariable UUID customerId) {
		return ResponseEntity.ok(loanService.getLoans(customerId));
	}

	@GetMapping("/installment/{loanId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.hasAccessToLoan(authentication, #loanId)")
	public ResponseEntity<ListLoanInstallmentsResponse> listLoanInstallments(@PathVariable UUID loanId) {
		return ResponseEntity.ok(loanService.getLoanInstallments(loanId));
	}

	@PostMapping("/{customerId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.hasAccess(authentication, #customerId)")
	public ResponseEntity<CreateLoanResponse> createLoan(@PathVariable UUID customerId, @RequestBody CreateLoanRequest createLoanRequest) {
		return ResponseEntity.ok(loanService.createLoan(createLoanRequest, customerId));
	}
}
