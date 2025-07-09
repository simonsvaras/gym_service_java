package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Useronetimeentries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOneTimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "useronetimeentryID")
    private Integer userOneTimeEntryID;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "onetimeentryID", nullable = false)
    @ToString.Exclude
    private OneTimeEntry oneTimeEntry;

    @Column(name = "purchasedate")
    private LocalDate purchaseDate;

    @Column(name = "isused")
    private Boolean isUsed = false;

    // Vztah s TransactionHistory
    @OneToMany(mappedBy = "oneTimeEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TransactionHistory> transactionHistories;
}
