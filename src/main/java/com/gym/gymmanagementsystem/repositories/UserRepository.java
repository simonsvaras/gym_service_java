package com.gym.gymmanagementsystem.repositories;


import com.gym.gymmanagementsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    /**
     * Najde uživatele, jejichž jméno nebo příjmení obsahuje daný výraz (case-insensitive).
     *
     * @param firstname Hledaný výraz v jméně.
     * @param lastname Hledaný výraz v příjmení.
     * @return Seznam uživatelů odpovídajících kritériím.
     */
    List<User> findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(String firstname, String lastname);

}
