package com.credit.credit.exception.common;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {
		super();
	}

	public EntityNotFoundException(String message) {
		super(message);
	}
}
