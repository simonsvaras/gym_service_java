package com.gym.gymmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TransactionHistory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(length = 255)
    private String description;

    @Column(length = 50)
    private String purchaseType; // 'Subscription' nebo 'OneTimeEntry'

    @ManyToOne
    @JoinColumn(name = "UserSubscriptionID")
    private UserSubscription userSubscription;

    @ManyToOne
    @JoinColumn(name = "OneTimeEntryID")
    private UserOneTimeEntry oneTimeEntry;
}
