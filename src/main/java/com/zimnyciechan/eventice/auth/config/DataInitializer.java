package com.zimnyciechan.eventice.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.zimnyciechan.eventice.auth.constants.RoleConstants;
import com.zimnyciechan.eventice.auth.model.User;
import com.zimnyciechan.eventice.auth.services.UserService;

@Component
public class DataInitializer implements CommandLineRunner {

        @Autowired
        private UserService userService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private final static boolean ENABLED = false;

        @Override
        public void run(String... args) throws Exception {
                if (ENABLED) {
                        initUsers();
                }
        }

        private void initUsers() {
                final User user1 = new User(-1L, true, "user", "user@example.com",
                                passwordEncoder.encode("password"), null);

                final User user2 = new User(-1L, true, "user2", "user2@example.com",
                                passwordEncoder.encode("password"), null);

                final User user3 = new User(-1L, true, "user3", "user3@example.com",
                                passwordEncoder.encode("password"), null);

                userService.createUser(user1);
                Long user2Id = userService.createUser(user2);
                Long user3Id = userService.createUser(user3);

                userService.grantAuthority(user2Id, RoleConstants.ADMIN);
                userService.grantAuthority(user3Id, RoleConstants.CREATOR);
        }

}
