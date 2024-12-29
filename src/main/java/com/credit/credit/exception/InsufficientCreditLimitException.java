package com.credit.credit.exception;

public class InsufficientCreditLimitException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientCreditLimitException() {
		super();
	}

	public InsufficientCreditLimitException(String message) {
		super(message);
	}

}
