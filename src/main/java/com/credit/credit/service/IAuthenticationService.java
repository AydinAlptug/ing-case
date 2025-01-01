package com.credit.credit.service;

import com.credit.credit.model.request.AuthenticationRequest;
import com.credit.credit.model.request.RegisterRequest;
import com.credit.credit.model.response.AuthenticationResponse;
import org.springframework.transaction.annotation.Transactional;

public interface IAuthenticationService {
	@Transactional
	AuthenticationResponse register(RegisterRequest request);

	AuthenticationResponse login(AuthenticationRequest request);
}
