package com.credit.credit.service;

import com.credit.credit.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationService {

	public boolean hasAccess(Authentication authentication, UUID userId) {
		UUID authenticatedUserId = ((User) authentication.getPrincipal()).getId();
		return authenticatedUserId.equals(userId);
	}
}