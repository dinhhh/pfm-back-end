package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.LimitExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitExpenseRepository extends JpaRepository<LimitExpense, String> {

    List<LimitExpense> findAllByUserNo(String userNo);

}
