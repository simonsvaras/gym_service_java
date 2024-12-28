package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.UserSubscription;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionService {
    List<UserSubscription> getAllUserSubscriptions();
    Optional<UserSubscription> getUserSubscriptionById(Integer id);
    UserSubscription createUserSubscription(UserSubscription userSubscription);
    UserSubscription updateUserSubscription(Integer id, UserSubscription userSubscriptionDetails);
    void deleteUserSubscription(Integer id);
    List<UserSubscription> findByUserId(Integer userId);
    List<UserSubscription> findActiveSubscriptions();
}
