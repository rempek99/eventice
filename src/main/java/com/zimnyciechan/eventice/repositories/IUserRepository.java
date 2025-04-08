/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 * 🍺🍺🍺
 */

package com.zimnyciechan.eventice.repositories;

import com.zimnyciechan.eventice.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
