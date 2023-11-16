package ru.unfatcrew.clientcalorietracker.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.util.Base64;

@Component
public class RestAuthenticationProvider implements AuthenticationProvider {
    @Value("${application.rest.api.users.url}")
    private String REST_USERS_URL;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        ResponseEntity<String> response;
        try {
            response = processRestAuthentication(username, password);
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

    private ResponseEntity<String> processRestAuthentication(String username, String password) throws RestClientException {
        String authStr = username + ":" + password;
        String authStrBase64 = Base64.getEncoder().encodeToString(authStr.getBytes());

        RequestEntity<Void> request = RequestEntity.get(REST_USERS_URL)
                .header("Authorization", "Basic " + authStrBase64)
                .build();

        return new RestTemplate().exchange(request, String.class);
    }
}
