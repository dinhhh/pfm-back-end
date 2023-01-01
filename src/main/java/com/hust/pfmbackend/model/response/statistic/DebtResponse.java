package com.hust.pfmbackend.model.response.statistic;

import com.hust.pfmbackend.entity.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DebtResponse {

    private long totalDebt;
    private long remainingDebt;
    private long returnedDebt;
    private List<UserDebtInfo> infoList;

    @Data
    @AllArgsConstructor
    @Builder
    public static class UserDebtInfo {
        private String userDebtInfoNo;
        private String userName;
        private long amount;
        private OperationType type;
    }
}
