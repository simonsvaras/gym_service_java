package com.gym.gymmanagementsystem.repositories;


import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneTimeEntryRepository extends JpaRepository<OneTimeEntry, Integer> {
    // Můžeš přidat vlastní metody podle potřeby
}
