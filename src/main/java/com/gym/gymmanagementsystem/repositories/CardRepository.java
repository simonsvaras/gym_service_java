package com.gym.gymmanagementsystem.repositories;

import com.gym.gymmanagementsystem.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Integer> {
    Optional<Card> findByCardNumber(Long cardNumber);
}
