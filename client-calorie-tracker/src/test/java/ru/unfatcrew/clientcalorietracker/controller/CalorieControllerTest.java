package ru.unfatcrew.clientcalorietracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestTemplate;
import ru.unfatcrew.clientcalorietracker.pojo.dto.ProductPostDTO;
import ru.unfatcrew.clientcalorietracker.pojo.dto.dom.ProductDTO;
import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;
import ru.unfatcrew.clientcalorietracker.pojo.entity.User;
import ru.unfatcrew.clientcalorietracker.rest_service.config.RestTemplateConfig;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@WebMvcTest(CalorieController.class)
@ContextConfiguration(classes = {RestTemplateConfig.class})
@ComponentScan("ru.unfatcrew.clientcalorietracker")
public class CalorieControllerTest {
    @Value("${application.rest.api.url}")
    private String restURL;
    private RestTemplate rest;
    private MockRestServiceServer mockServer;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public CalorieControllerTest(RestTemplate restTemplate,
                                 MockMvc mockMvc) {
        this.rest = restTemplate;
        this.mockMvc = mockMvc;
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(rest);
    }

    @DisplayName("Successfully opening of the login page with unauthorised user")
    @Test
    @WithAnonymousUser
    public void testSuccessfullyLoginPageOpenWithUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @DisplayName("Successfully redirection to main page when login page open with authorised user")
    @Test
    @WithMockUser
    public void testSuccessfullyRedirectionFromLoginToMainPageWithAuthorizedUser() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("Successfully opening of the register page with unauthorised user")
    @Test
    @WithAnonymousUser
    public void testSuccessfullyRegisterPageOpenWithUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @DisplayName("Successfully redirection to main page when register page open with authorised user")
    @Test
    @WithMockUser
    public void testSuccessfullyRedirectionFromRegisterToMainPageWithAuthorizedUser() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("Successfully user register")
    @Test
    @WithAnonymousUser
    public void testSuccessfullyUserRegister() throws Exception {
        User user = new User("FirstName LastName MiddleName", "testuser", "password", "60.0");

        mockServer.expect(requestTo(restURL + "users"))
                .andRespond(withSuccess());

        mockMvc.perform(post("/register")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        mockServer.verify();
    }

    @DisplayName("User register with incorrect user weight")
    @Test
    @WithAnonymousUser
    public void testUserRegisterWithIncorrectUserWeight() throws Exception {
        User user = new User("FirstName LastName MiddleName", "testuser", "password", "-60.0");

        mockMvc.perform(post("/register")
                        .flashAttr("user", user)
                        .with(csrf()))
                .andExpect(result -> Assertions.assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @DisplayName("Successfully opening of the main page")
    @Test
    @WithMockUser
    public void testSuccessfullyMainPageOpen() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"));
    }

    @DisplayName("Successfully opening the product creation page")
    @Test
    @WithMockUser
    public void testSuccessfullyCreateProductPageOpen() throws Exception {
        mockMvc.perform(get("/create-product"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_product"))
                .andExpect(model().attributeExists("product"));
    }


    @DisplayName("Successfully opening of the add meal page")
    @Test
    @WithMockUser
    public void testSuccessfullyAddMealPageOpening() throws Exception {
        mockMvc.perform(get("/add-meal"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_meal"))
                .andExpect(model().attributeExists("addMealDTO"));
    }

    @DisplayName("Error opening the product creation page unauthorized")
    @Test
    @WithAnonymousUser
    public void testUnauthorizedCreateProductPageOpen() throws Exception {
        mockMvc.perform(get("/create-product"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Successfully product creation")
    @Test
    @WithMockUser
    public void testSuccessfullyProductCreation() throws Exception {
        ProductPostDTO product = new ProductPostDTO("User", "Test Product", 15, 10.0f, 5.0f, 2.5f);

        mockServer.expect(requestTo(restURL + "products"))
                .andRespond(withSuccess());

        mockMvc.perform(post("/create-product")
                        .flashAttr("product", product)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/create-product"));

        mockServer.verify();
    }

    @Test
    @DisplayName("Successfully my products page opening")
    @WithMockUser(username = "testuser")
    public void successfullyMyProductsPageOpening() throws Exception {
        List<Product> products = List.of(new Product(1L, "Product 1", 12, 20.0f, 15.0f, 5.0f),
                new Product(2L, "Product 2", 255,125.0f, 55.0f, 0.0f));

        mockServer.expect(requestTo(restURL + "products/user-products?user-login=testuser&limit=100"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(products), MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/my-products"))
                .andExpect(model().attribute("productDTO", new ProductDTO(products)))
                .andExpect(view().name("list_product"));

        mockServer.verify();
    }
}
