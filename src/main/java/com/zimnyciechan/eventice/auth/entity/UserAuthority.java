package com.zimnyciechan.eventice.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
@AllArgsConstructor
@Table(name = "AUTHORITIES")
public class UserAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
