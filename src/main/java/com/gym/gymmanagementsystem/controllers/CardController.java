package com.gym.gymmanagementsystem.controllers;


import com.gym.gymmanagementsystem.entities.Card;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // GET /api/cards
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        List<Card> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }

    // GET /api/cards/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Integer id) {
        Card card = cardService.getCardById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id " + id));
        return ResponseEntity.ok(card);
    }

    // POST /api/cards
    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card createdCard = cardService.createCard(card);
        return ResponseEntity.ok(createdCard);
    }

    // PUT /api/cards/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Integer id,
                                           @RequestBody Card cardDetails) {
        Card updatedCard = cardService.updateCard(id, cardDetails);
        return ResponseEntity.ok(updatedCard);
    }

    // DELETE /api/cards/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Integer id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    // Příklad speciálního endpointu: GET /api/cards/byNumber/{cardNumber}
    @GetMapping("/byNumber/{cardNumber}")
    public ResponseEntity<Card> getCardByNumber(@PathVariable String cardNumber) {
        Card card = cardService.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with number " + cardNumber));
        return ResponseEntity.ok(card);
    }
}
