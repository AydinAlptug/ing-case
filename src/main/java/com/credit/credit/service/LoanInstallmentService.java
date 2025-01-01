package com.credit.credit.service;

import com.credit.credit.constants.LoanConstants;
import com.credit.credit.exception.common.InternalServerException;
import com.credit.credit.model.dto.LoanInstallmentDto;
import com.credit.credit.model.entity.Loan;
import com.credit.credit.model.entity.LoanInstallment;
import com.credit.credit.model.mapper.Mapper;
import com.credit.credit.model.response.ListLoanInstallmentsResponse;
import com.credit.credit.repository.ILoanInstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanInstallmentService implements ILoanInstallmentService {

	private final ILoanInstallmentRepository loanInstallmentRepository;

	private final Mapper mapper;

	@Override
	public ListLoanInstallmentsResponse getLoanInstallments(UUID loanId) {
		try {
			List<LoanInstallment> installments = loanInstallmentRepository.findByLoanId(loanId);
			List<LoanInstallmentDto> installmentDtos = installments.stream()
					.map(mapper::toLoanInstallmentDto)
					.collect(Collectors.toList());

			return ListLoanInstallmentsResponse.builder()
					.loanId(loanId)
					.installments(installmentDtos)
					.build();
		} catch (Exception e) {
			throw new InternalServerException(e.getMessage());
		}
	}

	@Override
	public LoanInstallmentDto createLoanInstallment(Loan loan, LocalDateTime dueDate, BigDecimal installmentAmount) {
		LoanInstallment installment = LoanInstallment.builder()
				.isPaid(false)
				.amount(installmentAmount)
				.loan(loan)
				.dueDate(dueDate)
				.paymentDate(null)
				.paidAmount(BigDecimal.ZERO)
				.build();
		loanInstallmentRepository.save(installment);
		return mapper.toLoanInstallmentDto(installment);
	}

	@Override
	public List<LoanInstallment> getUnpaidInstallments(UUID loanId, boolean all) {
		List<LoanInstallment> installments = loanInstallmentRepository.findByLoanId(loanId).stream()
				.filter(i -> !i.isPaid())
				.filter(i -> all || i.getDueDate().isBefore(LocalDateTime.now().plusMonths(LoanConstants.MAX_UPFRONT_PAYMENT_COUNT)))
				.sorted(Comparator.comparing(LoanInstallment::getDueDate))
				.collect(Collectors.toList());

		return installments;
	}

	@Override
	public BigDecimal getRemainingDept(UUID loanId){
		return loanInstallmentRepository.findByLoanId(loanId) // repeating call
				.stream()
				.filter(i -> !i.isPaid())
				.map(LoanInstallment::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public LoanInstallment save(LoanInstallment installment) {
		return loanInstallmentRepository.save(installment);
	}
}
