package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.request.NewWalletRequest;
import com.hust.pfmbackend.model.response.WalletResponse;
import com.hust.pfmbackend.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping(path = "/wallet/new")
    public ResponseEntity<String> save(@RequestBody NewWalletRequest request) {
        return walletService.save(request) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/wallet/get-all")
    public ResponseEntity<WalletResponse> getAll() {
        return ResponseEntity.ok(walletService.getAllByUser());
    }
}
