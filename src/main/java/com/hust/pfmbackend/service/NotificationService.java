package com.hust.pfmbackend.service;

import com.hust.pfmbackend.model.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getAll();

    boolean updateRead(String notificationNo);

}
