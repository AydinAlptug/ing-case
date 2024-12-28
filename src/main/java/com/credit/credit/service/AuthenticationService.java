package com.credit.credit.service;

import com.credit.credit.constants.Constants;
import com.credit.credit.model.entity.Role;
import com.credit.credit.model.entity.User;
import com.credit.credit.model.request.AuthenticationRequest;
import com.credit.credit.model.request.RegisterRequest;
import com.credit.credit.model.response.AuthenticationResponse;
import com.credit.credit.repository.IRoleRepository;
import com.credit.credit.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final JWTService jwtService;
	private final IUserRepository userRepository;
	private final IRoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthenticationResponse register(RegisterRequest request) {
		try {
			Role role = roleRepository.findByName(Constants.ROLE_CUSTOMER);
			User user = User.builder()
					.email(request.getEmail())
					.password(passwordEncoder.encode(request.getPassword()))
					.roles(List.of(role))
					.build();
			userRepository.save(user);

			String jwtToken = jwtService.generateToken(user);

			return AuthenticationResponse
					.builder()
					.token(jwtToken)
					.build();
		} catch (Exception e) {
			throw e;
		}
	}

	public AuthenticationResponse login(AuthenticationRequest request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getEmail(),
							request.getPassword()
					)
			);
			User user = (User) authentication.getPrincipal();
			String jwtToken = jwtService.generateToken(user);

			return AuthenticationResponse
					.builder()
					.token(jwtToken)
					.build();
		} catch (AuthenticationException e) {
			throw e;
		}
	}
}
