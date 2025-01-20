package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Value("${upload.profile-photos}")
    private String uploadDir;

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
        // Kontrola, zda už e-mail existuje
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new ResourceAlreadyExistsException("Uživatel s emailem " + user.getEmail() + " již existuje");
        }
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

    @Override
    public String uploadProfilePicture(Integer userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Soubor je prázdný.");
        }

        try {
            // 1) Zajistit existenci složky
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            // 2) Vygenerovat unikátní název souboru
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // 3) Složit výslednou cestu
            Path destinationPath = uploadPath.resolve(uniqueFilename);

            // 4) Uložit soubor na disk
            file.transferTo(destinationPath.toFile());

            // 5) Do DB (sloupec profilePhoto) uložit **relativní** cestu (nebo jen název souboru)
            //    - lze uložit i plnou absolutní cestu, ale většinou stačí relativní cesta + prefix
            //    - např.: /profile-photos/<uniqueFilename> pokud se následně servíruje
            //      přes konfigurovaný statický mapping
            user.setProfilePhoto(uniqueFilename);
            userRepository.save(user);

            return uniqueFilename; // nebo "profile-photos/" + uniqueFilename

        } catch (IOException e) {
            throw new RuntimeException("Chyba při ukládání souboru: " + e.getMessage(), e);
        }
    }
}
