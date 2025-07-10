package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.FileResourceData;
import com.gym.gymmanagementsystem.dto.*;
import com.gym.gymmanagementsystem.dto.AssignCardRequest;
import com.gym.gymmanagementsystem.dto.mappers.EntryHistoryMapper;
import com.gym.gymmanagementsystem.dto.mappers.SubscriptionMapper;
import com.gym.gymmanagementsystem.dto.mappers.UserMapper;
import com.gym.gymmanagementsystem.dto.mappers.UserSubscriptionMapper;
import com.gym.gymmanagementsystem.entities.EntryHistory;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.entities.UserSubscription;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.EntryHistoryService;
import com.gym.gymmanagementsystem.services.UserService;
import com.gym.gymmanagementsystem.services.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Kontroler pro správu uživatelů v systému správy gymu.
 *
 * @restController
 * @requestMapping("/api/users")
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    private UserMapper mapper;
    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;
    @Autowired
    private EntryHistoryMapper entryHistoryMapper;
    @Autowired
    private UserSubscriptionService userSubscriptionService;
    @Autowired
    private EntryHistoryService entryHistoryService;



    /**
     * Konstruktor pro injektování služby uživatele.
     *
     * @param userService Služba pro správu uživatelů.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Získá seznam všech uživatelů nebo filtrovaných uživatelů podle searchTerm.
     *
     * @param searchTerm Volitelný parametr pro filtrování uživatelů podle jména nebo příjmení.
     * @return ResponseEntity obsahující seznam uživatelských DTO.
     *
     * @getMapping("/")
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(required = false) String searchTerm) {
        log.info("GET /api/users searchTerm={}", searchTerm);
        List<User> users;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            users = userService.searchUsers(searchTerm);
        } else {
            users = userService.getAllUsers();
        }
        List<UserDto> userDtos = users.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        log.debug("Vráceno {} uživatelů", userDtos.size());
        return ResponseEntity.ok(userDtos);
    }

    /**
     * Získá uživatele podle jeho ID.
     *
     * @param id ID uživatele.
     * @return ResponseEntity obsahující uživatelské DTO.
     *
     * @getMapping("/{id}")
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        log.info("GET /api/users/{}", id);
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Uživatel nenalezen s ID " + id));
        UserDto dto = mapper.toDto(user);
        log.debug("Uživatel {} nalezen", id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Vytvoří nového uživatele.
     *
     * @param userDto DTO obsahující informace o novém uživateli.
     * @return ResponseEntity obsahující ID vytvořeného uživatele a zprávu.
     *
     * @postMapping("/")
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST /api/users - {}", userDto);

        User user = mapper.toEntity(userDto);
        User createdUser = userService.createUser(user);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", createdUser.getUserID());
        responseMap.put("message", "Uživatel vytvořen");

        log.debug("Uživatel vytvořen s ID {}", createdUser.getUserID());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
    }

    /**
     * Aktualizuje informace o uživateli podle jeho ID.
     *
     * @param id      ID uživatele, kterého chceme aktualizovat.
     * @param userDto DTO obsahující aktualizované informace o uživateli.
     * @return ResponseEntity obsahující aktualizované uživatelské DTO.
     *
     * @putMapping("/{id}")
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id,
                                              @Valid @RequestBody UserDto userDto) {
        log.info("PUT /api/users/{} - {}", id, userDto);
        User userDetails = mapper.toEntity(userDto);
        User updatedUser = userService.updateUser(id, userDetails);
        UserDto updatedDto = mapper.toDto(updatedUser);
        log.debug("Uživatel {} aktualizován", id);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Smaže uživatele podle jeho ID.
     *
     * @param id ID uživatele, kterého chceme smazat.
     * @return ResponseEntity bez obsahu, indikující úspěšné smazání.
     *
     * @deleteMapping("/{id}")
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        log.info("DELETE /api/users/{}", id);
        userService.deleteUser(id);
        log.debug("Uživatel {} smazán", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Získá uživatele podle jeho emailu.
     *
     * @param email Email uživatele.
     * @return ResponseEntity obsahující uživatelské DTO.
     *
     * @getMapping("/byEmail/{email}")
     */
    @GetMapping("/byEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        log.info("GET /api/users/byEmail/{}", email);
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Uživatel nenalezen s emailem " + email));
        UserDto dto = mapper.toDto(user);
        log.debug("Uživatel s emailem {} nalezen", email);
        return ResponseEntity.ok(dto);
    }


    /**
     * Získá stav karty podle jejího čísla.
     *
     * @param cardNumber číslo karty
     * @return HTTP 200 s CardResponse (status + případné userID)
     */
    @GetMapping("/byCardNumber/{cardNumber}")
    public ResponseEntity<CardResponse> getUserByCardNumber(@PathVariable Integer cardNumber) {
        CardResponse response = userService.findUserByCardNumber(cardNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * Nahraje profilovou fotku uživatele.
     *
     * @param id   ID uživatele, jehož fotku nahráváme.
     * @param file MultipartFile obsahující nahrávanou fotku.
     * @return ResponseEntity obsahující zprávu o úspěšném nahrání.
     *
     * @postMapping("/{id}/uploadProfilePicture")
     */
    @PostMapping("/{id}/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(
            @PathVariable Integer id,
            @RequestParam("profilePicture") MultipartFile file) {
        log.info("POST /api/users/{}/uploadProfilePicture - filename={}", id, file.getOriginalFilename());
        String uniqueFilename = userService.uploadProfilePicture(id, file);
        log.debug("Profilová fotka uložena jako {}", uniqueFilename);
        return ResponseEntity.ok("Profilová fotka nahrána. Soubor: " + uniqueFilename);
    }

    @GetMapping("/{id}/profilePhoto")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable Integer id) {
        log.info("GET /api/users/{}/profilePhoto", id);
        FileResourceData fileData = userService.loadProfilePicture(id);

        log.debug("Profilová fotka {} načtena", fileData.getResource().getFilename());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileData.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getResource().getFilename() + "\"")
                .body(fileData.getResource());
    }

    /**
     * Přiřadí kartu k uživateli.
     *
     * @param userId ID uživatele
     * @param request objekt obsahující číslo karty
     * @return ResponseEntity se zprávou o výsledku
     *
     * @postMapping("/{userId}/assignCard")
     */
    @PostMapping("/{userId}/assignCard")
    public ResponseEntity<String> assignCardToUser(
            @PathVariable Integer userId,
            @Valid @RequestBody AssignCardRequest request) {
        log.info("POST /api/users/{}/assignCard cardNumber={}", userId, request.getCardNumber());
        userService.assignCardToUser(userId, request.getCardNumber());
        log.debug("Karta {} přiřazena uživateli {}", request.getCardNumber(), userId);
        return ResponseEntity.ok("Karta přiřazena");
    }


    /**
     * Vrací detailní informace o uživatelích s volitelným filtrováním.
     * <p>
     * Možné volitelné parametry:
     * <ul>
     *   <li><b>entryStart</b> a <b>entryEnd</b>: časový interval, ve kterém musí mít alespoň jeden záznam vstupu</li>
     *   <li><b>minEntryCount</b>: minimální počet vstupů</li>
     *   <li><b>subscriptionStatus</b>: stav předplatného ("active", "inactive", "expiring")</li>
     * </ul>
     * </p>
     *
     * @param entryStart         počáteční datum a čas vstupu, nepovinné
     * @param entryEnd           konečné datum a čas vstupu, nepovinné
     * @param minEntryCount      minimální počet vstupů, nepovinné
     * @param subscriptionStatus stav předplatného, nepovinné
     * @return ResponseEntity obsahující seznam DetailedUserDto splňující daná kritéria
     */
    @GetMapping("/detailed")
    public ResponseEntity<List<DetailedUserDto>> getAllUsersDetailed(
            @RequestParam(value = "entryStart", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entryStart,
            @RequestParam(value = "entryEnd", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entryEnd,
            @RequestParam(value = "minEntryCount", required = false) Integer minEntryCount,
            @RequestParam(value = "subscriptionStatus", required = false) String subscriptionStatus) {
        log.info("GET /api/users/detailed entryStart={} entryEnd={} minEntryCount={} subscriptionStatus={}",
                entryStart, entryEnd, minEntryCount, subscriptionStatus);
        // Voláme novou metodu service, která zohlední předané filtry.
        List<User> users = userService.getFilteredUsers(entryStart, entryEnd, minEntryCount, subscriptionStatus);

        List<DetailedUserDto> detailedList = new ArrayList<>();
        for (User u : users) {
            DetailedUserDto dto = new DetailedUserDto();
            dto.setUserID(u.getUserID());
            dto.setFirstname(u.getFirstname());
            dto.setLastname(u.getLastname());
            dto.setEmail(u.getEmail());
            dto.setPoints(u.getPoints());

            // Pokud má uživatel fotku, sestavíme URL ke stažení
            if (u.getProfilePhoto() != null && !u.getProfilePhoto().isEmpty()) {
                dto.setProfilePhotoPath("/api/users/" + u.getUserID() + "/profilePhoto");
            }

            // Příklad: pokud máš v entitě collection subscription, mapuj je pomocí odpovídajícího mapperu
            List<UserSubscriptionDto> subscriptionDtos = u.getUserSubscriptions().stream()
                    .map(userSubscriptionMapper::toDto)
                    .toList();
            dto.setSubscriptions(subscriptionDtos);

            // Mapování historie vstupů (předpokládáme, že uživatel má kolekci entryHistories)
            List<EntryHistoryDto> entryHistoryDtos = u.getEntryHistories().stream()
                    .map(entryHistoryMapper::toDto)
                    .toList();
            dto.setEntryHistories(entryHistoryDtos);

            detailedList.add(dto);
        }
        log.debug("Vráceno {} detailních záznamů", detailedList.size());
        return ResponseEntity.ok(detailedList);
    }


}
