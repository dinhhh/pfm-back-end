package com.hust.pfmbackend.service;

import com.hust.pfmbackend.model.request.UserDebtInfoRequest;
import com.hust.pfmbackend.model.response.UserDebtInfoResponse;

import java.util.List;

public interface UserDebtInfoService {

    boolean save(UserDebtInfoRequest request);

    List<UserDebtInfoResponse> getAll();

}
