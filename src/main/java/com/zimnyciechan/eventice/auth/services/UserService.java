/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.auth.services;

import com.zimnyciechan.eventice.auth.model.User;
import com.zimnyciechan.eventice.auth.model.UserAuthority;
import com.zimnyciechan.eventice.auth.model.UserAuthorityFactory;
import com.zimnyciechan.eventice.auth.repositories.IUserRepository;
import com.zimnyciechan.eventice.exceptions.NotFoundException;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User found = userRepository.findByUsername(username).orElseThrow(() -> NotFoundException.create("User"));
        return new org.springframework.security.core.userdetails.User(found.getUsername(),
                found.getPassword(), found.getAuthorities());
    }

    @Transactional
    public Long createUser(@NonNull User user) {
        Set<UserAuthority> authorities = UserAuthorityFactory.createDefaultAuthorities();
        user.addAuthorities(authorities);
        final User saved = userRepository.save(user);
        return saved.getId();
    }

    public void deleteUserByUsername(String username) {
        User found = userRepository.findByUsername(username).orElseThrow(() -> NotFoundException.create("User"));
        userRepository.delete(found);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> NotFoundException.create(id.toString()));
    }

    @Transactional
    public void grantAuthority(Long userId, String authority) {
        modifyUserAuthority(userId, authority, true);
    }

    @Transactional
    public void revokeAuthority(Long userId, String authority) {
        modifyUserAuthority(userId, authority, false);
    }

    private void modifyUserAuthority(Long userId, String authority, boolean enable) {
        if (authority == null || authority.isBlank()) {
            // TODO: custom exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authority cannot be null or empty");
        }
        User user = findById(userId);
        UserAuthority userAuthority = user.getAuthorities()
                .stream()
                .filter(auth -> auth.getAuthority().equals(authority))
                .findFirst()
                .orElseThrow(
                        () -> NotFoundException.create("Authority " + authority + " for user " + user.getUsername()));
        if (userAuthority.isEnabled() == enable) {
            return;
        }
        userAuthority.setEnabled(enable);
        userRepository.save(user);
    }
}
