package com.gym.gymmanagementsystem.controllers;


import com.gym.gymmanagementsystem.entities.Subscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // GET /api/subscriptions
    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    // GET /api/subscriptions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Integer id) {
        Subscription subscription = subscriptionService.getSubscriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
        return ResponseEntity.ok(subscription);
    }

    // POST /api/subscriptions
    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);
        return ResponseEntity.ok(createdSubscription);
    }

    // PUT /api/subscriptions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Integer id,
                                                           @RequestBody Subscription subscriptionDetails) {
        Subscription updatedSubscription = subscriptionService.updateSubscription(id, subscriptionDetails);
        return ResponseEntity.ok(updatedSubscription);
    }

    // DELETE /api/subscriptions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Integer id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
