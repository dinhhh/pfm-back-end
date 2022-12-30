package com.hust.pfmbackend.service;

import com.hust.pfmbackend.model.request.EditRecurringTransactionRequest;
import com.hust.pfmbackend.model.request.NewRecurringTransactionRequest;
import com.hust.pfmbackend.model.response.RecurringTransactionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecurringTransactionService {

    boolean save(NewRecurringTransactionRequest request);

    List<RecurringTransactionResponse> getAll();

    boolean update(EditRecurringTransactionRequest request);

    boolean delete(String recurringTransactionNo);

}
