package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.Card;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CardServiceImpl implements CardService {

    private static final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Card> getAllCards() {
        log.info("Načítám všechny karty");
        List<Card> list = cardRepository.findAll();
        log.debug("Nalezeno {} karet", list.size());
        return list;
    }

    @Override
    public Optional<Card> getCardById(Integer id) {
        log.info("Vyhledávám kartu id={}", id);
        return cardRepository.findById(id);
    }

    @Override
    public Card createCard(Card card) {
        log.info("Vytvářím kartu: {}", card);
        Card saved = cardRepository.save(card);
        log.debug("Karta vytvořena s ID {}", saved.getCardID());
        return saved;
    }

    @Override
    public Card updateCard(Integer id, Card cardDetails) {
        log.info("Aktualizuji kartu id={}", id);
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
        log.info("Mažu kartu id={}", id);
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id " + id));
        cardRepository.delete(card);
        log.debug("Karta {} smazána", id);
    }

    @Override
    public Optional<Card> findByCardNumber(Long cardNumber) {
        log.info("Hledám kartu dle čísla {}", cardNumber);
        return cardRepository.findByCardNumber(cardNumber);
    }
}
