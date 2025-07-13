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

    @Column(name = "cardnumber", unique = true, nullable = false)
    private Long cardNumber;

    @Column(name = "lost")
    private Boolean lost;

    @Column(name = "cardtype",length = 50)
    private String cardType;

    // Vztah s uživatelem (jedna karta může patřit jednomu uživateli)
    @OneToOne(mappedBy = "card")
    @ToString.Exclude
    private User user;
}
