package com.hust.pfmbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank
    private String name;

    private String parentCategoryNo;

    private String description;

    @NotNull
    private int operationTypeCode;

}
