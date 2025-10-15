/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {

    private static final String DEFAULT_MESSAGE = "Data conflicted";

    private ConflictException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }

    public static ConflictException create() {
        return new ConflictException(DEFAULT_MESSAGE);
    }
}
