package com.list.todo.services;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class WebSocketAuthenticatorService {
    // This method MUST return a UsernamePasswordAuthenticationToken, another component in the security chain is testing it with 'instanceof'
    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String  username) throws AuthenticationException {
        if (username == null || username.trim().length()==0) {
            throw new AuthenticationCredentialsNotFoundException("Username was null or empty.");
        }

        // null credentials, we do not pass the password along
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }
}