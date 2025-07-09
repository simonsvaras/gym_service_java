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

    @Column(name = "cardnumber",length = 50, unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "lost")
    private Boolean lost;

    @Column(name = "cardtype",length = 50)
    private String cardType;

    // Vztah s uživatelem (jedna karta může patřit jednomu uživateli)
    @OneToOne(mappedBy = "card")
    @ToString.Exclude
    private User user;
}
