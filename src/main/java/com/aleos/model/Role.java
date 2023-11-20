package com.aleos.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Represents the roles that a user can have in the application.
 * Implementing GrantedAuthority allows the roles to be used within
 * Spring Security's authentication and authorization mechanisms.
 */
public enum Role implements GrantedAuthority {
    ROLE_PREMIUM_USER,
    ROLE_USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
