package ru.unfatcrew.clientcalorietracker.rest_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
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
}
