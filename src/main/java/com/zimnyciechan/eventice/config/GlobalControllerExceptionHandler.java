package com.zimnyciechan.eventice.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zimnyciechan.eventice.auth.dto.ResponseObject;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseObject> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        final Throwable rootCause = ex.getRootCause();
        String exceptionMessage = null;
        if (rootCause != null) {
            exceptionMessage = rootCause.getMessage();
        }
        final String message = extractMeaningfulMessage(exceptionMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("Failed", message));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseObject> handleConstraintViolation(ConstraintViolationException ex) {
        final String message = extractMeaningfulMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("Failed", message));
    }

    private String extractMeaningfulMessage(String rawMessage) {
        if (rawMessage == null) {
            return "Database constraint violation";
        }
        rawMessage = rawMessage.toLowerCase();
        // Example: parse known SQL constraint keywords
        if (rawMessage.contains("users_email_key")) {
            return "Email address already exists.";
        } else if (rawMessage.contains("users_username_key")) {
            return "Username already exists.";
        } else if (rawMessage.contains("NULL not allowed") ||
                rawMessage.contains("must not be null")) {
            return "A required field is missing.";
        }

        // Default fallback
        return "Database constraint violation: " + rawMessage;
    }

}
