package com.hust.pfmbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String walletNo;

    @ManyToOne
    @JoinColumn(name = "user_no")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "wallet")
    private Collection<ExpenseIncome> history;

    @Column(nullable = false)
    private String name;

    private String description;

    private long balance = 0;
}
