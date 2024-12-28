package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscriptionID;

    @Column(length = 50, nullable = false)
    private String subscriptionType;

    @Column(nullable = false)
    private Integer durationMonths;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Vztah s UserSubscription
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSubscription> userSubscriptions;
}
