package com.credit.credit.constants;

public class Constants {
	public static final long HOUR_IN_MILLISECONDS = 60 * 60 * 1000;  // 1 hour in milliseconds
	public static final long JWT_EXPIRATION_MILLISECONDS = HOUR_IN_MILLISECONDS / 4; // 15 minutes

	public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";

	private Constants() {}
}