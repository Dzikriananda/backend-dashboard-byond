package com.dzikriananda.multimatic_backend.util;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> response = new LinkedHashMap<>();
        StringBuilder errMessage = new StringBuilder();

        //Field Error
        //Triggered when a field-level annotation like @NotEmpty, @Email, etc., fails.
        // Example: @NotEmpty(message = "Username is required") → Affects only username.
        ex.getBindingResult().getFieldErrors().forEach(error -> {
                    System.out.println("Field: " + error.getField() + " | Rejected Value: " + error.getRejectedValue());
                    System.out.println("Error Message: " + error.getDefaultMessage()); // Debugging log
                    errMessage.append(error.getDefaultMessage()).append(" | ");
                }
        );

        //Global Error
        //Triggered when a class-level annotation like @ValidUsernameOrEmail fails.
        //Since it validates the entire object, it is considered a global error, not a field error.
        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            System.out.println("Global Error: " + error.getDefaultMessage());
            errMessage.append(error.getDefaultMessage()).append(" | ");
        });

        response.put("status","Error");
        response.put("message", errMessage.toString());
        return response;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status","Error");
        if (ex.getMessage().contains("users_email_key")) { // Check for email unique constraint
            response.put("message", "Email already exists. Please use a different email.");
        } else if (ex.getMessage().contains("users_username_key")) { // Check for username unique constraint
            response.put("message", "Username already exists. Please use a different username.");
        } else {
            response.put("message", "A database error occurred. Please try again.");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}
