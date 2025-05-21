/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99 
 *
 */

package com.zimnyciechan.eventice.auth.services;

import com.zimnyciechan.eventice.auth.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {

    public UserDTO create(UserDTO user);
    public UserDTO findByUsername(String username);
    public Iterable<UserDTO> findAll();
}
