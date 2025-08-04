package com.busbooking.busbooking.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class AuthToken extends AbstractAuthenticationToken {

    private final String username;

    public AuthToken(String username, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.username = username;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}
