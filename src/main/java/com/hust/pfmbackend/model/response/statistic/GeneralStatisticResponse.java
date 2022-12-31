package com.hust.pfmbackend.model.response.statistic;

import lombok.Data;

@Data
public class GeneralStatisticResponse {

    private long currentBalance;
    private long currentHave;
    private long currentDebt;

}
