package com.hust.pfmbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewRecurringTransactionRequest {

    @NotNull
    private long amount;

    @NotNull
    private int operationCode;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private int periodCode;

    private String description;

    @NotBlank
    private String name;

    private String walletNo;

}
