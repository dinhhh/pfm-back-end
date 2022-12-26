package com.hust.pfmbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ExpenseIncome {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String expenseIncomeNo;

    @ManyToOne
    @JoinColumn(name = "user_no", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    private long amount;

    private Date createOn;

    private String description;

    @ManyToOne
    @JoinColumn(name = "wallet_no")
    @JsonIgnore
    private Wallet wallet;

    private String categoryNo;

//    private String image;
}
