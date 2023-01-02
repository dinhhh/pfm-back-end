package com.hust.pfmbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "wallet")
    @ToString.Exclude
    private Collection<ExpenseIncome> history;

    @Column(nullable = false)
    private String name;

    private String description;

    private long balance = 0;
}
