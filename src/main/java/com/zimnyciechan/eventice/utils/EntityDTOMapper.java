/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.utils;

import com.zimnyciechan.eventice.data.User;
import com.zimnyciechan.eventice.dto.UserDTO;
import com.zimnyciechan.eventice.exceptions.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Component
public class EntityDTOMapper {

    private final EncryptionService encryptionService;

    @Autowired
    public EntityDTOMapper(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    public User toEntity(UserDTO user) {
        final String encryptedPassword;
        try {
            encryptedPassword =
                    encryptionService.encrypt(
                            user.getPassword(), EncryptionAlgorithms.MD5);
        } catch (NoSuchAlgorithmException e) {
            throw InternalException.create();
        }
        return new User(user.getId(), user.getUsername(), user.getEmail(), encryptedPassword);
    }
}
