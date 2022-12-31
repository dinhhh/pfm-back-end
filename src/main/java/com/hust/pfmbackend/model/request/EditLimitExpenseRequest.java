package com.hust.pfmbackend.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class EditLimitExpenseRequest {

    @NotBlank
    private String limitExpenseNo;

}
