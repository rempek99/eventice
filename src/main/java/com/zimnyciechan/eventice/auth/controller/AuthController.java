package com.zimnyciechan.eventice.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.zimnyciechan.eventice.auth.model.AuthenticationRequest;
import com.zimnyciechan.eventice.auth.model.User;
import com.zimnyciechan.eventice.auth.services.JwtService;
import com.zimnyciechan.eventice.auth.services.UserService;

import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return new ResponseEntity<>("Password is required", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Long createdId = null;
        try {
            createdId = userService.createUser(user);
        } catch (ConstraintViolationException e) {
            final var sb = new StringBuilder();
            e.getConstraintViolations()
                    .forEach(v -> sb.append(v.getPropertyPath()).append(" ").append(v.getMessage()).append("; "));
            return new ResponseEntity<>("Error creating user: " + sb.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User created with ID: " + createdId, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        final String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

}
