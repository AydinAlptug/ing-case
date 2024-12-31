package com.credit.credit.service;

import com.credit.credit.exception.common.InternalServerException;
import com.credit.credit.model.dto.LoanInstallmentDto;
import com.credit.credit.model.entity.Loan;
import com.credit.credit.model.entity.LoanInstallment;
import com.credit.credit.model.mapper.Mapper;
import com.credit.credit.model.response.ListLoanInstallmentsResponse;
import com.credit.credit.repository.ILoanInstallmentRepository;
import com.credit.credit.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanInstallmentServiceTests {

	@Mock
	private IUserRepository userRepository;

	@Mock
	private ILoanInstallmentRepository loanInstallmentRepository;

	@Mock
	private Mapper mapper;

	@InjectMocks
	private LoanInstallmentService loanInstallmentService;

	private UUID loanId;
	private Loan loan;
	private LoanInstallment loanInstallment;
	private LoanInstallmentDto loanInstallmentDto;

	@BeforeEach
	void setUp() {
		loanId = UUID.randomUUID();
		loan = Loan.builder().build();
		loanInstallment = LoanInstallment.builder()
				.loan(loan)
				.dueDate(LocalDateTime.now().plusMonths(1))
				.amount(BigDecimal.valueOf(1000))
				.isPaid(false)
				.build();
		loanInstallmentDto = LoanInstallmentDto.builder()
				.loanId(loan.getId())
				.dueDate(loanInstallment.getDueDate())
				.amount(loanInstallment.getAmount())
				.isPaid(loanInstallment.isPaid())
				.build();
	}

	@Test
	void getLoanInstallments_Success() {
		// Given
		when(loanInstallmentRepository.findByLoanId(loanId)).thenReturn(List.of(loanInstallment));
		Mockito.when(mapper.toLoanInstallmentDto(loanInstallment)).thenReturn(loanInstallmentDto);

		// When
		ListLoanInstallmentsResponse response = loanInstallmentService.getLoanInstallments(loanId);

		// Then
		assertNotNull(response);
		assertEquals(loanId, response.getLoanId());
		assertEquals(1, response.getInstallments().size());
		assertEquals(loanInstallmentDto, response.getInstallments().get(0));
		verify(loanInstallmentRepository, times(1)).findByLoanId(loanId);
	}

	@Test
	void createLoanInstallment_Success() {
		// Given
		LocalDateTime dueDate = LocalDateTime.now().plusMonths(2);
		BigDecimal installmentAmount = BigDecimal.valueOf(2000);
		when(loanInstallmentRepository.save(any(LoanInstallment.class))).thenReturn(loanInstallment);
		when(mapper.toLoanInstallmentDto(any(LoanInstallment.class))).thenReturn(loanInstallmentDto);

		// When
		LoanInstallmentDto createdInstallment = loanInstallmentService.createLoanInstallment(loan, dueDate, installmentAmount);

		// Then
		assertNotNull(createdInstallment);
		verify(loanInstallmentRepository, times(1)).save(any(LoanInstallment.class));
	}

	@Test
	void givenAllFalse_getUnpaidInstallments_ReturnsNotAll() {
		// Given
		List<LoanInstallment>  installments = getLoanInstallmentsByGivenSize(10);
		when(loanInstallmentRepository.findByLoanId(loanId)).thenReturn(installments);

		// When
		List<LoanInstallment> unpaidInstallments = loanInstallmentService.getUnpaidInstallments(loanId, false);

		// Then
		assertNotNull(unpaidInstallments);
		assertTrue(unpaidInstallments.size() < installments.size());
		assertFalse(unpaidInstallments.get(0).isPaid());
		verify(loanInstallmentRepository, times(1)).findByLoanId(loanId);
	}

	@Test
	void givenAllTrue_getUnpaidInstallments_ReturnsAll() {
		// Given
		List<LoanInstallment>  installments = getLoanInstallmentsByGivenSize(10);
		when(loanInstallmentRepository.findByLoanId(loanId)).thenReturn(installments);

		// When
		List<LoanInstallment> unpaidInstallments = loanInstallmentService.getUnpaidInstallments(loanId, true);

		// Then
		assertNotNull(unpaidInstallments);
		assertEquals(10, unpaidInstallments.size());
		assertFalse(unpaidInstallments.get(0).isPaid());
		verify(loanInstallmentRepository, times(1)).findByLoanId(loanId);
	}

	@Test
	void getRemainingDept_Success() {
		// Given
		when(loanInstallmentRepository.findByLoanId(loanId)).thenReturn(List.of(loanInstallment));

		// When
		BigDecimal remainingDept = loanInstallmentService.getRemainingDept(loanId);

		// Then
		assertEquals(BigDecimal.valueOf(1000), remainingDept);
		verify(loanInstallmentRepository, times(1)).findByLoanId(loanId);
	}

	@Test
	void getLoanInstallmentsThrowsException_Success() {
		// Given
		when(loanInstallmentRepository.findByLoanId(loanId)).thenThrow(new RuntimeException("Database error"));

		// When & Then
		InternalServerException thrown = assertThrows(InternalServerException.class, () -> {
			loanInstallmentService.getLoanInstallments(loanId);
		});
		assertEquals("Database error", thrown.getMessage());
	}

	private List<LoanInstallment> getLoanInstallmentsByGivenSize(int i) {
		List<LoanInstallment> installments = new ArrayList<>();

		for (int j = 1; j < i + 1; j++) {
			installments.add(LoanInstallment.builder()
					.id(UUID.randomUUID())
					.loan(loan)
					.dueDate(LocalDateTime.now().plusMonths(j))
					.amount(BigDecimal.valueOf(1000))
					.isPaid(false)
					.build());
		}
		return installments;
	}
}