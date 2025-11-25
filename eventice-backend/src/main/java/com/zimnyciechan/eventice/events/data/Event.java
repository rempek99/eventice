/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.events.data;

import com.zimnyciechan.eventice.auth.model.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq_gen")
    private Long id;

    private String name;

    private String description;

    @Lob
    private Byte[] imageData;

    @ManyToOne
    private User user;

}
