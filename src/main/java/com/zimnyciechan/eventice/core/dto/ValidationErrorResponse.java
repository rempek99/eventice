package com.zimnyciechan.eventice.core.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class ValidationErrorResponse extends ResponseObject {

    private final static String MESSAGE = "Validation error";
    
    private final Map<String, String> validationErrors;

    public ValidationErrorResponse(Map<String,String> validationErrors) {
        super(MESSAGE);
        this.validationErrors = new HashMap<>(validationErrors);
    }
}
