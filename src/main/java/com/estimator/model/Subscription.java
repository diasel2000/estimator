package com.estimator.model;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionID;

    @Column(nullable = false, unique = true)
    private String subscriptionName;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;
}
