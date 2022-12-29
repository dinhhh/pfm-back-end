package com.hust.pfmbackend.model.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LimitExpenseResponse {

    private String name;
    private String walletName;
    private long amount;
    private long remainingAmount;
    private long dayLeft;
    private String startDate;
    private String endDate;

}
