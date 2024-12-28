package com.gym.gymmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeDto {

    private Integer idEmployee;

    @NotBlank(message = "Username je povinný")
    @Size(max = 100, message = "Username může mít maximálně 100 znaků")
    private String username;

    @NotBlank(message = "Firstname je povinné")
    @Size(max = 100, message = "Firstname může mít maximálně 100 znaků")
    private String firstname;

    @NotBlank(message = "Lastname je povinné")
    @Size(max = 100, message = "Lastname může mít maximálně 100 znaků")
    private String lastname;

    @NotBlank(message = "Password je povinné")
    @Size(min = 6, message = "Password musí mít alespoň 6 znaků")
    private String password;

    @Size(max = 15, message = "Role může mít maximálně 15 znaků")
    private String role;

    // Vytvoř si samostatné DTO pro odpovědi, kde heslo nebude zahrnuto
}
