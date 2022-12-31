package com.hust.pfmbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Debt {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String debtNo;

    @Column(nullable = false)
    private String userNo;

    private long amount;

    private Date createdOn;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    private Date expectCollectDate;

    private Date actualCollectDate;

    @Column(nullable = false)
    private String userDebtInfoNo;

}
