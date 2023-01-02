package com.hust.pfmbackend.model.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NotificationResponse {

    private String notificationNo;
    private String message;
    private boolean isRead;
    private String timeAgo;

}
