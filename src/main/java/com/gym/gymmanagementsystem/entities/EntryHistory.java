package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "EntryHistory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer entryID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "entrydate", nullable = false, updatable = false)
    private LocalDateTime entryDate = LocalDateTime.now();

    @Column(name = "entrytype", length = 50)
    private String entryType; // 'Subscription' nebo 'OneTimeEntry'
}
