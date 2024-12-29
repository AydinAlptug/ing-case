package com.credit.credit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler({ InsufficientCreditLimitException.class})
	public ResponseEntity<Object> handleNotEnoughLimitException(InsufficientCreditLimitException exception) {
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(exception.getMessage());
	}
	@ExceptionHandler({ InvalidInstallmentNumberException.class})
	public ResponseEntity<Object> handleInvalidInstallmentNumberException(InvalidInstallmentNumberException exception) {
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(exception.getMessage());
	}
	@ExceptionHandler({ InvalidInterestRateException.class})
	public ResponseEntity<Object> handleInvalidInterestRateException(InvalidInterestRateException exception) {
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
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
}
