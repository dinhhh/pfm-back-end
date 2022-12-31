package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.request.CategoryRequest;
import com.hust.pfmbackend.model.request.EditCategoryRequest;
import com.hust.pfmbackend.model.response.CategoryResponse;
import com.hust.pfmbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(path = "/category/new")
    public ResponseEntity<String> save(@RequestBody CategoryRequest request) {
        return categoryService.save(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/category/get-all-expense")
    public ResponseEntity<List<CategoryResponse>> getAllExpense() {
        return new ResponseEntity<>(categoryService.getAllExpense(), HttpStatus.OK);
    }

    @GetMapping(path = "/category/get-all-income")
    public ResponseEntity<List<CategoryResponse>> getAllIncome() {
        return new ResponseEntity<>(categoryService.getAllIncome(), HttpStatus.OK);
    }

    @PutMapping(path = "/category/update")
    public ResponseEntity<String> update(@RequestBody EditCategoryRequest request) {
        return categoryService.update(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "/category/delete")
    public ResponseEntity<String> delete(@RequestBody EditCategoryRequest request) {
        return categoryService.delete(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
