package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.ExpenseIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseIncomeRepository extends JpaRepository<ExpenseIncome, String> {
}
