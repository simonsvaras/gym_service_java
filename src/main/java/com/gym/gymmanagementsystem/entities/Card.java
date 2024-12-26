package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cardID;

    @Column(length = 50, unique = true, nullable = false)
    private String cardNumber;

    private Boolean lost;

    @Column(length = 50)
    private String cardType;

    // Vztah s uživatelem (jedna karta může patřit jednomu uživateli)
    @OneToOne(mappedBy = "card")
    private User user;
}
