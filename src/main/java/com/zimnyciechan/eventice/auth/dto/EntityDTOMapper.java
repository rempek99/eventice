/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.auth.dto;

import com.zimnyciechan.eventice.auth.entity.User;
import com.zimnyciechan.eventice.utils.EncryptionAlgorithms;
import com.zimnyciechan.eventice.utils.EncryptionService;
import com.zimnyciechan.eventice.utils.exceptions.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.List;

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
        return new User(user.getId(), true, user.getUsername(), user.getEmail(), encryptedPassword, List.of());
    }
}
