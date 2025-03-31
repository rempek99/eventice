/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 * 🍺🍺🍺
 */

package com.zimnyciechan.eventice.api;

import com.zimnyciechan.eventice.data.User;
import com.zimnyciechan.eventice.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping(value = "{username}")
    public User getUserByUsername(String username) {
        return userService.findByUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(User user) {
        System.out.println("CREATING USER:");
        return userService.create(user);
    }
}

