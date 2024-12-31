package com.credit.credit.exception;

import com.credit.credit.exception.common.BadRequestException;
import com.credit.credit.exception.common.EntityNotFoundException;
import com.credit.credit.exception.common.InternalServerException;
import com.credit.credit.exception.loan.InsufficientCreditLimitException;
import com.credit.credit.exception.loan.InvalidCredentialsException;
import com.credit.credit.exception.loan.InvalidInstallmentNumberException;
import com.credit.credit.exception.loan.InvalidInterestRateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler({ InsufficientCreditLimitException.class})
	public ResponseEntity<Object> handleNotEnoughLimitException(InsufficientCreditLimitException exception) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(exception.getMessage());
	}
	@ExceptionHandler({ InvalidInstallmentNumberException.class})
	public ResponseEntity<Object> handleInvalidInstallmentNumberException(InvalidInstallmentNumberException exception) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(exception.getMessage());
	}
	@ExceptionHandler({ InvalidInterestRateException.class})
	public ResponseEntity<Object> handleInvalidInterestRateException(InvalidInterestRateException exception) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(exception.getMessage());
	}

	@ExceptionHandler({ InvalidCredentialsException.class})
	public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException exception) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(exception.getMessage());
	}

	@ExceptionHandler({ EntityNotFoundException.class})
	public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(exception.getMessage());
	}

	@ExceptionHandler({ InternalServerException.class})
	public ResponseEntity<Object> handleInternalServerException(InternalServerException exception) {
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(exception.getMessage());
	}
	@ExceptionHandler({ AccessDeniedException.class})
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body("Access denied");
	}

	@ExceptionHandler({ BadRequestException.class})
	public ResponseEntity<Object> handleBadRequestException(BadRequestException exception) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(exception.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
