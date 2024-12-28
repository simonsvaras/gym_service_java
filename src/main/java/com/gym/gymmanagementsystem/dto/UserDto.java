package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private Integer userID;

    @NotBlank(message = "Firstname je povinné")
    @Size(max = 50, message = "Firstname může mít maximálně 50 znaků")
    private String firstname;

    @NotBlank(message = "Lastname je povinné")
    @Size(max = 50, message = "Lastname může mít maximálně 50 znaků")
    private String lastname;

    @NotBlank(message = "Email je povinný")
    @Email(message = "Neplatný formát emailu")
    private String email;

    @NotBlank(message = "Password je povinný")
    @Size(min = 6, message = "Password musí mít alespoň 6 znaků")
    private String password;

    private LocalDate birthdate;

    @Size(max = 200, message = "Profile photo může mít maximálně 200 znaků")
    private String profilePhoto;

    private Boolean realUser = true;

    // Vztahy mohou být reprezentovány pomocí ID nebo dalších DTO
    private Integer cardID;
    private Integer activeSubscriptionID;

    // Můžeš přidat další pole nebo vztahy podle potřeby
}
