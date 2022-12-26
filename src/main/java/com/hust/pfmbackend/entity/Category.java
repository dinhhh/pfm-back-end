package com.hust.pfmbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Category {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    private String categoryNo;

    @ManyToOne()
    @JoinColumn(name = "user_no")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String name;

    @CreatedDate
    @JsonIgnore
    private Date createOn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    private String description;

    private String parentCategoryNo; // parent category no = null -> it is parent

    // private String icon;
}
