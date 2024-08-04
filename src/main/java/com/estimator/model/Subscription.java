package com.estimator.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

// Model for Subscriptions
@Data
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscriptionID;

    @NotNull
    @Column(nullable = false, unique = true)
    private String subscriptionName;

    private String description;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;
}