package com.zimnyciechan.eventice.auth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;

import com.zimnyciechan.eventice.auth.constants.RoleConstants;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AUTHORITIES")
public class UserAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String authority = RoleConstants.USER;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @NotNull
    @Setter
    private boolean enabled = false;

    public UserAuthority(String authority, boolean enabled) {
        this.authority = authority;
        this.enabled = enabled;
    }
}
