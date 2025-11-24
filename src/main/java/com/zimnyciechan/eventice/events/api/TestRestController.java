/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.events.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zimnyciechan.eventice.auth.constants.RoleConstants;
import com.zimnyciechan.eventice.core.dto.ResponseObject;

@RestController
public class TestRestController {

    @GetMapping("test")
    public ResponseEntity<ResponseObject> testRequest() {
        return new ResponseEntity<>(new ResponseObject("Hello World!, dupa biskupa"), HttpStatus.OK);
    }

    @GetMapping("test-admin")
    @PreAuthorize(RoleConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<ResponseObject> testAdmin() {
        return new ResponseEntity<>(new ResponseObject("Hello Admin!"), HttpStatus.OK);
    }

    @GetMapping("test-user")
    @PreAuthorize(RoleConstants.HAS_ROLE_USER)
    public ResponseEntity<ResponseObject> testUser() {
        return new ResponseEntity<>(new ResponseObject("Hello User!"), HttpStatus.OK);
    }

    @GetMapping("test-creator")
    @PreAuthorize(RoleConstants.HAS_ROLE_CREATOR)
    public ResponseEntity<ResponseObject> testCreator() {
        return new ResponseEntity<>(new ResponseObject("Hello Creator!"), HttpStatus.OK);
    }
}