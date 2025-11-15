package com.zimnyciechan.eventice.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.zimnyciechan.eventice.auth.dto.LoginResponse;
import com.zimnyciechan.eventice.auth.dto.RegisterRequest;
import com.zimnyciechan.eventice.auth.exceptions.UserNotFoundException;
import com.zimnyciechan.eventice.auth.model.AuthenticationRequest;
import com.zimnyciechan.eventice.auth.model.User;
import com.zimnyciechan.eventice.auth.services.JwtService;
import com.zimnyciechan.eventice.auth.services.UserService;
import com.zimnyciechan.eventice.core.dto.ResponseObject;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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
    public ResponseEntity<ResponseObject> registerUser(@Valid @RequestBody RegisterRequest requestData) {
        User user = User.builder()
                .id(null)
                .username(requestData.getUsername())
                .email(requestData.getEmail())
                .password(passwordEncoder.encode(requestData.getPassword()))
                .build();
        ;
        userService.createUser(user);
        return new ResponseEntity<>(new ResponseObject("User created"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>(new LoginResponse(null), HttpStatus.FORBIDDEN);
        }
        User userDetails;
        try {
            userDetails = userService.getUserByUsername(request.getUsername());
        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(new LoginResponse(null), HttpStatus.FORBIDDEN);
        }
        String jwt = jwtService.generateToken(userDetails);
        return new ResponseEntity<>(new LoginResponse(jwt), HttpStatus.OK);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

}
