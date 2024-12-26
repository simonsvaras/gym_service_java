package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "UserSubscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userSubscriptionID;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "SubscriptionID", nullable = false)
    private Integer subscriptionID;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isActive = false;

    // Vztah s TransactionHistory
    @OneToMany(mappedBy = "userSubscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionHistory> transactionHistories;

    // Vztah s User
    // Uživatelský vztah je nastaven v User entitě
}
