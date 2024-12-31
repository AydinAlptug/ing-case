package com.credit.credit.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	@NotEmpty(message = "Email cannot be empty")
	@Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email format")
	private String email;
	@NotEmpty(message = "Password cannot be empty")
	private String password;
	@NotNull(message = "Name cannot be empty")
	@NotEmpty(message = "Name cannot be empty")
	private String name;
	@NotEmpty(message = "Surname cannot be empty")
	private String surname;
	@NotNull(message = "Credit limit cannot be empty")
	private BigDecimal creditLimit;
}