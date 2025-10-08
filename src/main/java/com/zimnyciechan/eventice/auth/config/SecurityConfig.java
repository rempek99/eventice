/*
 * Copyright (c) 2025. Created by zimnyciechan
 * Github: rempek99
 *
 */

package com.zimnyciechan.eventice.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.zimnyciechan.eventice.auth.services.CustomUserDetailsService;
import com.zimnyciechan.eventice.auth.services.JwtService;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/public/**", "/login", "/register", "/error").permitAll()
                .anyRequest().authenticated());
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Disables CSRF protection, meaning the application will not be protected
        // against CSRF attacks.Disabling CSRF protection can be justified for
        // applications that are not vulnerable to such attacks, such as RESTful
        // applications using token-based authentication (e.g., JWT) instead of
        // sessions.
        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Despite the filter is used only internaly, it is a bean to allow dependency
    // injection.
    // Spring beans specification requires the method invoking bean creation to be
    // public.
    @Bean
    public Filter jwtRequestFilter() {
        return new JwtFilter(jwtService, customUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
