package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Usersubscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscription {

    @Id
    @Column(name = "usersubscriptionid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userSubscriptionID;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "SubscriptionID", nullable = false)
    private Subscription subscription;

    @Column(name = "startdate")
    private LocalDate startDate;
    @Column(name = "enddate")
    private LocalDate endDate;

    @Column(name = "isactive")
    private Boolean isActive = false;

    // Vztah s TransactionHistory
    @OneToMany(mappedBy = "userSubscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionHistory> transactionHistories;

    // Vztah s User
    // Uživatelský vztah je nastaven v User entitě
}
