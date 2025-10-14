package com.zimnyciechan.eventice.auth.model;

import java.util.Set;

public class UserAuthorityFactory {

    public static Set<UserAuthority> createDefaultAuthoritiesForUser(User user) {
        return Set.of(
                UserAuthority.builder().authority("USER").enabled(true).user(user).build(),
                UserAuthority.builder().authority("ADMIN").user(user).build(),
                UserAuthority.builder().authority("CREATOR").user(user).build());
    }
}