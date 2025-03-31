/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99 
 * 🍺🍺🍺
 */

package com.zimnyciechan.eventice.services;

import com.zimnyciechan.eventice.data.User;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {

    public User create(User user);
    public User findByUsername(String username);
    public Iterable<User> findAll();
}
