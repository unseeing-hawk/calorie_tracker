package ru.unfatcrew.clientcalorietracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationManager implements AuthenticationManager {
    private RestAuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticationProvider.authenticate(authentication);
    }

    @Autowired
    public void setAuthenticationProvider(RestAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
}
