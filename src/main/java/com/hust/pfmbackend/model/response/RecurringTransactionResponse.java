package com.hust.pfmbackend.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RecurringTransactionResponse {

    private String recurringTransactionNo;
    private int operationCode;
    private String startDate;
    private String endDate;
    private int periodCode;
    private String description;
    private String name;
    private long amount;

}
