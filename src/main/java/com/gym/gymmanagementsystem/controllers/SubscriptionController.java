package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.SubscriptionDto;
import com.gym.gymmanagementsystem.dto.mappers.SubscriptionMapper;
import com.gym.gymmanagementsystem.entities.Subscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.SubscriptionService;
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
 * Kontroler pro správu předplatných v systému správy gymu.
 *
 * @restController
 * @requestMapping("/api/subscriptions")
 */
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    private final SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionMapper mapper;

    /**
     * Konstruktor pro injektování služby předplatných.
     *
     * @param subscriptionService Služba pro správu předplatných.
     */
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Získá seznam všech předplatných.
     *
     * @return ResponseEntity obsahující seznam DTO předplatných.
     *
     * @getMapping("/")
     */
    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptions() {
        log.info("GET /api/subscriptions");
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        List<SubscriptionDto> subscriptionDtos = subscriptions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} předplatných", subscriptionDtos.size());
        return ResponseEntity.ok(subscriptionDtos);
    }

    /**
     * Získá předplatné podle jeho ID.
     *
     * @param id ID předplatného.
     * @return ResponseEntity obsahující DTO předplatného.
     *
     * @getMapping("/{id}")
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getSubscriptionById(@PathVariable Integer id) {
        log.info("GET /api/subscriptions/{}", id);
        Subscription subscription = subscriptionService.getSubscriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Předplatné nenalezeno s ID " + id));
        SubscriptionDto dto = mapper.toDto(subscription);
        log.debug("Předplatné {} nalezeno", id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Vytvoří nové předplatné.
     *
     * @param subscriptionDto DTO obsahující informace o novém předplatném.
     * @return ResponseEntity obsahující vytvořené DTO předplatného.
     *
     * @postMapping("/")
     */
    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(@Valid @RequestBody SubscriptionDto subscriptionDto) {
        log.info("POST /api/subscriptions - {}", subscriptionDto);
        Subscription subscription = mapper.toEntity(subscriptionDto);
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);
        SubscriptionDto createdDto = mapper.toDto(createdSubscription);
        log.debug("Předplatné vytvořeno s ID {}", createdSubscription.getSubscriptionID());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    /**
     * Aktualizuje informace o předplatném podle jeho ID.
     *
     * @param id              ID předplatného, které chceme aktualizovat.
     * @param subscriptionDto DTO obsahující aktualizované informace o předplatném.
     * @return ResponseEntity obsahující aktualizované DTO předplatného.
     *
     * @putMapping("/{id}")
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto> updateSubscription(@PathVariable Integer id,
                                                              @Valid @RequestBody SubscriptionDto subscriptionDto) {
        log.info("PUT /api/subscriptions/{} - {}", id, subscriptionDto);
        Subscription subscriptionDetails = mapper.toEntity(subscriptionDto);
        Subscription updatedSubscription = subscriptionService.updateSubscription(id, subscriptionDetails);
        SubscriptionDto updatedDto = mapper.toDto(updatedSubscription);
        log.debug("Předplatné {} aktualizováno", id);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Smaže předplatné podle jeho ID.
     *
     * @param id ID předplatného, které chceme smazat.
     * @return ResponseEntity bez obsahu, indikující úspěšné smazání.
     *
     * @deleteMapping("/{id}")
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Integer id) {
        log.info("DELETE /api/subscriptions/{}", id);
        subscriptionService.deleteSubscription(id);
        log.debug("Předplatné {} smazáno", id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
