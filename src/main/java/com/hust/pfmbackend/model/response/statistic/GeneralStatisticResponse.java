package com.hust.pfmbackend.model.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class GeneralStatisticResponse {

    private long currentBalance;
    private long currentHave;
    private long currentBorrow;
    private long currentLend;

}
