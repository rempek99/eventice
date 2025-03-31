/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 * 🍺🍺🍺
 */

package com.zimnyciechan.eventice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalException extends ResponseStatusException {

    private InternalException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }

    public static InternalException create() {
        return new InternalException("Internal Server Error");
    }

    public static InternalException create(String reason) {
        return new InternalException(reason);
    }
}
