package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.request.DebtRequest;
import com.hust.pfmbackend.model.response.statistic.DebtResponse;
import com.hust.pfmbackend.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "/debt/get-all-borrow")
    public ResponseEntity<DebtResponse> getAllBorrow() {
        return ResponseEntity.ok(debtService.getAllBorrow());
    }

    @GetMapping(path = "/debt/get-all-lend")
    public ResponseEntity<DebtResponse> getAllIncome() {
        return ResponseEntity.ok(debtService.getAllLend());
    }

    @PutMapping(path = "/debt/delete-by-user/{userDebtInfoNo}")
    public ResponseEntity<String> deleteByUserDebtInfoNo(@PathVariable String userDebtInfoNo) {
        return debtService.deleteByUserDebtInfoNo(userDebtInfoNo) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
