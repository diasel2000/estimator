package com.estimator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SubscriptionDTO {
    private Long subscriptionID;
    private String subscriptionName;
    private String description;
    private BigDecimal price;
}