package com.hust.pfmbackend.controller;

import com.hust.pfmbackend.model.response.NotificationResponse;
import com.hust.pfmbackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping(path = "/notification/get-all")
    public ResponseEntity<List<NotificationResponse>> getAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }

    @PutMapping(path = "/notification/is-read/{notificationNo}")
    public ResponseEntity<String> updateRead(@PathVariable String notificationNo) {
        return notificationService.updateRead(notificationNo) ?
                ResponseEntity.ok("success") :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
