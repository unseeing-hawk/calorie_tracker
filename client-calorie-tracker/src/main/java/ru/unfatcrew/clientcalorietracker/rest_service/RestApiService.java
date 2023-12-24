package ru.unfatcrew.clientcalorietracker.rest_service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;
import ru.unfatcrew.clientcalorietracker.pojo.entity.User;
import ru.unfatcrew.clientcalorietracker.pojo.requests.ChangeProductsRequest;

import java.util.List;

@Service
public class RestApiService {
    private static final String PASSWORD_PREFIX = "{bcrypt}";
    @Value("${application.rest.api.url}")
    private String restURL;
    private RestTemplate rest;

    @Autowired
    public RestApiService(RestTemplate rest) {
        this.rest = rest;
    }

    public void registerNewUser(User user) throws RestClientException {
        User userToAdd = new User(user.getName(), user.getLogin(), encodePassword(user.getPassword()), user.getWeight());
        rest.postForEntity(restURL + "/users", userToAdd, User.class);
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

    public User getUserData() throws RestClientException {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        String uri = UriComponentsBuilder.fromHttpUrl(restURL)
                .path("/users")
                .queryParam("login", username)
                .toUriString();

        return rest.getForObject(uri, User.class);
    }

    public void saveUserSettings(User user) throws RestClientException {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        long userId = getUserData().getId();

        @Valid
        User userToAdd = new User(userId, user.getName(), username, user.getPassword(), user.getWeight());
        userToAdd.setPassword(encodePassword(userToAdd.getPassword()));

        rest.put(restURL + "/users", userToAdd);
    }

    public List<Product> getUserProducts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        String url = UriComponentsBuilder.fromHttpUrl(restURL)
                .path("/products/user-products")
                .queryParam("user-login", username)
                .toUriString();

        RequestEntity<Void> request = RequestEntity.get(url).build();

        ResponseEntity<List<Product>> response = rest.exchange(request, new ParameterizedTypeReference<>() {});

        return response.getBody();
    }

    public void changeUserProducts(ChangeProductsRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        request.setUserLogin(username);

        rest.put(restURL + "/products", request);
    }

    private static String encodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return PASSWORD_PREFIX + passwordEncoder.encode(password);
    }
}
