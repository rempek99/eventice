/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.auth.services;

import com.zimnyciechan.eventice.auth.model.User;
import com.zimnyciechan.eventice.auth.repositories.IUserRepository;
import com.zimnyciechan.eventice.utils.exceptions.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User found = userRepository.findByUsername(username).orElseThrow(() -> NotFoundException.create("User"));
        return new org.springframework.security.core.userdetails.User(found.getUsername(),
                found.getPassword(), found.getAuthorities());
    }
}
