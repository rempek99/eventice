package com.zimnyciechan.eventice.core.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.zimnyciechan.eventice.core.dto.ResponseObject;
import com.zimnyciechan.eventice.core.dto.ValidationErrorResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalRequestValidator {
 
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String,String> fieldErrors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse(fieldErrors));
    }
}
