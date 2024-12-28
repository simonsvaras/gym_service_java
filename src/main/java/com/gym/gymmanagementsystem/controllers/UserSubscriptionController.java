package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.UserSubscriptionDto;
import com.gym.gymmanagementsystem.dto.mappers.UserSubscriptionMapper;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.UserSubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;
    private final UserSubscriptionMapper mapper = UserSubscriptionMapper.INSTANCE;

    public UserSubscriptionController(UserSubscriptionService userSubscriptionService) {
        this.userSubscriptionService = userSubscriptionService;
    }

    // GET /api/user-subscriptions
    @GetMapping
    public ResponseEntity<List<UserSubscriptionDto>> getAllUserSubscriptions() {
        List<UserSubscription> subscriptions = userSubscriptionService.getAllUserSubscriptions();
        List<UserSubscriptionDto> subscriptionDtos = subscriptions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subscriptionDtos);
    }

    // GET /api/user-subscriptions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserSubscriptionDto> getUserSubscriptionById(@PathVariable Integer id) {
        UserSubscription userSubscription = userSubscriptionService.getUserSubscriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found with id " + id));
        UserSubscriptionDto dto = mapper.toDto(userSubscription);
        return ResponseEntity.ok(dto);
    }

    // POST /api/user-subscriptions
    @PostMapping
    public ResponseEntity<UserSubscriptionDto> createUserSubscription(@Valid @RequestBody UserSubscriptionDto userSubscriptionDto) {
        UserSubscription userSubscription = mapper.toEntity(userSubscriptionDto);
        UserSubscription created = userSubscriptionService.createUserSubscription(userSubscription);
        UserSubscriptionDto createdDto = mapper.toDto(created);
        return ResponseEntity.ok(createdDto);
    }

    // PUT /api/user-subscriptions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserSubscriptionDto> updateUserSubscription(@PathVariable Integer id,
                                                                      @Valid @RequestBody UserSubscriptionDto userSubscriptionDto) {
        UserSubscription userSubscriptionDetails = mapper.toEntity(userSubscriptionDto);
        UserSubscription updated = userSubscriptionService.updateUserSubscription(id, userSubscriptionDetails);
        UserSubscriptionDto updatedDto = mapper.toDto(updated);
        return ResponseEntity.ok(updatedDto);
    }

    // DELETE /api/user-subscriptions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable Integer id) {
        userSubscriptionService.deleteUserSubscription(id);
        return ResponseEntity.noContent().build();
    }

    // Speciální příklad: GET /api/user-subscriptions/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSubscriptionDto>> findByUserId(@PathVariable Integer userId) {
        List<UserSubscription> subscriptions = userSubscriptionService.findByUserId(userId);
        List<UserSubscriptionDto> subscriptionDtos = subscriptions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subscriptionDtos);
    }

    // Další speciální příklad: GET /api/user-subscriptions/active
    @GetMapping("/active")
    public ResponseEntity<List<UserSubscriptionDto>> findActiveSubscriptions() {
        List<UserSubscription> activeSubs = userSubscriptionService.findActiveSubscriptions();
        List<UserSubscriptionDto> subscriptionDtos = activeSubs.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subscriptionDtos);
    }
}
