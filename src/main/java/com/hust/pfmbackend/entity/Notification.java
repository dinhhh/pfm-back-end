package com.hust.pfmbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Notification {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String notificationNo;

    @Column(nullable = false)
    private String userNo;

    private String message;

    private LocalDateTime createdOn;

    private boolean isRead = false;

}
