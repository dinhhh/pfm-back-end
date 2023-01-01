package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.Debt;
import com.hust.pfmbackend.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, String> {

    List<Debt> findAllByUserNoAndOperationType(String userNo, OperationType type);

    List<Debt> findAllByUserNoAndUserDebtInfoNo(String userNo, String userDebtInfoNo);
}
