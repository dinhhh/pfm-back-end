package com.hust.pfmbackend.service;

import com.hust.pfmbackend.model.request.CategoryRequest;
import com.hust.pfmbackend.model.request.EditCategoryRequest;
import com.hust.pfmbackend.model.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    boolean save(CategoryRequest request);

    List<CategoryResponse> getAllExpense();

    List<CategoryResponse> getAllIncome();

    boolean update(EditCategoryRequest request);

    boolean delete(EditCategoryRequest request);

}
