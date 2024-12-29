package com.credit.credit.exception;

public class InvalidInterestRateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidInterestRateException() {
		super();
	}

	public InvalidInterestRateException(String message) {
		super(message);
	}
}
