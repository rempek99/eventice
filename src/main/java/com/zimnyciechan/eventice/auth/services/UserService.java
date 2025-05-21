/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.auth.services;

import com.zimnyciechan.eventice.auth.entity.User;
import com.zimnyciechan.eventice.auth.dto.UserDTO;
import com.zimnyciechan.eventice.utils.exceptions.ConflictException;
import com.zimnyciechan.eventice.auth.repositories.IUserRepository;
import com.zimnyciechan.eventice.auth.dto.EntityDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private EntityDTOMapper mapper;

    @Override
    public UserDTO create(UserDTO user) {

        Optional<User> found = userRepository.findByEmail(user.getEmail());
        if (found.isPresent()) {
            throw ConflictException.create();
        }
        User userEntity = mapper.toEntity(user);
        User created = userRepository.save(userEntity);
        return mapper.toDTO(created);
    }

    @Override
    public UserDTO findByUsername(String username) {
        User found = userRepository.findByUsername(username).orElse(null);
        //TODO handle not found
        return mapper.toDTO(found);
    }

    @Override
    public Iterable<UserDTO> findAll() {
        List<User> all = userRepository.findAll();
        return all.stream().map(mapper::toDTO).toList();
    }
}
