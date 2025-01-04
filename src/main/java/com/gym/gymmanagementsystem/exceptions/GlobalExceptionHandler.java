package com.gym.gymmanagementsystem.exceptions;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1) ResourceNotFoundException -> 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    // 2) Validace: MethodArgumentNotValidException -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Získat všechny field errors a uložit do mapy: "pole" : "chybová hláška"
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        // Můžeš přidat i ex.getBindingResult().getGlobalErrors() pro @NotBlank atd. na class-level

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 3) ResourceAlreadyExistsException -> 409
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());
        // 409 Conflict značí konflikt v datech (duplicitní unikátní hodnota)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }


    // Obecné chyby -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", "Internal Server Error: " + ex.getMessage());
        // V produkci raději nevystavuj ex.getMessage() naplno,
        // ale pro debug/ vývoj je to v pohodě
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }
}
