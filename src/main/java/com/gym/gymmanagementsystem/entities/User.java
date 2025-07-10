package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userID;

    @Column(name="firstname",length = 50, nullable = false)
    private String firstname;

    @Column(name="lastname",length = 50, nullable = false)
    private String lastname;

    @Column(name="email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "points")
    private Integer points = 0;

    @Column(length = 200)
    private String profilePhoto;

    // Jedna karta na uživatele
    @OneToOne
    @JoinColumn(name = "ID_Card", unique = true)
    @ToString.Exclude
    private Card card;

    // Aktivní subscription
    @OneToOne
    @JoinColumn(name = "ID_Subscription", unique = true)
    @ToString.Exclude
    private UserSubscription activeSubscription;

    @Column(name="realuser")
    private Boolean realUser = true;

    @Column(name = "createdat", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Vztahy
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UserSubscription> userSubscriptions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UserOneTimeEntry> userOneTimeEntries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TransactionHistory> transactionHistories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<EntryHistory> entryHistories;
}
