package com.credit.credit.exception.loan;

public class InvalidInstallmentNumberException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidInstallmentNumberException() {
		super();
	}

	public InvalidInstallmentNumberException(String message) {
		super(message);
	}
}
