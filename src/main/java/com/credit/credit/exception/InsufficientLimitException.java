package com.credit.credit.exception;

public class InsufficientLimitException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientLimitException() {
		super();
	}

	public InsufficientLimitException(String message) {
		super(message);
	}

}
