/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.auth.services;

import com.zimnyciechan.eventice.auth.exceptions.UserNotFoundException;
import com.zimnyciechan.eventice.auth.model.User;
import com.zimnyciechan.eventice.auth.model.UserAuthority;
import com.zimnyciechan.eventice.auth.model.UserAuthorityFactory;
import com.zimnyciechan.eventice.auth.repositories.IUserRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User found = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
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
        User found = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        userRepository.delete(found);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    }

    @Transactional
    public void grantAuthority(Long userId, String authority) {
        modifyUserAuthority(userId, authority, true);
    }

    @Transactional
    public void revokeAuthority(Long userId, String authority) {
        modifyUserAuthority(userId, authority, false);
    }

    private boolean modifyUserAuthority(Long userId, String authority, boolean enable) {
        User user = findById(userId);
        Optional<UserAuthority> userAuthorityOptional = user.getAuthorities()
                .stream()
                .filter(auth -> auth.getAuthority().equals(authority))
                .findFirst();
        if(userAuthorityOptional.isEmpty()) {
            return false;
        }
        if (userAuthorityOptional.get().isEnabled() == enable) {
            return false;
        }
        userAuthorityOptional.get().setEnabled(enable);
        userRepository.save(user);
        return true;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
