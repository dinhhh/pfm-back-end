package com.hust.pfmbackend.repository;

import com.hust.pfmbackend.entity.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, String> {

    List<RecurringTransaction> findAllByUserNo(String userNo);

}
