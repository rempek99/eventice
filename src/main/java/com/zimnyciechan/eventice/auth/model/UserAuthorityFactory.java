package com.zimnyciechan.eventice.auth.model;

import java.util.Set;

import com.zimnyciechan.eventice.auth.constants.RoleConstants;

public class UserAuthorityFactory {

    public static Set<UserAuthority> createDefaultAuthoritiesForUser(User user) {
        return Set.of(
                UserAuthority.builder().authority(RoleConstants.USER).enabled(true).user(user).build(),
                UserAuthority.builder().authority(RoleConstants.ADMIN).user(user).build(),
                UserAuthority.builder().authority(RoleConstants.CREATOR).user(user).build());
    }
}