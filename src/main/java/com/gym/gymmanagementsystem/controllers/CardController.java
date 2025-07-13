package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.CardDto;
import com.gym.gymmanagementsystem.dto.mappers.CardMapper;
import com.gym.gymmanagementsystem.entities.Card;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler pro správu karet v systému správy gymu.
 *
 * @restController
 * @requestMapping("/api/cards")
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private final CardService cardService;

    @Autowired
    private CardMapper mapper;

    /**
     * Konstruktor pro injektování služby karet.
     *
     * @param cardService Služba pro správu karet.
     */
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Získá seznam všech karet.
     *
     * @return ResponseEntity obsahující seznam DTO karet.
     *
     * @getMapping("/")
     */
    @GetMapping
    public ResponseEntity<List<CardDto>> getAllCards() {
        log.info("GET /api/cards");
        List<Card> cards = cardService.getAllCards();
        List<CardDto> cardDtos = cards.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} karet", cardDtos.size());
        return ResponseEntity.ok(cardDtos);
    }

    /**
     * Získá kartu podle jejího ID.
     *
     * @param id ID karty.
     * @return ResponseEntity obsahující DTO karty.
     *
     * @getMapping("/{id}")
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCardById(@PathVariable Integer id) {
        log.info("GET /api/cards/{}", id);
        Card card = cardService.getCardById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Karta nenalezena s ID " + id));
        CardDto dto = mapper.toDto(card);
        log.debug("Karta {} nalezena", id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Vytvoří novou kartu.
     *
     * @param cardDto DTO obsahující informace o nové kartě.
     * @return ResponseEntity obsahující vytvořenou DTO karty.
     *
     * @postMapping("/")
     */
    @PostMapping
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CardDto cardDto) {
        log.info("POST /api/cards - {}", cardDto);
        Card card = mapper.toEntity(cardDto);
        Card createdCard = cardService.createCard(card);
        CardDto createdDto = mapper.toDto(createdCard);
        log.debug("Karta vytvořena s ID {}", createdCard.getCardID());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    /**
     * Aktualizuje informace o kartě podle jejího ID.
     *
     * @param id      ID karty, kterou chceme aktualizovat.
     * @param cardDto DTO obsahující aktualizované informace o kartě.
     * @return ResponseEntity obsahující aktualizované DTO karty.
     *
     * @putMapping("/{id}")
     */
    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(@PathVariable Integer id,
                                              @Valid @RequestBody CardDto cardDto) {
        log.info("PUT /api/cards/{} - {}", id, cardDto);
        Card cardDetails = mapper.toEntity(cardDto);
        Card updatedCard = cardService.updateCard(id, cardDetails);
        CardDto updatedDto = mapper.toDto(updatedCard);
        log.debug("Karta {} aktualizována", id);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Smaže kartu podle jejího ID.
     *
     * @param id ID karty, kterou chceme smazat.
     * @return ResponseEntity bez obsahu, indikující úspěšné smazání.
     *
     * @deleteMapping("/{id}")
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Integer id) {
        log.info("DELETE /api/cards/{}", id);
        cardService.deleteCard(id);
        log.debug("Karta {} smazána", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Získá kartu podle jejího čísla.
     *
     * @param cardNumber Číslo karty.
     * @return ResponseEntity obsahující DTO karty.
     *
     * @getMapping("/byNumber/{cardNumber}")
     */
    @GetMapping("/byNumber/{cardNumber}")
    public ResponseEntity<CardDto> getCardByNumber(@PathVariable Long cardNumber) {
        log.info("GET /api/cards/byNumber/{}", cardNumber);
        Card card = cardService.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Karta nenalezena s číslem " + cardNumber));
        CardDto dto = mapper.toDto(card);
        log.debug("Karta {} nalezena", cardNumber);
        return ResponseEntity.ok(dto);
    }
}
