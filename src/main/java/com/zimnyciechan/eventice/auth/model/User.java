/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Builder.Default
    private boolean enabled = true;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Setter
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    @Builder.Default
    private Set<UserAuthority> authorities = new HashSet<>();

    @Override
    public Collection<UserAuthority> getAuthorities() {
        return new HashSet<>(authorities);
    }

    public void addAuthorities(Set<UserAuthority> authorities) {
        this.authorities.addAll(authorities);
    }
}
