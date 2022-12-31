package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.request.EditLimitExpenseRequest;
import com.hust.pfmbackend.model.request.LimitExpenseRequest;
import com.hust.pfmbackend.model.response.LimitExpenseResponse;
import com.hust.pfmbackend.service.LimitExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LimitExpenseController {

    @Autowired
    private LimitExpenseService limitExpenseService;

    @PostMapping(path = "/limit-expense/new")
    public ResponseEntity<String> save(@RequestBody LimitExpenseRequest request) {
        return limitExpenseService.save(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/limit-expense/get-all")
    public ResponseEntity<List<LimitExpenseResponse>> getAll() {
        return ResponseEntity.ok(limitExpenseService.getAll());
    }

    @DeleteMapping(path = "/limit-expense/delete")
    public ResponseEntity<String> delete(@RequestBody EditLimitExpenseRequest request) {
        return limitExpenseService.delete(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
