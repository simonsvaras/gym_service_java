package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Integer id);
    User createUser(User user);
    User updateUser(Integer id, User userDetails);
    void deleteUser(Integer id);
    Optional<User> findByEmail(String email);
    public String uploadProfilePicture(Integer userId, MultipartFile file);
}
