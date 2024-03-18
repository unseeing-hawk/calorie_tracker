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
import ru.unfatcrew.clientcalorietracker.pojo.dto.*;
import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;
import ru.unfatcrew.clientcalorietracker.pojo.entity.User;
import ru.unfatcrew.clientcalorietracker.pojo.requests.ChangeMealsRequest;
import ru.unfatcrew.clientcalorietracker.pojo.requests.ChangeProductsRequest;

import java.util.ArrayList;
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String url = UriComponentsBuilder.fromHttpUrl(restURL)
                .path("/products/user-products")
                .queryParam("user-login", username)
                .queryParam("limit", 100)
                .toUriString();

        RequestEntity<Void> request = RequestEntity.get(url).build();

        ResponseEntity<List<Product>> response = rest.exchange(request, new ParameterizedTypeReference<>() {});

        return response.getBody();
    }

    public List<SearchProductDTO> searchProducts(String pattern) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String url = UriComponentsBuilder.fromHttpUrl(restURL)
                .path("/products/search-products")
                .queryParam("user-login", username)
                .queryParam("pattern", pattern)
                .toUriString();

        RequestEntity<Void> request = RequestEntity.get(url).build();
        ResponseEntity<List<SearchProductDTO>> response = rest.exchange(request, new ParameterizedTypeReference<>() {});

        return response.getBody();
    }

    public void changeUserProducts(ChangeProductsRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        request.setUserLogin(username);

        rest.put(restURL + "/products", request);
    }

    public void addUserProduct(ProductPostDTO product) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        product.setUserLogin(username);

        rest.postForObject(restURL + "/products", product, ProductPostDTO.class);
    }

    public List<Product> addFatSecretProducts(List<SearchProductDTO> products) {
        List<Product> productsList = new ArrayList<>();

        for (SearchProductDTO product : products) {
            productsList.add(rest.postForObject(restURL + "/products", product, Product.class));
        }

        return productsList;
    }

    public List<DaySummaryDTO> getDaySummary(String startDate, String endDate) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        String url = UriComponentsBuilder.fromHttpUrl(restURL)
                .path("/meals/summary")
                .queryParam("start-date", startDate)
                .queryParam("end-date", endDate)
                .queryParam("user-login", username)
                .toUriString();

        RequestEntity<Void> request = RequestEntity.get(url).build();
        ResponseEntity<List<DaySummaryDTO>> response = rest.exchange(request, new ParameterizedTypeReference<>() { });
        return response.getBody();
    }

    public MealGetDTO getMeals(String date) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String url = UriComponentsBuilder.fromHttpUrl(restURL)
                .path("/meals")
                .queryParam("user-login", username)
                .queryParam("date", date)
                .toUriString();

        return rest.getForObject(url, MealGetDTO.class);
    }

    public void addMeal(MealPostDTO mealPostDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        mealPostDTO.setUserLogin(username);

        rest.postForObject(restURL + "/meals", mealPostDTO, MealPostDTO.class);
    }

    public void changeMeals(ChangeMealsRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        request.setUserLogin(username);
        rest.put(restURL + "/meals", request);
    }

    private static String encodePassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return PASSWORD_PREFIX + passwordEncoder.encode(password);
    }
}
