/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.auth.api;

import com.zimnyciechan.eventice.auth.dto.UserDTO;
import com.zimnyciechan.eventice.auth.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public Iterable<UserDTO> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping(value = "{username}")
    public UserDTO getUserByUsername(String username) {
        return userService.findByUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createUser(@RequestBody UserDTO user) {
        System.out.println("CREATING USER:");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Email: " + user.getEmail());
        return userService.create(user);
    }
}

