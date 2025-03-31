/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 * 🍺🍺🍺
 */

package com.zimnyciechan.eventice.repositories;

import com.zimnyciechan.eventice.data.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEventRepository extends JpaRepository<Event, Long> {
}
