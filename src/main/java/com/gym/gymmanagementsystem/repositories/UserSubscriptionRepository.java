package com.gym.gymmanagementsystem.repositories;


import com.gym.gymmanagementsystem.entities.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
    List<UserSubscription> findByUserUserID(Integer userID);
    List<UserSubscription> findByIsActiveTrue();
}
