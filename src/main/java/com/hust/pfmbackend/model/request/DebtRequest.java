package com.hust.pfmbackend.model.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString
public class DebtRequest {

    @NotNull
    private long amount;

    @NotNull
    private int operationCode;

    @NotNull
    private Date createdDate;

    private Date expectCollectDate;

    private String description;

    private String userDebtInfoNo;

    private int isNewUserDebtInfo;

    private String userDebtInfoName;
}
