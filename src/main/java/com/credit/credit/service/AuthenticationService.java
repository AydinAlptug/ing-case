package com.credit.credit.service;

import com.credit.credit.constants.Constants;
import com.credit.credit.exception.common.BadRequestException;
import com.credit.credit.exception.common.InternalServerException;
import com.credit.credit.exception.loan.InvalidCredentialsException;
import com.credit.credit.model.entity.Customer;
import com.credit.credit.model.entity.Role;
import com.credit.credit.model.entity.User;
import com.credit.credit.model.request.AuthenticationRequest;
import com.credit.credit.model.request.RegisterRequest;
import com.credit.credit.model.response.AuthenticationResponse;
import com.credit.credit.repository.ICustomerRepository;
import com.credit.credit.repository.IRoleRepository;
import com.credit.credit.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

	private static final Logger logger = LogManager.getLogger(AuthenticationService.class);

	private final AuthenticationManager authenticationManager;

	private final JWTService jwtService;

	private final IUserRepository userRepository;

	private final IRoleRepository roleRepository;

	private final ICustomerRepository customerRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public AuthenticationResponse register(RegisterRequest request) {
		try {
			logger.info("Registering a new user with email: {}", request.getEmail());

			userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
				throw new BadRequestException("The email address is already in use.");
			});

			Role role = roleRepository.findByName(Constants.ROLE_CUSTOMER);

			Customer customer = Customer.builder()
					.name(request.getName())
					.surname(request.getSurname())
					.creditLimit(request.getCreditLimit())
					.usedCreditLimit(BigDecimal.ZERO)
					.build();

			customerRepository.save(customer);

			User user = User.builder()
					.email(request.getEmail())
					.password(passwordEncoder.encode(request.getPassword()))
					.roles(List.of(role))
					.customer(customer)
					.build();

			userRepository.save(user);

			String jwtToken = jwtService.generateToken(user);

			logger.info("User registered successfully with email: {}", request.getEmail());

			return AuthenticationResponse
					.builder()
					.token(jwtToken)
					.customerId(customer.getId())
					.build();
		} catch (BadRequestException e) {
			logger.warn("The email address is already in use.");
			throw e;
		}  catch (IllegalArgumentException e) {
			logger.warn("An error occurred while registering the user: {}", e.getMessage());
			throw new BadRequestException(e.getMessage());
		} catch (Exception e) {
			logger.error("An error occurred while registering the user: {}", e.getMessage());
			throw new InternalServerException("An error occurred while registering the user.");
		}
	}

	@Override
	public AuthenticationResponse login(AuthenticationRequest request) {
		try {
			logger.info("Logging in with email: {}", request.getEmail());
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
					.customerId(user.getCustomer() != null ? user.getCustomer().getId() : null)
					.build();
		} catch (AuthenticationException e) {
			logger.warn("Invalid credentials.");
			throw new InvalidCredentialsException("Invalid credentials.");
		} catch (Exception e) {
			logger.error("An error occurred while logging in: {}", e.getMessage());
			throw new InternalServerException("An error occurred while logging in.");
		}
	}
}
