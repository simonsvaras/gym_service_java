package com.gym.gymmanagementsystem.repositories;

import com.gym.gymmanagementsystem.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    // Můžeš přidat vlastní metody podle potřeby
}
