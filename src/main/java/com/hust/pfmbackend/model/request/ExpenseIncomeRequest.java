package com.hust.pfmbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseIncomeRequest {

    @NotNull
    private Date createdOn;

    @NotNull
    private long amount;

    private String description;

    @NotBlank
    private String walletNo;

    @NotBlank
    private String categoryNo;

    @NotNull
    private int operationCode;

}
