package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.FileResourceData;
import com.gym.gymmanagementsystem.dto.CardResponse;
import com.gym.gymmanagementsystem.entities.User;
import org.apache.catalina.webresources.FileResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import com.gym.gymmanagementsystem.dto.enums.PhotoQuality;

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
     * Načte profilovou fotku daného uživatele jako {@link org.springframework.core.io.Resource}.
     *
     * @param userId  ID uživatele
     * @param quality požadovaná kvalita fotografie (LOW, MEDIUM, HIGH)
     * @return FileResourceData (s Resource a contentType), nebo vyhodí výjimku, pokud fotka neexistuje
     */
    FileResourceData loadProfilePicture(Integer userId, PhotoQuality quality);


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

    /**
     * Assigns a card identified by its number to the given user.
     * <p>
     * If the card does not exist, it will be created. If the card already
     * exists and is assigned to another user, a {@link com.gym.gymmanagementsystem.exceptions.ResourceAlreadyExistsException}
     * is thrown.
     *
     * @param userId     ID of the user to whom the card should be assigned
     * @param cardNumber number of the card
     */
    void assignCardToUser(Integer userId, Long cardNumber);

    /**
     * Odstraní přiřazení karty od uživatele.
     *
     * @param userId ID uživatele
     */
    void unsignCard(Integer userId);

    /**
     * Vyhledá uživatele podle čísla karty.
     * <p>
     * Pokud karta s daným číslem neexistuje, vyhodí {@link com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException}.
     * </p>
     *
     * @param cardNumber číslo karty
     * @return Optional s nalezeným uživatelem nebo prázdné, pokud karta není přiřazena
     */
    CardResponse findUserByCardNumber(Long cardNumber);

}
