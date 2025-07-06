package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.Subscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> getAllSubscriptions() {
        log.info("Načítám předplatné");
        List<Subscription> list = subscriptionRepository.findAll();
        log.debug("Nalezeno {} předplatných", list.size());
        return list;
    }

    @Override
    public Optional<Subscription> getSubscriptionById(Integer id) {
        log.info("Hledám předplatné id={}", id);
        return subscriptionRepository.findById(id);
    }

    @Override
    public Subscription createSubscription(Subscription subscription) {
        log.info("Vytvářím předplatné: {}", subscription);
        Subscription saved = subscriptionRepository.save(subscription);
        log.debug("Předplatné uloženo s ID {}", saved.getSubscriptionID());
        return saved;
    }

    @Override
    public Subscription updateSubscription(Integer id, Subscription subscriptionDetails) {
        log.info("Aktualizuji předplatné id={}", id);
        return subscriptionRepository.findById(id)
                .map(subscription -> {
                    subscription.setSubscriptionType(subscriptionDetails.getSubscriptionType());
                    subscription.setDurationMonths(subscriptionDetails.getDurationMonths());
                    subscription.setPrice(subscriptionDetails.getPrice());
                    // Aktualizujte další pole podle potřeby
                    Subscription s = subscriptionRepository.save(subscription);
                    log.debug("Předplatné {} aktualizováno", id);
                    return s;
                }).orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
    }

    @Override
    public void deleteSubscription(Integer id) {
        log.info("Mažu předplatné id={}", id);
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
        subscriptionRepository.delete(subscription);
        log.debug("Předplatné {} smazáno", id);
    }
}
