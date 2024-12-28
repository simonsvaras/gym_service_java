package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.SubscriptionDto;
import com.gym.gymmanagementsystem.dto.mappers.SubscriptionMapper;
import com.gym.gymmanagementsystem.entities.Subscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper mapper = SubscriptionMapper.INSTANCE;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // GET /api/subscriptions
    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        List<SubscriptionDto> subscriptionDtos = subscriptions.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(subscriptionDtos);
    }

    // GET /api/subscriptions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getSubscriptionById(@PathVariable Integer id) {
        Subscription subscription = subscriptionService.getSubscriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
        SubscriptionDto dto = mapper.toDto(subscription);
        return ResponseEntity.ok(dto);
    }

    // POST /api/subscriptions
    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(@Valid @RequestBody SubscriptionDto subscriptionDto) {
        Subscription subscription = mapper.toEntity(subscriptionDto);
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);
        SubscriptionDto createdDto = mapper.toDto(createdSubscription);
        return ResponseEntity.ok(createdDto);
    }

    // PUT /api/subscriptions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto> updateSubscription(@PathVariable Integer id,
                                                              @Valid @RequestBody SubscriptionDto subscriptionDto) {
        Subscription subscriptionDetails = mapper.toEntity(subscriptionDto);
        Subscription updatedSubscription = subscriptionService.updateSubscription(id, subscriptionDetails);
        SubscriptionDto updatedDto = mapper.toDto(updatedSubscription);
        return ResponseEntity.ok(updatedDto);
    }

    // DELETE /api/subscriptions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Integer id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
