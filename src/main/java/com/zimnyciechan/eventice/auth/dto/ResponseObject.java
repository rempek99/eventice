package com.zimnyciechan.eventice.auth.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ResponseObject {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;
    private final String error;

    public ResponseObject(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public ResponseObject(String message) {
        this(message, "");
    }
}
