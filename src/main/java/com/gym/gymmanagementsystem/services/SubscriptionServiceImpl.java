package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.Subscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Optional<Subscription> getSubscriptionById(Integer id) {
        return subscriptionRepository.findById(id);
    }

    @Override
    public Subscription createSubscription(Subscription subscription) {
        // Případná validace nebo další logika
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription updateSubscription(Integer id, Subscription subscriptionDetails) {
        return subscriptionRepository.findById(id)
                .map(subscription -> {
                    subscription.setSubscriptionType(subscriptionDetails.getSubscriptionType());
                    subscription.setDurationMonths(subscriptionDetails.getDurationMonths());
                    subscription.setPrice(subscriptionDetails.getPrice());
                    // Aktualizujte další pole podle potřeby
                    return subscriptionRepository.save(subscription);
                }).orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
    }

    @Override
    public void deleteSubscription(Integer id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
        subscriptionRepository.delete(subscription);
    }
}
