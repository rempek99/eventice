package com.zimnyciechan.eventice.auth.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.zimnyciechan.eventice.auth.constants.RoleConstants;
import com.zimnyciechan.eventice.auth.model.User;
import com.zimnyciechan.eventice.auth.model.UserAuthority;
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
                final UserAuthority user1Authority = new UserAuthority(-1L, RoleConstants.USER, user1);
                user1.setAuthorities(List.of(user1Authority));

                final User user2 = new User(-1L, true, "user2", "user2@example.com",
                                passwordEncoder.encode("password"), null);
                final UserAuthority user2Authority = new UserAuthority(-1L, RoleConstants.ADMIN, user2);
                user2.setAuthorities(List.of(user2Authority));

                final User user3 = new User(-1L, true, "user3", "user3@example.com",
                                passwordEncoder.encode("password"), null);
                final UserAuthority user3Authority = new UserAuthority(-1L, RoleConstants.CREATOR, user3);
                user3.setAuthorities(List.of(user3Authority));

                userService.createUser(user1);
                userService.createUser(user2);
                userService.createUser(user3);
        }

}
