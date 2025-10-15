package com.zimnyciechan.eventice.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.zimnyciechan.eventice.auth.constants.RoleConstants;
import com.zimnyciechan.eventice.auth.model.User;
import com.zimnyciechan.eventice.auth.services.UserService;

@SpringBootTest
@ActiveProfiles("test")
public class UserAuthorityTests {

        private final static String TEST_USERNAME = "testuser";
        private final static String TEST_PASSWORD = "testpassword";
        private final static String TEST_EMAIL = "testuser@example.com";

        @Autowired
        private UserService userService;

        @Test
        public void newUserHasDefaultAuthorities() {
                // This test is to ensure that the UserAuthorityFactory creates the correct
                // default authorities

                User user = User.builder()
                                .username(TEST_USERNAME)
                                .password(TEST_PASSWORD)
                                .email(TEST_EMAIL)
                                .build();

                Long createdUserId = userService.createUser(user);

                assertNotNull(createdUserId);

                User createdUser = userService.findById(createdUserId);

                assertTrue(createdUser.isEnabled());
                assertTrue(createdUser.getUsername().equals(TEST_USERNAME));
                // Check that the user has the default ROLE_USER authority
                assertNotNull(createdUser.getAuthorities());
                assert (createdUser.getAuthorities().stream()
                                .anyMatch(auth -> auth.getAuthority().equals(RoleConstants.USER)));
                assert (createdUser.getAuthorities().stream()
                                .anyMatch(auth -> auth.getAuthority().equals(RoleConstants.ADMIN)));
                assert (createdUser.getAuthorities().stream()
                                .anyMatch(auth -> auth.getAuthority().equals(RoleConstants.CREATOR)));

                assertTrue(
                                createdUser.getAuthorities().stream()
                                                .filter(auth -> auth.getAuthority().equals(RoleConstants.USER))
                                                .findFirst()
                                                .get().isEnabled());

                assertFalse(
                                createdUser.getAuthorities().stream()
                                                .filter(auth -> auth.getAuthority().equals(RoleConstants.ADMIN))
                                                .findFirst()
                                                .get().isEnabled());
                assertFalse(
                                createdUser.getAuthorities().stream()
                                                .filter(auth -> auth.getAuthority().equals(RoleConstants.CREATOR))
                                                .findFirst()
                                                .get().isEnabled());
        }
}
