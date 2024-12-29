package com.credit.credit.constants;

import java.math.BigDecimal;
import java.util.List;

public class LoanConstants {
	public static final List<Integer> VALID_INSTALLMENT_NUMBERS = List.of(6, 9, 12, 24);

	public static final BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.1");
	public static final BigDecimal MAX_INTEREST_RATE = new BigDecimal("0.5");

	private LoanConstants() {}
}
