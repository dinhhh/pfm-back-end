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
public class RecurringTransaction {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String recurringTransactionNo;

    @Column(nullable = false)
    private String userNo;

    @Column(nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType;

    private String description;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "boolean default false")
    private boolean executed = false;

    private String walletNo;

}
