package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    List<Subscription> getAllSubscriptions();
    Optional<Subscription> getSubscriptionById(Integer id);
    Subscription createSubscription(Subscription subscription);
    Subscription updateSubscription(Integer id, Subscription subscriptionDetails);
    void deleteSubscription(Integer id);
}
