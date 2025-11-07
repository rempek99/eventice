package com.zimnyciechan.eventice.core.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ResponseObject {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;

    public ResponseObject(String message) {
        this.message = message;
    }
}
