package com.gym.gymmanagementsystem.controllers;


import com.gym.gymmanagementsystem.entities.TransactionHistory;
import com.gym.gymmanagementsystem.exceptions.ResourceNotFoundException;
import com.gym.gymmanagementsystem.services.TransactionHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction-history")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
        this.transactionHistoryService = transactionHistoryService;
    }

    // GET /api/transaction-history
    @GetMapping
    public ResponseEntity<List<TransactionHistory>> getAllTransactionHistories() {
        List<TransactionHistory> histories = transactionHistoryService.getAllTransactionHistories();
        return ResponseEntity.ok(histories);
    }

    // GET /api/transaction-history/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TransactionHistory> getTransactionHistoryById(@PathVariable Integer id) {
        TransactionHistory history = transactionHistoryService.getTransactionHistoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TransactionHistory not found with id " + id));
        return ResponseEntity.ok(history);
    }

    // POST /api/transaction-history
    @PostMapping
    public ResponseEntity<TransactionHistory> createTransactionHistory(@RequestBody TransactionHistory transactionHistory) {
        TransactionHistory created = transactionHistoryService.createTransactionHistory(transactionHistory);
        return ResponseEntity.ok(created);
    }

    // PUT /api/transaction-history/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TransactionHistory> updateTransactionHistory(@PathVariable Integer id,
                                                                       @RequestBody TransactionHistory transactionHistoryDetails) {
        TransactionHistory updated = transactionHistoryService.updateTransactionHistory(id, transactionHistoryDetails);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/transaction-history/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionHistory(@PathVariable Integer id) {
        transactionHistoryService.deleteTransactionHistory(id);
        return ResponseEntity.noContent().build();
    }

    // Speciální příklad: GET /api/transaction-history/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionHistory>> findByUserId(@PathVariable Integer userId) {
        List<TransactionHistory> results = transactionHistoryService.findByUserId(userId);
        return ResponseEntity.ok(results);
    }

    // Speciální příklad: GET /api/transaction-history/purchaseType/{purchaseType}
    @GetMapping("/purchaseType/{purchaseType}")
    public ResponseEntity<List<TransactionHistory>> findByPurchaseType(@PathVariable String purchaseType) {
        List<TransactionHistory> results = transactionHistoryService.findByPurchaseType(purchaseType);
        return ResponseEntity.ok(results);
    }
}
