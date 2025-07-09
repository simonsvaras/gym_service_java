package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Onetimeentries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneTimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onetimeentryID")
    private Integer oneTimeEntryID;

    @Column(name = "entryname",length = 50)
    private String entryName;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    // Vztah s UserOneTimeEntry
    @OneToMany(mappedBy = "oneTimeEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<UserOneTimeEntry> userOneTimeEntries;
}
