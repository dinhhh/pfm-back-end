package com.hust.pfmbackend.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class EditRecurringTransactionRequest {

    @NotBlank
    private String recurringTransactionNo;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    @NotNull
    private long amount;

}
