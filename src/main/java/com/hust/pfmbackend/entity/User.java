package com.hust.pfmbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
public class User {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String userNo;

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName;

    private String phone;

    private String address;

    @Column(nullable = false)
    private String password;

}
