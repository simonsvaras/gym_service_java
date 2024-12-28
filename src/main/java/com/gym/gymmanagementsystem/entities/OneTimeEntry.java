package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "OneTimeEntries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneTimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oneTimeEntryID;

    @Column(length = 50)
    private String entryName;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    // Vztah s UserOneTimeEntry
    @OneToMany(mappedBy = "oneTimeEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOneTimeEntry> userOneTimeEntries;
}
