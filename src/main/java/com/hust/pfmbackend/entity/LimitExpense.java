package com.hust.pfmbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LimitExpense {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String limitExpenseNo;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String userNo;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private long remainingAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Column(nullable = false)
    private String walletNo;

    private String categoryNo;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

}
