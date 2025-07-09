package com.gym.gymmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactionhistory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    @ToString.Exclude
    private User user;

    @Column(name = "transactiondate", nullable = false, updatable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "purchasetype", length = 50)
    private String purchaseType; // 'Subscription' nebo 'OneTimeEntry'

    @ManyToOne
    @JoinColumn(name = "usersubscriptionID")
    @ToString.Exclude
    private UserSubscription userSubscription;

    @ManyToOne
    @JoinColumn(name = "onetimeentryid")
    @ToString.Exclude
    private UserOneTimeEntry oneTimeEntry;
}
