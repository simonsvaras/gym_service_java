package com.gym.gymmanagementsystem.services;

import com.gym.gymmanagementsystem.entities.OneTimeEntry;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.repositories.OneTimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OneTimeEntryServiceImpl implements OneTimeEntryService {

    private static final Logger log = LoggerFactory.getLogger(OneTimeEntryServiceImpl.class);

    @Autowired
    private OneTimeEntryRepository oneTimeEntryRepository;

    @Override
    public List<OneTimeEntry> getAllOneTimeEntries() {
        log.info("Načítám jednorázové vstupy");
        List<OneTimeEntry> list = oneTimeEntryRepository.findAll();
        log.debug("Nalezeno {} záznamů", list.size());
        return list;
    }

    @Override
    public Optional<OneTimeEntry> getOneTimeEntryById(Integer id) {
        log.info("Hledám jednorázový vstup id={}", id);
        return oneTimeEntryRepository.findById(id);
    }

    @Override
    public OneTimeEntry createOneTimeEntry(OneTimeEntry oneTimeEntry) {
        log.info("Vytvářím jednorázový vstup: {}", oneTimeEntry);
        OneTimeEntry saved = oneTimeEntryRepository.save(oneTimeEntry);
        log.debug("Jednorázový vstup uložen s ID {}", saved.getOneTimeEntryID());
        return saved;
    }

    @Override
    public OneTimeEntry updateOneTimeEntry(Integer id, OneTimeEntry oneTimeEntryDetails) {
        log.info("Aktualizuji jednorázový vstup id={}", id);
        return oneTimeEntryRepository.findById(id)
                .map(oneTimeEntry -> {
                    oneTimeEntry.setEntryName(oneTimeEntryDetails.getEntryName());
                    oneTimeEntry.setPrice(oneTimeEntryDetails.getPrice());
                    // Aktualizujte další pole podle potřeby
                    OneTimeEntry e = oneTimeEntryRepository.save(oneTimeEntry);
                    log.debug("Jednorázový vstup {} aktualizován", id);
                    return e;
                }).orElseThrow(() -> new ResourceNotFoundException("OneTimeEntry not found with id " + id));
    }

    @Override
    public void deleteOneTimeEntry(Integer id) {
        log.info("Mažu jednorázový vstup id={}", id);
        OneTimeEntry oneTimeEntry = oneTimeEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OneTimeEntry not found with id " + id));
        oneTimeEntryRepository.delete(oneTimeEntry);
        log.debug("Jednorázový vstup {} smazán", id);
    }
}
