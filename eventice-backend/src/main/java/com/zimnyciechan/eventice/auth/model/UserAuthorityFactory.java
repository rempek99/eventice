package com.zimnyciechan.eventice.auth.model;

import java.util.Set;

import com.zimnyciechan.eventice.auth.constants.RoleConstants;

public class UserAuthorityFactory {

    public static Set<UserAuthority> createDefaultAuthorities() {
        return Set.of(
                new UserAuthority(RoleConstants.USER, true),
                new UserAuthority(RoleConstants.ADMIN, false),
                new UserAuthority(RoleConstants.CREATOR, false));
    }
}