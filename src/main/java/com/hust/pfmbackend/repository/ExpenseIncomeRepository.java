package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.ExpenseIncome;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseIncomeRepository extends JpaRepository<ExpenseIncome, String> {

    List<ExpenseIncome> findAllByUserAndOperationType(User user, OperationType ot);
}
