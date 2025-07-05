package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.FileResourceData;
import com.gym.gymmanagementsystem.dto.*;
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
        List<User> users;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            users = userService.searchUsers(searchTerm);
        } else {
            users = userService.getAllUsers();
        }
        List<UserDto> userDtos = users.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
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
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Uživatel nenalezen s ID " + id));
        UserDto dto = mapper.toDto(user);
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
        // Logování přijatého UserDto místo System.out.println
        System.out.println("Přijatý UserDto: " + userDto);

        User user = mapper.toEntity(userDto);
        User createdUser = userService.createUser(user);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", createdUser.getUserID());
        responseMap.put("message", "Uživatel vytvořen");

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
        User userDetails = mapper.toEntity(userDto);
        User updatedUser = userService.updateUser(id, userDetails);
        UserDto updatedDto = mapper.toDto(updatedUser);
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
        userService.deleteUser(id);
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
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Uživatel nenalezen s emailem " + email));
        UserDto dto = mapper.toDto(user);
        return ResponseEntity.ok(dto);
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

        String uniqueFilename = userService.uploadProfilePicture(id, file);
        return ResponseEntity.ok("Profilová fotka nahrána. Soubor: " + uniqueFilename);
    }

    @GetMapping("/{id}/profilePhoto")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable Integer id) {
        // Zavoláme servis
        FileResourceData fileData = userService.loadProfilePicture(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileData.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getResource().getFilename() + "\"")
                .body(fileData.getResource());
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

        // Voláme novou metodu service, která zohlední předané filtry.
        List<User> users = userService.getFilteredUsers(entryStart, entryEnd, minEntryCount, subscriptionStatus);

        List<DetailedUserDto> detailedList = new ArrayList<>();
        for (User u : users) {
            DetailedUserDto dto = new DetailedUserDto();
            dto.setUserID(u.getUserID());
            dto.setFirstname(u.getFirstname());
            dto.setLastname(u.getLastname());
            dto.setEmail(u.getEmail());

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
        return ResponseEntity.ok(detailedList);
    }


}
