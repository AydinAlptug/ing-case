package com.credit.credit.repository;

import com.credit.credit.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository  extends JpaRepository<Role, Integer> {
	Role findByName(String name);
}