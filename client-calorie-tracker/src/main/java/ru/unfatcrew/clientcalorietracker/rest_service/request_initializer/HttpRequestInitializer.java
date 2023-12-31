package ru.unfatcrew.clientcalorietracker.rest_service.request_initializer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class HttpRequestInitializer implements ClientHttpRequestInitializer {
    @Override
    public void initialize(ClientHttpRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (auth instanceof AnonymousAuthenticationToken)) {
            return;
        }

        String username = auth.getPrincipal().toString();
        String password = auth.getCredentials().toString();

        HttpHeaders headers = request.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(username, password);
    }
}
