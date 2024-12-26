package com.gym.gymmanagementsystem.repositories;

import com.gym.gymmanagementsystem.entities.UserOneTimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOneTimeEntryRepository extends JpaRepository<UserOneTimeEntry, Integer> {
    List<UserOneTimeEntry> findByUserUserID(Integer userID);
    List<UserOneTimeEntry> findByIsUsedFalse();
}
