package com.zimnyciechan.eventice.core.dto;

import java.util.HashMap;
import java.util.Map;

public class ValidationErrorResponse extends ResponseObject {
    public ValidationErrorResponse(String message) {
        super(message);
        validationErrors = new HashMap<>();
    }

    private final Map<String, String> validationErrors;

}
