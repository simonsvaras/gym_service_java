package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.OneTimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OneTimeEntryServiceImpl implements OneTimeEntryService {

    @Autowired
    private OneTimeEntryRepository oneTimeEntryRepository;

    @Override
    public List<OneTimeEntry> getAllOneTimeEntries() {
        return oneTimeEntryRepository.findAll();
    }

    @Override
    public Optional<OneTimeEntry> getOneTimeEntryById(Integer id) {
        return oneTimeEntryRepository.findById(id);
    }

    @Override
    public OneTimeEntry createOneTimeEntry(OneTimeEntry oneTimeEntry) {
        // Případná validace nebo další logika
        return oneTimeEntryRepository.save(oneTimeEntry);
    }

    @Override
    public OneTimeEntry updateOneTimeEntry(Integer id, OneTimeEntry oneTimeEntryDetails) {
        return oneTimeEntryRepository.findById(id)
                .map(oneTimeEntry -> {
                    oneTimeEntry.setEntryName(oneTimeEntryDetails.getEntryName());
                    oneTimeEntry.setPrice(oneTimeEntryDetails.getPrice());
                    // Aktualizujte další pole podle potřeby
                    return oneTimeEntryRepository.save(oneTimeEntry);
                }).orElseThrow(() -> new ResourceNotFoundException("OneTimeEntry not found with id " + id));
    }

    @Override
    public void deleteOneTimeEntry(Integer id) {
        OneTimeEntry oneTimeEntry = oneTimeEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OneTimeEntry not found with id " + id));
        oneTimeEntryRepository.delete(oneTimeEntry);
    }
}
