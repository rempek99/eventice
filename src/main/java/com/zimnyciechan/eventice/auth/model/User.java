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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS", uniqueConstraints = {
        @UniqueConstraint(name = "users_username_key", columnNames = "username"),
        @UniqueConstraint(name = "users_email_key", columnNames = "email")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Builder.Default
    private boolean enabled = true;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    @Setter
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    @Builder.Default
    private Set<UserAuthority> authorities = new HashSet<>();

    @Override
    public Collection<UserAuthority> getAuthorities() {
        return authorities.stream()
                .filter(a -> a.isEnabled())
                .toList();
    }

    public void addAuthorities(Set<UserAuthority> authorities) {
        if (authorities == null) {
            return;
        }
        for (UserAuthority authority : authorities) {
            authority.setUser(this);
            this.authorities.add(authority);
        }
    }
}
