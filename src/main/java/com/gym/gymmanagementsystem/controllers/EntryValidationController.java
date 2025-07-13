package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.entities.Card;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.CardService;
import com.gym.gymmanagementsystem.services.EntryValidationResult;
import com.gym.gymmanagementsystem.services.EntryValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler pro ověření vstupu uživatele.
 */
@RestController
@RequestMapping("/api/entry-validation")
public class EntryValidationController {

    private final EntryValidationService entryValidationService;
    private final CardService cardService;

    @Autowired
    public EntryValidationController(EntryValidationService entryValidationService,
                                      CardService cardService) {
        this.entryValidationService = entryValidationService;
        this.cardService = cardService;
    }

    /**
     * Ověří vstup na základě ID uživatele.
     */
    @GetMapping("/validateEntryByUserID/{userId}")
    public ResponseEntity<EntryValidationResult> validateByUserId(@PathVariable Integer userId) {
        // pouze zjistí výsledek; případné 404 se řeší v service
        EntryValidationResult result = entryValidationService.canUserEnter(userId);
        if (!result.isAllowed()) {
            return ResponseEntity.status(403).body(result);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Ověří vstup na základě čísla karty.
     */
    @GetMapping("/validateEntryByCardNumber/{cardNumber}")
    public ResponseEntity<EntryValidationResult> validateByCard(@PathVariable Long cardNumber) {
        Card card = cardService.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with number " + cardNumber));
        User user = card.getUser();
        if (user == null) {
            throw new ResourceNotFoundException("Card " + cardNumber + " is not assigned to any user");
        }
        EntryValidationResult result = entryValidationService.canUserEnter(user.getUserID());
        if (!result.isAllowed()) {
            return ResponseEntity.status(403).body(result);
        }
        return ResponseEntity.ok(result);
    }
}

