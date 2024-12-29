package com.credit.credit.service;

import com.credit.credit.model.entity.User;
import com.credit.credit.repository.ILoanRepository;
import com.credit.credit.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

	private final IUserRepository userRepository;

	private final ILoanRepository loanRepository;

	public boolean hasAccess(Authentication authentication, UUID customerId) {
		UUID authenticatedUserId = ((User) authentication.getPrincipal()).getId();
		UUID authenticatedCustomerId = userRepository.findById(authenticatedUserId).get().getCustomer().getId();
		return authenticatedCustomerId.equals(customerId) ;
	}

	public boolean hasAccessToLoan(Authentication authentication, UUID loanId) {
		UUID authenticatedUserId = ((User) authentication.getPrincipal()).getId();
		UUID authenticatedCustomerId = userRepository.findById(authenticatedUserId).get().getCustomer().getId();
		UUID customerId = loanRepository.findById(loanId).get().getCustomer().getId();
		return authenticatedCustomerId.equals(customerId) ;
	}
}