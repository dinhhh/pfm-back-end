package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.request.ExpenseIncomeRequest;
import com.hust.pfmbackend.service.ExpenseIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpenseIncomeController {

    @Autowired
    private ExpenseIncomeService service;

    @PostMapping(path = "/expense-income/new")
    public ResponseEntity<String> save(@RequestBody ExpenseIncomeRequest request) {
        // TODO: Test with postman latter. Now add logic to create category
        return service.save(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
