package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.FileResourceData;
import com.gym.gymmanagementsystem.dto.CardResponse;
import com.gym.gymmanagementsystem.dto.enums.CardStatus;
import com.gym.gymmanagementsystem.entities.Card;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.exceptions.ResourceAlreadyExistsException;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.UserRepository;
import com.gym.gymmanagementsystem.repositories.CardRepository;
import com.gym.gymmanagementsystem.services.specifications.UserSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import com.gym.gymmanagementsystem.dto.enums.PhotoQuality;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Value("${upload.profile-photos}")
    private String uploadDir;

    private static final long MAX_IMAGE_SIZE_BYTES = 500 * 1024; // 500KB

    private static final int SIZE_LOW = 100;
    private static final int SIZE_MEDIUM = 400;
    private static final int SIZE_HIGH = 800;

    /**
     * Načte obrázek z bytového pole a aplikuje případnou rotaci podle EXIF
     * tagu Orientation. Pokud EXIF nelze přečíst, vrátí se původní obrázek.
     */
    private BufferedImage loadAndCorrectOrientation(byte[] bytes) throws IOException {
        int orientation = 1;
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
            ExifIFD0Directory dir = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (dir != null && dir.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                orientation = dir.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (Exception ex) {
            log.debug("Nepodařilo se načíst EXIF data: {}", ex.getMessage());
        }

        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
        if (img == null) {
            throw new IllegalArgumentException("Neplatný obrazový soubor");
        }

        switch (orientation) {
            case 6:
                return Thumbnails.of(img).rotate(90).scale(1).asBufferedImage();
            case 3:
                return Thumbnails.of(img).rotate(180).scale(1).asBufferedImage();
            case 8:
                return Thumbnails.of(img).rotate(270).scale(1).asBufferedImage();
            default:
                return img;
        }
    }

    private void saveVariant(BufferedImage src, Path path, int size, double quality) throws IOException {
        try (OutputStream out = Files.newOutputStream(path)) {
            Thumbnails.of(src)
                    .size(size, size)
                    .outputFormat("jpg")
                    .outputQuality(quality)
                    .toOutputStream(out);
        }
    }

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
        log.info("Vytvářím uživatele: {}", user);
        // Kontrola, zda už e-mail existuje
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new ResourceAlreadyExistsException("Uživatel s emailem " + user.getEmail() + " již existuje");
        }
        User saved = userRepository.save(user);
        log.debug("Uživatel uložen s ID {}", saved.getUserID());
        return saved;
    }

    @Override
    public User updateUser(Integer id, User userDetails) {
        log.info("Aktualizuji uživatele id={}", id);
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
                    User u = userRepository.save(user);
                    log.debug("Uživatel {} aktualizován", id);
                    return u;
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Integer id) {
        log.info("Mažu uživatele id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(user);
        log.debug("Uživatel {} smazán", id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String uploadProfilePicture(Integer userId, MultipartFile file) {
        log.info("Nahrávám profilovou fotku pro uživatele {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Soubor je prázdný.");
        }

        try {
            // 1) Zajistit existenci složky
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            // případné smazání staré fotky (všechny varianty)
            if (user.getProfilePhoto() != null && !user.getProfilePhoto().isEmpty()) {
                String oldName = user.getProfilePhoto();
                Path[] oldPaths = {
                        uploadPath.resolve("high_" + oldName),
                        uploadPath.resolve("medium_" + oldName),
                        uploadPath.resolve("low_" + oldName)
                };
                for (Path oldPath : oldPaths) {
                    log.debug("Mažu starou fotku {}", oldPath);
                    try {
                        Files.deleteIfExists(oldPath);
                    } catch (IOException ex) {
                        log.warn("Nepodařilo se smazat starou fotku {}: {}", oldPath, ex.getMessage());
                    }
                }
            }

            // 2) Vygenerovat unikátní název souboru (ukládáme vždy jako JPG)
            String uniqueFilename = UUID.randomUUID().toString() + ".jpg";

            // 3) Složit výsledné cesty pro jednotlivé varianty
            Path highPath = uploadPath.resolve("high_" + uniqueFilename);
            Path mediumPath = uploadPath.resolve("medium_" + uniqueFilename);
            Path lowPath = uploadPath.resolve("low_" + uniqueFilename);

            // 4) Načíst obrázek a případně jej otočit podle EXIF orientace
            byte[] fileBytes = file.getBytes();
            BufferedImage image = loadAndCorrectOrientation(fileBytes);

            // Ověření poměru stran (povolena odchylka 10 %)
            double ratio = (double) image.getWidth() / image.getHeight();
            if (Math.abs(ratio - 1.0) > 0.1) {
                throw new IllegalArgumentException("Profilová fotka musí mít poměr stran 1:1");
            }

            // 5) Vytvoření variant
            saveVariant(image, highPath, SIZE_HIGH, 0.9);
            saveVariant(image, mediumPath, SIZE_MEDIUM, 0.8);
            saveVariant(image, lowPath, SIZE_LOW, 0.6);

            // 6) Do DB (sloupec profilePhoto) uložit jen základní název
            user.setProfilePhoto(uniqueFilename);
            userRepository.save(user);

            log.info("Fotka uživatele {} uložena jako {} (vytvořeny varianty)", userId, uniqueFilename);
            return uniqueFilename; // klient si může připojit prefix varianty

        } catch (IOException e) {
            log.error("Chyba při ukládání fotky", e);
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
    public FileResourceData loadProfilePicture(Integer userId, PhotoQuality quality) {
        // 1) Najdeme uživatele
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        // 2) Zjistíme, jestli user má v DB nějakou fotku
        String photoFilename = user.getProfilePhoto();
        if (photoFilename == null || photoFilename.isEmpty()) {
            throw new ResourceNotFoundException("User " + userId + " has no profile photo set.");
        }

        // 3) Složíme fyzickou cestu k souboru dle zvolené kvality
        String prefix = (quality != null ? quality.prefix() : PhotoQuality.HIGH.prefix());
        Path filePath = Paths.get(uploadDir).resolve(prefix + photoFilename).normalize();
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

    @Override
    public List<User> getUsersWithEntryHistoryInRange(LocalDateTime start, LocalDateTime end) {
        Specification<User> spec = UserSpecifications.hasEntryHistoryInRange(start, end);
        return userRepository.findAll(spec);
    }


    /**
     * Vrací seznam uživatelů dle zadaných filtrů.
     *
     * @param entryStart         počáteční datum a čas pro filtrování záznamů vstupů, může být null
     * @param entryEnd           konečné datum a čas pro filtrování záznamů vstupů, může být null
     * @param minEntryCount      minimální počet vstupů, může být null
     * @param subscriptionStatus stav předplatného ("active", "inactive", "expiring"), může být null
     * @return seznam uživatelů splňující daná kritéria; pokud nejsou zadány žádné filtry, vrací všechny uživatele
     */
    @Override
    public List<User> getFilteredUsers(LocalDateTime entryStart, LocalDateTime entryEnd, Integer minEntryCount, String subscriptionStatus) {
        Specification<User> spec = Specification.where(null);

        // Filtrace podle data vstupu, pokud jsou oba parametry zadány
        if (entryStart != null && entryEnd != null) {
            spec = spec.and(UserSpecifications.hasEntryHistoryInRange(entryStart, entryEnd));
        }

        // Filtrace podle minimálního počtu vstupů
        if (minEntryCount != null) {
            spec = spec.and(UserSpecifications.hasMinimumEntryCount(minEntryCount));
        }

        // Filtrace podle stavu předplatného
        if (subscriptionStatus != null) {
            if ("active".equalsIgnoreCase(subscriptionStatus)) {
                spec = spec.and(UserSpecifications.hasActiveSubscription());
            } else if ("inactive".equalsIgnoreCase(subscriptionStatus)) {
                spec = spec.and(UserSpecifications.hasNoActiveSubscription());
            } else if ("expiring".equalsIgnoreCase(subscriptionStatus)) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime plus7Days = now.plusDays(7); // předpokládáme "končí brzy" = do 7 dnů
                spec = spec.and(UserSpecifications.hasExpiringSubscription(now, plus7Days));
            }
        }

        if (spec != null) {
            return userRepository.findAll(spec);
        } else {
            return userRepository.findAll();
        }
    }

    @Override
    public void assignCardToUser(Integer userId, String cardNumber) {
        log.info("Přiřazuji kartu {} uživateli {}", cardNumber, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        var optionalCard = cardRepository.findByCardNumber(cardNumber);
        var card = optionalCard.orElseGet(() -> {
            var newCard = new com.gym.gymmanagementsystem.entities.Card();
            newCard.setCardNumber(cardNumber);
            return cardRepository.save(newCard);
        });

        if (card.getUser() != null && !card.getUser().getUserID().equals(userId)) {
            log.warn("Karta {} již náleží jinému uživateli {}", cardNumber, card.getUser().getUserID());
            throw new ResourceAlreadyExistsException("Daná karta je již přiřazena jinému uživateli");
        }

        card.setUser(user);
        user.setCard(card);
        // save both sides
        cardRepository.save(card);
        userRepository.save(user);
        log.info("Karta {} úspěšně přiřazena uživateli {}", cardNumber, userId);
    }

    @Override
    public void unsignCard(Integer userId) {
        log.info("Odpájím kartu od uživatele {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        if (user.getCard() == null) {
            log.warn("Uživatel {} nemá přiřazenou kartu", userId);
            return;
        }

        var card = user.getCard();
        card.setUser(null);
        user.setCard(null);
        cardRepository.save(card);
        userRepository.save(user);
        log.info("Karta od uživatele {} úspěšně odebrána", userId);
    }

    /**
     * Najde stav karty a případně uživatele.
     *
     * @param cardNumber číslo karty
     * @return CardResponse s jedním ze stavů a userID, pokud je přiřazeno
     */
    public CardResponse findUserByCardNumber(Integer cardNumber) {
        // 1) Zjistíme, zda je karta v DB
        Optional<Card> cardOpt = cardRepository.findByCardNumber(cardNumber.toString());
        if (cardOpt.isEmpty()) {
            return new CardResponse(CardResponse.CardStatus.NOT_REGISTERED, null);
        }

        // 2) Karta existuje, zkusíme najít uživatele
        Integer cardId = cardOpt.get().getCardID();
        Optional<User> userOpt = userRepository.findByCardCardID(cardId);
        if (userOpt.isEmpty()) {
            return new CardResponse(CardResponse.CardStatus.UNASSIGNED, null);
        }

        // 3) Karta je přiřazena uživateli
        return new CardResponse(CardResponse.CardStatus.ASSIGNED, userOpt.get().getUserID());
    }

}
