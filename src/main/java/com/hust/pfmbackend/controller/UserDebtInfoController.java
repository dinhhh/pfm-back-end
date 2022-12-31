package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.request.UserDebtInfoRequest;
import com.hust.pfmbackend.model.response.UserDebtInfoResponse;
import com.hust.pfmbackend.service.UserDebtInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserDebtInfoController {

    @Autowired
    private UserDebtInfoService service;

    @PostMapping(path = "/user-debt-info/new")
    private ResponseEntity<String> save(@RequestBody UserDebtInfoRequest request) {
        return service.save(request) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/user-debt-info/get-all")
    private ResponseEntity<List<UserDebtInfoResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

}
