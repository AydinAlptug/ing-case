package com.credit.credit.repository;

import com.credit.credit.model.entity.Customer;
import com.credit.credit.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ILoanRepository extends JpaRepository<Loan, UUID> {
	List<Loan> findByCustomer(Customer customer);
	List<Loan> findByCustomerId(UUID customerId);
}
