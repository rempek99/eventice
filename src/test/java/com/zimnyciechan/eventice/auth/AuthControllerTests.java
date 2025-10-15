package com.zimnyciechan.eventice.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import com.zimnyciechan.eventice.auth.controller.AuthController;
import com.zimnyciechan.eventice.auth.model.AuthenticationRequest;
import com.zimnyciechan.eventice.auth.model.User;
import com.zimnyciechan.eventice.auth.services.UserService;
import com.zimnyciechan.eventice.exceptions.NotFoundException;

@SpringBootTest
@SuppressWarnings("null")
@ActiveProfiles("test")
public class AuthControllerTests {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserService userService;

    private final String TEST_USERNAME = "testuser";
    private final String TEST_PASSWORD = "testpassword";
    private final String TEST_EMAIL = "testuser@example.com";

    @Test
    public void contextLoads() {
        assertNotNull(authController);
    }

    @BeforeEach
    public void setUp() {
        // Delete test user if exists
        try {
            userService.deleteUserByUsername(TEST_USERNAME);
        } catch (NotFoundException e) {
            // User might not exist, ignore the exception
        }
        assertThrowsExactly(NotFoundException.class, () -> {
            userService.loadUserByUsername(TEST_USERNAME);
        });
    }

    @Test
    public void testLoginFailed() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();
        var response = authController.loginUser(request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void registerUserMissingEmailFailed() {
        User user = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();
        var response = authController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("email must not be null"));
    }

    @Test
    public void registerUserMissingUsernameFailed() {
        User user = User.builder()
                .password(TEST_PASSWORD)
                .email(TEST_EMAIL)
                .build();
        var response = authController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("username must not be null"));
    }

    @Test
    public void registerUserMissingPasswordFailed() {
        User user = User.builder()
                .username(TEST_USERNAME)
                .email(TEST_EMAIL)
                .build();
        var response = authController.registerUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Password is required"));
    }

    @Test
    public void registerUser() {
        User user = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .email(TEST_EMAIL)
                .build();
        var response = authController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User created with ID: 1", response.getBody());

        UserDetails createdUser = userService.loadUserByUsername(TEST_USERNAME);
        assertNotNull(createdUser);
        assertEquals(TEST_USERNAME, createdUser.getUsername());
        assertNotEquals(TEST_PASSWORD, createdUser.getPassword());
        assertNotNull(createdUser.getAuthorities());
        assertEquals(3, createdUser.getAuthorities().size());
        assertTrue(createdUser.isEnabled());
    }

    @Test
    public void registerAndLoginUser() {
        User user = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .email(TEST_EMAIL)
                .build();
        var registerResponse = authController.registerUser(user);
        assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());
        var loginResponse = authController.loginUser(AuthenticationRequest.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build());
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        assertTrue(loginResponse.getBody().length() > 100); // JWT
    }

    @Test
    public void jwtClaims() {
        User user = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .email(TEST_EMAIL)
                .build();
        var registerResponse = authController.registerUser(user);
        assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());
        var loginResponse = authController.loginUser(AuthenticationRequest.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build());
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        assertTrue(loginResponse.getBody().length() > 100); // JWT

        String jwt = loginResponse.getBody();
        String[] jwtParts = jwt.split("\\.");
        assertEquals(3, jwtParts.length);

        String claimsJson = new String(java.util.Base64.getUrlDecoder().decode(jwtParts[1]));
        assertTrue(claimsJson.contains("\"sub\":\"" + TEST_USERNAME + "\""));
    }

}
