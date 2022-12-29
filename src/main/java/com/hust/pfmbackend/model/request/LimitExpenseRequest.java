package com.hust.pfmbackend.model.request;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LimitExpenseRequest {

    @NotNull
    private long amount;

    @NotNull
    private int periodTypeCode;

    @NotBlank
    private String name;

    @NotBlank
    private String walletNo;

    private String categoryNo;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

}
