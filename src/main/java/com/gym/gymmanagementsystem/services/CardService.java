package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.Card;

import java.util.List;
import java.util.Optional;

public interface CardService {
    List<Card> getAllCards();
    Optional<Card> getCardById(Integer id);
    Card createCard(Card card);
    Card updateCard(Integer id, Card cardDetails);
    void deleteCard(Integer id);
    Optional<Card> findByCardNumber(String cardNumber);
}
