package com.estimator.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionID;

    @NotNull
    @Column(nullable = false, unique = true)
    private String subscriptionName;

    private String description;

    @NotNull
    @Column(nullable = false)
    private BigDecimal price;

    public Subscription(String subscriptionName) {
        this.subscriptionName= subscriptionName;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionID=" + subscriptionID +
                ", subscriptionName='" + subscriptionName + '\'' +
                '}';
    }

}