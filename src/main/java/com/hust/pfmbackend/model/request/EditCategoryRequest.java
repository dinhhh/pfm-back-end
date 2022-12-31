package com.hust.pfmbackend.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class EditCategoryRequest {

    @NotBlank
    private String categoryNo;
    private String name;
    private String description;

}
