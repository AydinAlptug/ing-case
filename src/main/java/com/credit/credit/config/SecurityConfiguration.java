package com.credit.credit.config;

import com.credit.credit.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final JWTAuthenticationFilter jwtAuthFilter;

	private final AuthenticationProvider authenticationProvider;

	private final Environment env;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		if (env.acceptsProfiles("dev")) {
			httpSecurity
					.authorizeHttpRequests(request -> request
							.requestMatchers("/h2-console/**").permitAll())
					.headers(headers -> headers.frameOptions().sameOrigin());
		}

		httpSecurity
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(request -> request.requestMatchers("/api/auth/**", "/error")
						.permitAll()
						.anyRequest()
						.authenticated()
				)
				.sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}
}
