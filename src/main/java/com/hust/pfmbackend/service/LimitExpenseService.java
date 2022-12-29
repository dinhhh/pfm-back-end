package com.hust.pfmbackend.service;

import com.hust.pfmbackend.model.request.LimitExpenseRequest;
import com.hust.pfmbackend.model.response.LimitExpenseResponse;

import java.util.List;

public interface LimitExpenseService {

    boolean save(LimitExpenseRequest request);

    List<LimitExpenseResponse> getAll();

}
