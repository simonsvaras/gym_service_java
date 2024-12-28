package com.gym.gymmanagementsystem.services;


import com.gym.gymmanagementsystem.entities.OneTimeEntry;

import java.util.List;
import java.util.Optional;

public interface OneTimeEntryService {
    List<OneTimeEntry> getAllOneTimeEntries();
    Optional<OneTimeEntry> getOneTimeEntryById(Integer id);
    OneTimeEntry createOneTimeEntry(OneTimeEntry oneTimeEntry);
    OneTimeEntry updateOneTimeEntry(Integer id, OneTimeEntry oneTimeEntryDetails);
    void deleteOneTimeEntry(Integer id);
}
