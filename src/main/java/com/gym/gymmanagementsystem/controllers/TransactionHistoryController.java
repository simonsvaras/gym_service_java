package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.TransactionHistoryDto;
import com.gym.gymmanagementsystem.dto.mappers.TransactionHistoryMapper;
import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler pro správu historie transakcí v systému správy gymu.
 *
 * @restController
 * @requestMapping("/api/transaction-history")
 */
@RestController
@RequestMapping("/api/transaction-history")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    private TransactionHistoryMapper mapper;

    /**
     * Konstruktor pro injektování služby historie transakcí.
     *
     * @param transactionHistoryService Služba pro správu historie transakcí.
     */
    public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
        this.transactionHistoryService = transactionHistoryService;
    }

    /**
     * Získá seznam všech záznamů historie transakcí.
     *
     * @return ResponseEntity obsahující seznam DTO záznamů historie transakcí.
     *
     * @getMapping("/")
     */
    @GetMapping
    public ResponseEntity<List<TransactionHistoryDto>> getAllTransactionHistories() {
        List<TransactionHistory> histories = transactionHistoryService.getAllTransactionHistories();
        List<TransactionHistoryDto> historyDtos = histories.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(historyDtos);
    }

    /**
     * Získá záznam historie transakce podle jeho ID.
     *
     * @param id ID záznamu historie transakce.
     * @return ResponseEntity obsahující DTO záznamu historie transakce.
     *
     * @getMapping("/{id}")
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionHistoryDto> getTransactionHistoryById(@PathVariable Integer id) {
        TransactionHistory history = transactionHistoryService.getTransactionHistoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransactionHistory nenalezena s ID " + id));
        TransactionHistoryDto dto = mapper.toDto(history);
        return ResponseEntity.ok(dto);
    }

    /**
     * Vytvoří nový záznam historie transakce.
     *
     * @param transactionHistoryDto DTO obsahující informace o nové transakci.
     * @return ResponseEntity obsahující vytvořené DTO záznamu historie transakce.
     *
     * @postMapping("/")
     */
    @PostMapping
    public ResponseEntity<TransactionHistoryDto> createTransactionHistory(@Valid @RequestBody TransactionHistoryDto transactionHistoryDto) {
        TransactionHistory transactionHistory = mapper.toEntity(transactionHistoryDto);
        TransactionHistory created = transactionHistoryService.createTransactionHistory(transactionHistory);
        TransactionHistoryDto createdDto = mapper.toDto(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    /**
     * Aktualizuje informace o záznamu historie transakce podle jeho ID.
     *
     * @param id                   ID záznamu historie transakce, který chceme aktualizovat.
     * @param transactionHistoryDto DTO obsahující aktualizované informace o transakci.
     * @return ResponseEntity obsahující aktualizované DTO záznamu historie transakce.
     *
     * @putMapping("/{id}")
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionHistoryDto> updateTransactionHistory(@PathVariable Integer id,
                                                                          @Valid @RequestBody TransactionHistoryDto transactionHistoryDto) {
        TransactionHistory transactionHistoryDetails = mapper.toEntity(transactionHistoryDto);
        TransactionHistory updated = transactionHistoryService.updateTransactionHistory(id, transactionHistoryDetails);
        TransactionHistoryDto updatedDto = mapper.toDto(updated);
        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Smaže záznam historie transakce podle jeho ID.
     *
     * @param id ID záznamu historie transakce, který chceme smazat.
     * @return ResponseEntity bez obsahu, indikující úspěšné smazání.
     *
     * @deleteMapping("/{id}")
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionHistory(@PathVariable Integer id) {
        transactionHistoryService.deleteTransactionHistory(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * Získá seznam záznamů historie transakcí podle ID uživatele.
     *
     * @param userId ID uživatele.
     * @return ResponseEntity obsahující seznam DTO záznamů historie transakcí.
     *
     * @getMapping("/user/{userId}")
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionHistoryDto>> findByUserId(@PathVariable Integer userId) {
        List<TransactionHistory> results = transactionHistoryService.findByUserId(userId);
        List<TransactionHistoryDto> resultDtos = results.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultDtos);
    }

    /**
     * Získá seznam záznamů historie transakcí podle typu nákupu.
     *
     * @param purchaseType Typ nákupu.
     * @return ResponseEntity obsahující seznam DTO záznamů historie transakcí.
     *
     * @getMapping("/purchaseType/{purchaseType}")
     */
    @GetMapping("/purchaseType/{purchaseType}")
    public ResponseEntity<List<TransactionHistoryDto>> findByPurchaseType(@PathVariable String purchaseType) {
        List<TransactionHistory> results = transactionHistoryService.findByPurchaseType(purchaseType);
        List<TransactionHistoryDto> resultDtos = results.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultDtos);
    }

}
