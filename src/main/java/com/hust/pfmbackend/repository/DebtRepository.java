package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebtRepository extends JpaRepository<Debt, String> {
}
