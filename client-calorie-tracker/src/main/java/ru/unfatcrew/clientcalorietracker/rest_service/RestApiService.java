package ru.unfatcrew.clientcalorietracker.rest_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.unfatcrew.clientcalorietracker.pojo.entity.User;

@Service
public class RestApiService {
    @Value("${application.rest.api.url}")
    private String restURL;
    private RestTemplate rest;

    @Autowired
    public RestApiService(RestTemplate rest) {
        this.rest = rest;
    }

    public void registerNewUser(User user) throws RestClientException {
        rest.postForEntity(restURL + "/users", user, User.class);
    }

    public void loginUser(String username, String password) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        String uri = UriComponentsBuilder.fromHttpUrl(restURL)
                .path("/users")
                .queryParam("login", username)
                .toUriString();

        RequestEntity<Void> request = RequestEntity.get(uri)
                .headers(headers)
                .build();

        rest.exchange(request, String.class);
    }
}
