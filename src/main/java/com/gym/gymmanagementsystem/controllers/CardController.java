package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.CardDto;
import com.gym.gymmanagementsystem.dto.mappers.CardMapper;
import com.gym.gymmanagementsystem.entities.Card;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final CardMapper mapper = CardMapper.INSTANCE;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // GET /api/cards
    @GetMapping
    public ResponseEntity<List<CardDto>> getAllCards() {
        List<Card> cards = cardService.getAllCards();
        List<CardDto> cardDtos = cards.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cardDtos);
    }

    // GET /api/cards/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCardById(@PathVariable Integer id) {
        Card card = cardService.getCardById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id " + id));
        CardDto dto = mapper.toDto(card);
        return ResponseEntity.ok(dto);
    }

    // POST /api/cards
    @PostMapping
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CardDto cardDto) {
        Card card = mapper.toEntity(cardDto);
        Card createdCard = cardService.createCard(card);
        CardDto createdDto = mapper.toDto(createdCard);
        return ResponseEntity.ok(createdDto);
    }

    // PUT /api/cards/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(@PathVariable Integer id,
                                              @Valid @RequestBody CardDto cardDto) {
        Card cardDetails = mapper.toEntity(cardDto);
        Card updatedCard = cardService.updateCard(id, cardDetails);
        CardDto updatedDto = mapper.toDto(updatedCard);
        return ResponseEntity.ok(updatedDto);
    }

    // DELETE /api/cards/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Integer id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    // Příklad speciálního endpointu: GET /api/cards/byNumber/{cardNumber}
    @GetMapping("/byNumber/{cardNumber}")
    public ResponseEntity<CardDto> getCardByNumber(@PathVariable String cardNumber) {
        Card card = cardService.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with number " + cardNumber));
        CardDto dto = mapper.toDto(card);
        return ResponseEntity.ok(dto);
    }
}
