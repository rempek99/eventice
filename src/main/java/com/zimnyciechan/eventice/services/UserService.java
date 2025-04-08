/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 * 🍺🍺🍺
 */

package com.zimnyciechan.eventice.services;

import com.zimnyciechan.eventice.data.User;
import com.zimnyciechan.eventice.exceptions.InternalException;
import com.zimnyciechan.eventice.repositories.IUserRepository;
import com.zimnyciechan.eventice.utils.EncryptionAlgorithms;
import com.zimnyciechan.eventice.utils.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Override
    public User create(User user) {
        try {
            final String encryptedPassword =
                    encryptionService.encrypt(
                            user.getPassword(), EncryptionAlgorithms.MD5);
            user.setPassword(encryptedPassword);
        } catch (NoSuchAlgorithmException ex) {
            throw InternalException.create();
        }
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Iterable<User> findAll() {
        List<User> all = userRepository.findAll();
        System.out.println("All read users: " + all.size());
        return all;
    }
}
