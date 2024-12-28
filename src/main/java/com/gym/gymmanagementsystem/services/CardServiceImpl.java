package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.Card;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Optional<Card> getCardById(Integer id) {
        return cardRepository.findById(id);
    }

    @Override
    public Card createCard(Card card) {
        // Případná validace nebo další logika
        return cardRepository.save(card);
    }

    @Override
    public Card updateCard(Integer id, Card cardDetails) {
        return cardRepository.findById(id)
                .map(card -> {
                    card.setCardNumber(cardDetails.getCardNumber());
                    card.setLost(cardDetails.getLost());
                    card.setCardType(cardDetails.getCardType());
                    // Aktualizujte další pole podle potřeby
                    return cardRepository.save(card);
                }).orElseThrow(() -> new ResourceNotFoundException("Card not found with id " + id));
    }

    @Override
    public void deleteCard(Integer id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id " + id));
        cardRepository.delete(card);
    }

    @Override
    public Optional<Card> findByCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }
}
