package ru.unfatcrew.clientcalorietracker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import ru.unfatcrew.clientcalorietracker.rest_service.RestApiService;

@Component
public class RestAuthenticationProvider implements AuthenticationProvider {
    private RestApiService restService;

    @Autowired
    public RestAuthenticationProvider(RestApiService restService) {
        this.restService = restService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        try {
            restService.loginUser(username, password);
        } catch (ResourceAccessException exception) {
            throw new BadCredentialsException("Failed to establish a connection with the server");
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new BadCredentialsException("Invalid username or password");
            }
            throw new BadCredentialsException("An error has occurred. Status code: " + exception.getStatusCode());
        }

        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(),
                AuthorityUtils.NO_AUTHORITIES);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
