package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Entryhistory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer entryID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    @ToString.Exclude
    private User user;

    @Column(name = "entrydate", nullable = false, updatable = false)
    private LocalDateTime entryDate = LocalDateTime.now();

    @Column(name = "entrytype", length = 50)
    private String entryType; // 'Subscription' nebo 'OneTimeEntry'
}
