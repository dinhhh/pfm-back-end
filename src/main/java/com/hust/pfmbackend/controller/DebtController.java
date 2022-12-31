package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.request.DebtRequest;
import com.hust.pfmbackend.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebtController {

    @Autowired
    private DebtService debtService;

    @PostMapping(path = "/debt/new")
    public ResponseEntity<String> save(@RequestBody DebtRequest request) {
        return debtService.save(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
