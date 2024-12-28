package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        // Případná validace nebo další logika
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstname(userDetails.getFirstname());
                    user.setLastname(userDetails.getLastname());
                    user.setEmail(userDetails.getEmail());
                    user.setPassword(userDetails.getPassword());
                    user.setBirthdate(userDetails.getBirthdate());
                    user.setProfilePhoto(userDetails.getProfilePhoto());
                    user.setRealUser(userDetails.getRealUser());
                    // Aktualizujte další pole podle potřeby
                    return userRepository.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
