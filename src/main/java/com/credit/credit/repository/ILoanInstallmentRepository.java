package com.credit.credit.repository;

import com.credit.credit.model.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ILoanInstallmentRepository extends JpaRepository<LoanInstallment, UUID> {
}
