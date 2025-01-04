package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.UserDto;
import com.gym.gymmanagementsystem.dto.mappers.UserMapper;
import com.gym.gymmanagementsystem.entities.User;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private UserMapper mapper;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        UserDto dto = mapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

    // POST /api/users
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        System.out.println("Received UserDto: " + userDto);

        User user = mapper.toEntity(userDto);
        User createdUser = userService.createUser(user);
        UserDto createdDto = mapper.toDto(createdUser);
        return ResponseEntity.ok(createdDto);
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id,
                                              @Valid @RequestBody UserDto userDto) {
        User userDetails = mapper.toEntity(userDto);
        User updatedUser = userService.updateUser(id, userDetails);
        UserDto updatedDto = mapper.toDto(updatedUser);
        return ResponseEntity.ok(updatedDto);
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Příklad speciálního endpointu: GET /api/users/byEmail/{email}
    @GetMapping("/byEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
        UserDto dto = mapper.toDto(user);
        return ResponseEntity.ok(dto);
    }
}
