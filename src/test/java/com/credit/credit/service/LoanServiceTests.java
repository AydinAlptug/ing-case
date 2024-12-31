package com.credit.credit.service;

import com.credit.credit.enums.ChainingMethod;
import com.credit.credit.enums.SpecificationType;
import com.credit.credit.exception.loan.InsufficientCreditLimitException;
import com.credit.credit.exception.loan.InvalidInstallmentNumberException;
import com.credit.credit.exception.loan.InvalidInterestRateException;
import com.credit.credit.model.dto.LoanDto;
import com.credit.credit.model.dto.LoanInstallmentDto;
import com.credit.credit.model.entity.Customer;
import com.credit.credit.model.entity.Loan;
import com.credit.credit.model.entity.LoanInstallment;
import com.credit.credit.model.mapper.Mapper;
import com.credit.credit.model.request.CreateLoanRequest;
import com.credit.credit.model.response.CreateLoanResponse;
import com.credit.credit.model.response.ListLoansResponse;
import com.credit.credit.repository.ICustomerRepository;
import com.credit.credit.repository.ILoanRepository;
import com.credit.credit.validator.CreditLimitSpecification;
import com.credit.credit.validator.InstallmentNumberSpecification;
import com.credit.credit.validator.InterestRateSpecification;
import com.credit.credit.validator.LoanValidator;
import com.credit.credit.validator.SpecificationFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTests {

	@Mock
	private ICustomerRepository customerRepository;

	@Mock
	private ILoanRepository loanRepository;

	@Mock
	private LoanInstallmentService loanInstallmentService;

	@Mock
	private SpecificationFactory specificationFactory;

	@Mock
	private LoanValidator loanValidator;

	@Mock
	private Mapper mapper;

	@InjectMocks
	private LoanService loanService;

	private UUID customerId;
	private Customer customer;
	private Loan loan;
	private int numberOfInstallments;

	@BeforeEach
	void setUp() {
		numberOfInstallments = 6;
		customerId = UUID.randomUUID();
		customer = Customer.builder().id(customerId).creditLimit(BigDecimal.valueOf(10000)).usedCreditLimit(BigDecimal.ZERO).build();
		loan = Loan.builder()
				.id(UUID.randomUUID())
				.customer(customer)
				.loanAmount(BigDecimal.valueOf(5500))
				.numberOfInstallment(numberOfInstallments)
				.createDate(LocalDateTime.now())
				.isPaid(false)
				.build();
	}

	@Test
	void shouldReturnLoansForCustomerWhenLoansExist() {
		// Given
		when(loanRepository.findByCustomerId(customerId)).thenReturn(List.of(loan));
		when(mapper.toLoanDto(loan)).thenReturn(LoanDto.builder()
				.id(loan.getId())
				.loanAmount(loan.getLoanAmount())
				.numberOfInstallment(loan.getNumberOfInstallment())
				.createDate(loan.getCreateDate())
				.isPaid(loan.isPaid())
				.build());

		// When
		ListLoansResponse response = loanService.getLoans(customerId);

		// Then
		assertNotNull(response);
		assertEquals(customerId, response.getCustomerId());
		assertEquals(1, response.getLoans().size());
		verify(loanRepository, times(1)).findByCustomerId(customerId);
	}

	@Test
	void shouldCreateLoanWhenValidRequestIsProvided() {
		// Given
		CreateLoanRequest createLoanRequest = CreateLoanRequest.builder()
				.loanAmount(BigDecimal.valueOf(5000))
				.numberOfInstallments(numberOfInstallments)
				.interestRate(BigDecimal.valueOf(0.01))
				.build();

		LoanInstallmentDto installment = getInstallment(loan, loan.getCreateDate().plusMonths(1));

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(loanRepository.save(any(Loan.class))).thenReturn(loan);
		when(loanInstallmentService.createLoanInstallment(any(Loan.class), any(LocalDateTime.class), any(BigDecimal.class)))
				.thenReturn(installment);
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		// When
		CreateLoanResponse response = loanService.createLoan(createLoanRequest, customerId);

		// Then
		assertNotNull(response);
		assertEquals(createLoanRequest.getLoanAmount(), response.getLoanAmount());
		verify(loanRepository, times(1)).save(any(Loan.class));
		verify(customerRepository, times(1)).save(any(Customer.class));
	}

	@ParameterizedTest
	@MethodSource("loanTestCases")
	void shouldThrow_CorrectLoanCreationExceptions(BigDecimal loanAmount, BigDecimal creditLimit, BigDecimal interestRate, int installmentAmount, BigDecimal usedCreditLimit, Class<? extends Exception> expectedException) {
		// Given
		CreateLoanRequest createLoanRequest = CreateLoanRequest.builder()
				.loanAmount(loanAmount)
				.numberOfInstallments(installmentAmount)
				.interestRate(interestRate)
				.build();

		customer.setCreditLimit(creditLimit);
		customer.setUsedCreditLimit(usedCreditLimit);

		CreditLimitSpecification creditLimitSpecification = new CreditLimitSpecification(customer.getCreditLimit().subtract(customer.getUsedCreditLimit()));
		InterestRateSpecification interestRateSpecification = new InterestRateSpecification();
		InstallmentNumberSpecification installmentNumberSpecification = new InstallmentNumberSpecification();

		when(specificationFactory.getSpecification(SpecificationType.CREDIT_LIMIT, customer.getCreditLimit().subtract(customer.getUsedCreditLimit())))
				.thenReturn(creditLimitSpecification);
		when(specificationFactory.getSpecification(SpecificationType.INTEREST_RATE, null))
				.thenReturn(interestRateSpecification);
		when(specificationFactory.getSpecification(SpecificationType.INSTALLMENT_NUMBER, null))
				.thenReturn(installmentNumberSpecification);

		doCallRealMethod().when(loanValidator).addSpecification(eq(creditLimitSpecification), eq(ChainingMethod.AND));
		doCallRealMethod().when(loanValidator).addSpecification(eq(interestRateSpecification), eq(ChainingMethod.AND));
		doCallRealMethod().when(loanValidator).addSpecification(eq(installmentNumberSpecification), eq(ChainingMethod.AND));
		doCallRealMethod().when(loanValidator).validate(any(CreateLoanRequest.class));

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

		// When & Then
		assertThrows(expectedException, () -> {
			loanService.createLoan(createLoanRequest, customerId);
		});
		verify(loanValidator, times(3)).addSpecification(any(), eq(ChainingMethod.AND));
		verify(loanValidator).validate(createLoanRequest);
	}

	static Stream<Arguments> loanTestCases() {
		return Stream.of(
				Arguments.of(BigDecimal.valueOf(100000), BigDecimal.valueOf(10000), BigDecimal.valueOf(0.1), 6, BigDecimal.valueOf(5000), InsufficientCreditLimitException.class),
				Arguments.of(BigDecimal.valueOf(1), BigDecimal.valueOf(10000), BigDecimal.valueOf(0.1), 1, BigDecimal.valueOf(5000), InvalidInstallmentNumberException.class),
				Arguments.of(BigDecimal.valueOf(1), BigDecimal.valueOf(10000), BigDecimal.valueOf(1.0), 6, BigDecimal.valueOf(5000), InvalidInterestRateException.class)
		);
	}

	private List<LoanInstallmentDto> getInstallments(Loan sourceLoan) {
		List<LoanInstallmentDto> installments = new ArrayList<>();

		LocalDateTime firstDueDate = LocalDateTime.now().plusMonths(1).withDayOfMonth(1).toLocalDate().atStartOfDay();

		Stream.iterate(firstDueDate, date -> date.plusMonths(1))
				.limit(numberOfInstallments)
				.forEach(dueDate -> {
					LoanInstallmentDto installmentDto = getInstallment(sourceLoan, dueDate);
					installments.add(installmentDto);
				});
		return installments;
	}

	private LoanInstallmentDto getInstallment(Loan sourceLoan, LocalDateTime dueDate) {
		BigDecimal loanAmount = sourceLoan.getLoanAmount();
		int numberOfInstallments = sourceLoan.getNumberOfInstallment();

		BigDecimal amount = loanAmount.divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);

		LoanInstallment installment = LoanInstallment.builder()
				.isPaid(false)
				.amount(amount)
				.loan(sourceLoan)
				.dueDate(dueDate)
				.paymentDate(null)
				.paidAmount(BigDecimal.ZERO)
				.build();
		LoanInstallmentDto installmentDto = mapper.toLoanInstallmentDto(installment);
		return installmentDto;
	}
}
