package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "UserOneTimeEntries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOneTimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userOneTimeEntryID;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "OneTimeEntryID", nullable = false)
    private OneTimeEntry oneTimeEntry;

    @Column(name = "purchasedate")
    private LocalDate purchaseDate;

    @Column(name = "isused")
    private Boolean isUsed = false;

    // Vztah s TransactionHistory
    @OneToMany(mappedBy = "oneTimeEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionHistory> transactionHistories;
}
