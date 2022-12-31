package com.hust.pfmbackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDebtInfoResponse {

    private String userDebtInfoNo;
    private String name;

}
