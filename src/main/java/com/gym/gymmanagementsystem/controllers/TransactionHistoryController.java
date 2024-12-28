package com.gym.gymmanagementsystem.controllers;

import com.gym.gymmanagementsystem.dto.TransactionHistoryDto;
import com.gym.gymmanagementsystem.dto.mappers.TransactionHistoryMapper;
import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.TransactionHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transaction-history")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;
    private final TransactionHistoryMapper mapper = TransactionHistoryMapper.INSTANCE;

    public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
        this.transactionHistoryService = transactionHistoryService;
    }

    // GET /api/transaction-history
    @GetMapping
    public ResponseEntity<List<TransactionHistoryDto>> getAllTransactionHistories() {
        List<TransactionHistory> histories = transactionHistoryService.getAllTransactionHistories();
        List<TransactionHistoryDto> historyDtos = histories.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(historyDtos);
    }

    // GET /api/transaction-history/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TransactionHistoryDto> getTransactionHistoryById(@PathVariable Integer id) {
        TransactionHistory history = transactionHistoryService.getTransactionHistoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransactionHistory not found with id " + id));
        TransactionHistoryDto dto = mapper.toDto(history);
        return ResponseEntity.ok(dto);
    }

    // POST /api/transaction-history
    @PostMapping
    public ResponseEntity<TransactionHistoryDto> createTransactionHistory(@Valid @RequestBody TransactionHistoryDto transactionHistoryDto) {
        TransactionHistory transactionHistory = mapper.toEntity(transactionHistoryDto);
        TransactionHistory created = transactionHistoryService.createTransactionHistory(transactionHistory);
        TransactionHistoryDto createdDto = mapper.toDto(created);
        return ResponseEntity.ok(createdDto);
    }

    // PUT /api/transaction-history/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TransactionHistoryDto> updateTransactionHistory(@PathVariable Integer id,
                                                                          @Valid @RequestBody TransactionHistoryDto transactionHistoryDto) {
        TransactionHistory transactionHistoryDetails = mapper.toEntity(transactionHistoryDto);
        TransactionHistory updated = transactionHistoryService.updateTransactionHistory(id, transactionHistoryDetails);
        TransactionHistoryDto updatedDto = mapper.toDto(updated);
        return ResponseEntity.ok(updatedDto);
    }

    // DELETE /api/transaction-history/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionHistory(@PathVariable Integer id) {
        transactionHistoryService.deleteTransactionHistory(id);
        return ResponseEntity.noContent().build();
    }

    // Speciální příklad: GET /api/transaction-history/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionHistoryDto>> findByUserId(@PathVariable Integer userId) {
        List<TransactionHistory> results = transactionHistoryService.findByUserId(userId);
        List<TransactionHistoryDto> resultDtos = results.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultDtos);
    }

    // Speciální příklad: GET /api/transaction-history/purchaseType/{purchaseType}
    @GetMapping("/purchaseType/{purchaseType}")
    public ResponseEntity<List<TransactionHistoryDto>> findByPurchaseType(@PathVariable String purchaseType) {
        List<TransactionHistory> results = transactionHistoryService.findByPurchaseType(purchaseType);
        List<TransactionHistoryDto> resultDtos = results.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultDtos);
    }
}
