package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Override
    public List<UserSubscription> getAllUserSubscriptions() {
        return userSubscriptionRepository.findAll();
    }

    @Override
    public Optional<UserSubscription> getUserSubscriptionById(Integer id) {
        return userSubscriptionRepository.findById(id);
    }

    @Override
    public UserSubscription createUserSubscription(UserSubscription userSubscription) {
        // Případná validace nebo další logika
        return userSubscriptionRepository.save(userSubscription);
    }

    @Override
    public UserSubscription updateUserSubscription(Integer id, UserSubscription userSubscriptionDetails) {
        return userSubscriptionRepository.findById(id)
                .map(userSubscription -> {
                    userSubscription.setUser(userSubscriptionDetails.getUser());
                    userSubscription.setSubscription(userSubscriptionDetails.getSubscription());
                    userSubscription.setStartDate(userSubscriptionDetails.getStartDate());
                    userSubscription.setEndDate(userSubscriptionDetails.getEndDate());
                    userSubscription.setIsActive(userSubscriptionDetails.getIsActive());
                    // Aktualizujte další pole podle potřeby
                    return userSubscriptionRepository.save(userSubscription);
                }).orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found with id " + id));
    }

    @Override
    public void deleteUserSubscription(Integer id) {
        UserSubscription userSubscription = userSubscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSubscription not found with id " + id));
        userSubscriptionRepository.delete(userSubscription);
    }

    @Override
    public List<UserSubscription> findByUserId(Integer userId) {
        return userSubscriptionRepository.findByUserUserID(userId);
    }

    @Override
    public List<UserSubscription> findActiveSubscriptions() {
        return userSubscriptionRepository.findByIsActiveTrue();
    }
}
