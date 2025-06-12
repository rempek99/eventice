/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.events.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController {

    @GetMapping("test")
    public String testRequest() {
        return "Hello World!, dupa biskupa";
    }

    @GetMapping("test-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String testAdmin() {
        return "Hello Admin!";
    }

    @GetMapping("test-user")
    @PreAuthorize("hasRole('USER')")
    public String testUser() {
        return "Hello User!";
    }
}