package com.hust.pfmbackend.model.response;

import com.hust.pfmbackend.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private String parentCategoryNo;
    private String parentCategoryName;
    private String parentCategoryDescription;
    private List<Category> subCategories;

}
