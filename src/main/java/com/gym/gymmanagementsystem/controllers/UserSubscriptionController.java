package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.UserSubscriptionDto;
import com.gym.gymmanagementsystem.dto.mappers.UserSubscriptionMapper;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler pro správu předplatných uživatelů v systému správy gymu.
 *
 * @restController
 * @requestMapping("/api/user-subscriptions")
 */
@RestController
@RequestMapping("/api/user-subscriptions")
public class UserSubscriptionController {

    private static final Logger log = LoggerFactory.getLogger(UserSubscriptionController.class);

    private final UserSubscriptionService userSubscriptionService;

    @Autowired
    private UserSubscriptionMapper mapper;

    /**
     * Konstruktor pro injektování služby předplatných uživatelů.
     *
     * @param userSubscriptionService Služba pro správu předplatných uživatelů.
     */
    public UserSubscriptionController(UserSubscriptionService userSubscriptionService) {
        this.userSubscriptionService = userSubscriptionService;
    }

    /**
     * Získá seznam všech předplatných uživatelů.
     *
     * @return ResponseEntity obsahující seznam DTO předplatných uživatelů.
     *
     * @getMapping("/")
     */
    @GetMapping
    public ResponseEntity<List<UserSubscriptionDto>> getAllUserSubscriptions() {
        log.info("GET /api/user-subscriptions");
        List<UserSubscription> subscriptions = userSubscriptionService.getAllUserSubscriptions();
        List<UserSubscriptionDto> subscriptionDtos = subscriptions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} předplatných uživatelů", subscriptionDtos.size());
        return ResponseEntity.ok(subscriptionDtos);
    }

    /**
     * Získá předplatné uživatele podle jeho ID.
     *
     * @param id ID předplatného uživatele.
     * @return ResponseEntity obsahující DTO předplatného uživatele.
     *
     * @getMapping("/{id}")
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserSubscriptionDto> getUserSubscriptionById(@PathVariable Integer id) {
        log.info("GET /api/user-subscriptions/{}", id);
        UserSubscription userSubscription = userSubscriptionService.getUserSubscriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSubscription nenalezeno s ID " + id));
        UserSubscriptionDto dto = mapper.toDto(userSubscription);
        log.debug("Předplatné uživatele {} nalezeno", id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Vytvoří nové předplatné uživatele.
     *
     * @param dto DTO obsahující informace o novém předplatném uživatele.
     * @return ResponseEntity obsahující vytvořené DTO předplatného uživatele.
     *
     * @postMapping("/")
     */
    @PostMapping
    public ResponseEntity<UserSubscriptionDto> createUserSubscription(
            @Valid @RequestBody UserSubscriptionDto dto) {
        log.info("POST /api/user-subscriptions - {}", dto);

        // Podmíněná validace pro subscriptionID = 6
        if (dto.getSubscriptionID().equals(6)) {
            if (dto.getCustomEndDate() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Pole customEndDate je povinné pro subscriptionID = 6"
                );
            }
            if (dto.getCustomPrice() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Pole customPrice je povinné pro subscriptionID = 6"
                );
            }
        } else {
            if (dto.getCustomEndDate() != null || dto.getCustomPrice() != null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "customEndDate a customPrice lze zadat pouze pro subscriptionID = 6"
                );
            }
        }

        // Mapování a volání service
        UserSubscription reqEnt = mapper.toEntity(dto);
        UserSubscription created = userSubscriptionService.createUserSubscription(
                reqEnt,
                dto.getCustomEndDate(),
                dto.getCustomPrice()
        );

        UserSubscriptionDto out = mapper.toDto(created);
        log.debug("Předplatné uživatele vytvořeno s ID {}", out.getUserSubscriptionID());
        return ResponseEntity.status(HttpStatus.CREATED).body(out);
    }

    /**
     * Aktualizuje informace o předplatném uživatele podle jeho ID.
     *
     * @param id                  ID předplatného uživatele, které chceme aktualizovat.
     * @param userSubscriptionDto DTO obsahující aktualizované informace o předplatném uživatele.
     * @return ResponseEntity obsahující aktualizované DTO předplatného uživatele.
     *
     * @putMapping("/{id}")
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserSubscriptionDto> updateUserSubscription(@PathVariable Integer id,
                                                                      @Valid @RequestBody UserSubscriptionDto userSubscriptionDto) {
        log.info("PUT /api/user-subscriptions/{} - {}", id, userSubscriptionDto);
        UserSubscription userSubscriptionDetails = mapper.toEntity(userSubscriptionDto);
        UserSubscription updated = userSubscriptionService.updateUserSubscription(id, userSubscriptionDetails);
        UserSubscriptionDto updatedDto = mapper.toDto(updated);
        log.debug("Předplatné uživatele {} aktualizováno", id);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Smaže předplatné uživatele podle jeho ID.
     *
     * @param id ID předplatného uživatele, které chceme smazat.
     * @return ResponseEntity bez obsahu, indikující úspěšné smazání.
     *
     * @deleteMapping("/{id}")
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable Integer id) {
        log.info("DELETE /api/user-subscriptions/{}", id);
        userSubscriptionService.deleteUserSubscription(id);
        log.debug("Předplatné uživatele {} smazáno", id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * Získá seznam předplatných uživatelů podle ID uživatele.
     *
     * @param userId ID uživatele.
     * @return ResponseEntity obsahující seznam DTO předplatných uživatelů.
     *
     * @getMapping("/user/{userId}")
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSubscriptionDto>> findByUserId(@PathVariable Integer userId) {
        log.info("GET /api/user-subscriptions/user/{}", userId);
        List<UserSubscription> subscriptions = userSubscriptionService.findByUserId(userId);
        List<UserSubscriptionDto> subscriptionDtos = subscriptions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} předplatných", subscriptionDtos.size());
        return ResponseEntity.ok(subscriptionDtos);
    }

    /**
     * Získá seznam aktivních předplatných uživatelů.
     *
     * @return ResponseEntity obsahující seznam DTO aktivních předplatných uživatelů.
     *
     * @getMapping("/active")
     */
    @GetMapping("/active")
    public ResponseEntity<List<UserSubscriptionDto>> findActiveSubscriptions() {
        log.info("GET /api/user-subscriptions/active");
        List<UserSubscription> activeSubs = userSubscriptionService.findActiveSubscriptions();
        List<UserSubscriptionDto> subscriptionDtos = activeSubs.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} aktivních předplatných", subscriptionDtos.size());
        return ResponseEntity.ok(subscriptionDtos);
    }
}
