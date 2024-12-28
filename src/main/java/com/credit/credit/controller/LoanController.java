package com.credit.credit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
public class LoanController {

	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.hasAccess(authentication, #userId)")
	public ResponseEntity<String> listLoans(@PathVariable UUID userId) {
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or @authorizationService.hasAccess(authentication, #userId)")
	public ResponseEntity<Void> createLoan(@PathVariable UUID userId) {
		return ResponseEntity.ok().build();
	}
}
