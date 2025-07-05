package com.gym.gymmanagementsystem.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmployee;

    @Column(name = "username",length = 100, nullable = false)
    private String username;

    @Column(name = "firstname", length = 100, nullable = false)
    private String firstname;

    @Column(name = "lastname", length = 100, nullable = false)
    private String lastname;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "role", length = 15)
    private String role;

    @Column(name = "createdat", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
