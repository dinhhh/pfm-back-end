package com.hust.pfmbackend.service;

import com.hust.pfmbackend.model.request.DebtRequest;
import com.hust.pfmbackend.model.response.statistic.DebtResponse;

public interface DebtService {

    boolean save(DebtRequest request);

    DebtResponse getAllBorrow();

    DebtResponse getAllLend();

    boolean deleteByUserDebtInfoNo(String userDebtInfoNo);

}
