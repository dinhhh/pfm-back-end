package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.request.EditRecurringTransactionRequest;
import com.hust.pfmbackend.model.request.NewRecurringTransactionRequest;
import com.hust.pfmbackend.model.response.RecurringTransactionResponse;
import com.hust.pfmbackend.service.RecurringTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecurringTransactionController {

    @Autowired
    private RecurringTransactionService transactionService;

    @PostMapping(path = "/recurring-transaction/new")
    public ResponseEntity<String> save(@RequestBody NewRecurringTransactionRequest request) {
        return transactionService.save(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/recurring-transaction/get-all")
    public ResponseEntity<List<RecurringTransactionResponse>> getAll() {
        return new ResponseEntity<>(transactionService.getAll(), HttpStatus.OK);
    }

    @PutMapping(path = "/recurring-transaction/update")
    public ResponseEntity<String> update(@RequestBody EditRecurringTransactionRequest request) {
        return transactionService.update(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(path = "/recurring-transaction/delete")
    public ResponseEntity<String> delete(@RequestBody EditRecurringTransactionRequest request) {
        return transactionService.delete(request.getRecurringTransactionNo()) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
