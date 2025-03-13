package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.FileResourceData;
import com.gym.gymmanagementsystem.entities.User;
import org.apache.catalina.webresources.FileResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
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

    List<User> searchUsers(String searchTerm);
    /**
     * Načte profilovou fotku daného uživatele jako Resource.
     *
     * @param userId ID uživatele
     * @return FileResourceData (s Resource a contentType), nebo vyhodí výjimku, pokud fotka neexistuje
     */
    FileResourceData loadProfilePicture(Integer userId);


    /**
     * Vrací seznam uživatelů, kteří mají alespoň jeden záznam ve své kolekci entryHistories
     * s datumem vstupu v rámci zadaného časového intervalu.
     *
     * @param start počáteční datum a čas intervalu (včetně)
     * @param end   konečné datum a čas intervalu (včetně)
     * @return seznam uživatelů odpovídajících kritériím
     */
    List<User> getUsersWithEntryHistoryInRange(LocalDateTime start, LocalDateTime end);

    List<User> getFilteredUsers(LocalDateTime entryStart, LocalDateTime entryEnd, Integer minEntryCount, String subscriptionStatus);

}
