/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {

    private NotFoundException(final String resouceName) {
        super(HttpStatus.NOT_FOUND, String.format("%s not found!", resouceName));
    }

    public static NotFoundException create(final String resourceName) {
        return new NotFoundException(resourceName);
    }

}
