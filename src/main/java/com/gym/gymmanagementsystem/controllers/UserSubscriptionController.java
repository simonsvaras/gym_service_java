package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.UserSubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    public UserSubscriptionController(UserSubscriptionService userSubscriptionService) {
        this.userSubscriptionService = userSubscriptionService;
    }

    // GET /api/user-subscriptions
    @GetMapping
    public ResponseEntity<List<UserSubscription>> getAllUserSubscriptions() {
        List<UserSubscription> subscriptions = userSubscriptionService.getAllUserSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    // GET /api/user-subscriptions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserSubscription> getUserSubscriptionById(@PathVariable Integer id) {
        UserSubscription userSubscription = userSubscriptionService.getUserSubscriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found with id " + id));
        return ResponseEntity.ok(userSubscription);
    }

    // POST /api/user-subscriptions
    @PostMapping
    public ResponseEntity<UserSubscription> createUserSubscription(@RequestBody UserSubscription userSubscription) {
        UserSubscription created = userSubscriptionService.createUserSubscription(userSubscription);
        return ResponseEntity.ok(created);
    }

    // PUT /api/user-subscriptions/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserSubscription> updateUserSubscription(@PathVariable Integer id,
                                                                   @RequestBody UserSubscription userSubscriptionDetails) {
        UserSubscription updated = userSubscriptionService.updateUserSubscription(id, userSubscriptionDetails);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/user-subscriptions/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSubscription(@PathVariable Integer id) {
        userSubscriptionService.deleteUserSubscription(id);
        return ResponseEntity.noContent().build();
    }

    // Speciální příklad: GET /api/user-subscriptions/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSubscription>> findByUserId(@PathVariable Integer userId) {
        List<UserSubscription> subscriptions = userSubscriptionService.findByUserId(userId);
        return ResponseEntity.ok(subscriptions);
    }

    // Další speciální příklad: GET /api/user-subscriptions/active
    @GetMapping("/active")
    public ResponseEntity<List<UserSubscription>> findActiveSubscriptions() {
        List<UserSubscription> activeSubs = userSubscriptionService.findActiveSubscriptions();
        return ResponseEntity.ok(activeSubs);
    }
}
