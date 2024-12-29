package com.credit.credit.repository;

import com.credit.credit.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ICustomerRepository extends JpaRepository<Customer, UUID> {
	Optional<Customer> findById(UUID id);
}
