package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.FileResourceData;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
            user.setProfilePhoto(uniqueFilename);
            userRepository.save(user);

            return uniqueFilename; // nebo "profile-photos/" + uniqueFilename

        } catch (IOException e) {
            throw new RuntimeException("Chyba při ukládání souboru: " + e.getMessage(), e);
        }
    }

    /**
     * Najde uživatele podle zadaného výrazu v jméně nebo příjmení.
     *
     * @param searchTerm Hledaný výraz.
     * @return Seznam uživatelů odpovídajících kritériím.
     */
    @Override
    public List<User> searchUsers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllUsers();
        }
        return userRepository.findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(searchTerm, searchTerm);
    }

    @Override
    public FileResourceData loadProfilePicture(Integer userId) {
        // 1) Najdeme uživatele
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        // 2) Zjistíme, jestli user má v DB nějakou fotku
        String photoFilename = user.getProfilePhoto();
        if (photoFilename == null || photoFilename.isEmpty()) {
            throw new ResourceNotFoundException("User " + userId + " has no profile photo set.");
        }

        // 3) Složíme fyzickou cestu k souboru
        Path filePath = Paths.get(uploadDir).resolve(photoFilename).normalize();
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("Photo file not found: " + filePath);
        }

        try {
            // 4) Vytvoříme Resource
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File can't be read as Resource: " + filePath);
            }

            // 5) Detekujeme content-type (pokud chceš)
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return new FileResourceData(resource, contentType);

        } catch (IOException e) {
            throw new ResourceNotFoundException("Error reading file: " + e.getMessage());
        }
    }
}
