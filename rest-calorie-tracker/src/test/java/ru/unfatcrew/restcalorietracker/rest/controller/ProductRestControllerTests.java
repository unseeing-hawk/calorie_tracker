package ru.unfatcrew.restcalorietracker.rest.controller;

import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPostDTO;
import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPutDTO;
import ru.unfatcrew.restcalorietracker.pojo.request.ChangeProductsRequest;
import ru.unfatcrew.restcalorietracker.pojo.response.ChangeProductsResponse;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import ru.unfatcrew.restcalorietracker.service.ProductService;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import jakarta.validation.ConstraintViolationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.HashSet;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTests {

    private User user;
    private Product product;
    private List<ProductPostDTO> listProductPostDTOs;
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void setup() {
        user = new User("User Name",
                                "userlogin",
                                "{bcrypt}$2y$10$Sp/ZFpu6i5LJ7lgaEa09yOTgXp0PAA2xwkf5Gb51rwTGKZiJtNnQa",
                                90.17f);
        product  = new Product(
                0L, 
                user,
                "Random Product", 
                200, 
                15.5f, 
                10.2f, 
                30.8f, 
                true);

        listProductPostDTOs = Arrays.asList(
                new ProductPostDTO(user.getLogin(), 
                                product.getName(),
                                product.getCalories(), 
                                product.getProteins(), 
                                product.getFats(), 
                                product.getCarbohydrates()),
                new ProductPostDTO("userLogin2",
                                        "product2",
                                        100,
                                        3.3f,
                                        1.5f,
                                        50.23f));
    }

    @Test
    @DisplayName("Test adding product - Success")
    public void testAddProduct() throws Exception {
        String productString = objectMapper.writeValueAsString(product);

        when(productService.addProduct(any(ProductPostDTO.class))).thenReturn(product);
    
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productString))
                .andExpect(status().isOk())
                .andExpect(content().json(productString));
    }

    @Test
    @DisplayName("Test adding product with ConstraintViolationException")
    public void testAddProduct_ConstraintViolationException() throws Exception {

        when(productService.addProduct(any(ProductPostDTO.class)))
                           .thenThrow(new ConstraintViolationException(new HashSet<>()));

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test adding product with IllegalRequestArgumentException")
    public void testAddProduct_IllegalRequestArgumentException() throws Exception {

        when(productService.addProduct(any(ProductPostDTO.class)))
                           .thenThrow(IllegalRequestArgumentException.class);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test changing product - Success")
    public void testChangeProducts() throws Exception {
        List<ProductPutDTO> productsForChange = Arrays.asList(new ProductPutDTO(1, 
                                                            product.getName(), 
                                                            product.getCalories(), 
                                                            product.getProteins(), 
                                                            product.getFats(), 
                                                            product.getCarbohydrates()));
        List<Long> productIdsForDeletion = Arrays.asList(1L, 10L, 4L);
        ChangeProductsResponse response = new ChangeProductsResponse(productsForChange, productIdsForDeletion);

        when(productService.changeProducts(any(ChangeProductsRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ChangeProductsRequest())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("Test changing product with ConstraintViolationException")
    public void testChangeProducts_ConstraintViolationException() throws Exception {

        when(productService.changeProducts(any(ChangeProductsRequest.class)))
                .thenThrow(new ConstraintViolationException(new HashSet<>()));

        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ChangeProductsRequest())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test change products with IllegalRequestArgumentException")
    public void testChangeProducts_IllegalRequestArgumentException() throws Exception {

        when(productService.changeProducts(any(ChangeProductsRequest.class)))
                .thenThrow(IllegalRequestArgumentException.class);

        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ChangeProductsRequest())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test сhange products with ResourceNotFoundException")
    public void testChangeProducts_ResourceNotFoundException() throws Exception {

        when(productService.changeProducts(any(ChangeProductsRequest.class)))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ChangeProductsRequest())))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test get user products - Success")
    @ParameterizedTest
    @CsvSource({
            "1, 0, testUser",
            "100, 1000, 123456789012345678901234567890"
    })
    public void testGetUserProducts(int limit, int offset, String userLogin) throws Exception {

        when(productService.getUserProducts(anyInt(), anyInt(), anyString())).thenReturn(listProductPostDTOs);

        mockMvc.perform(get("/products/user-products")
            .param("limit", String.valueOf(limit))
            .param("offset", String.valueOf(offset))
            .param("user-login", userLogin)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(listProductPostDTOs)));
    }

    @DisplayName("Test get user products with invalid request params")
    @ParameterizedTest
    @CsvSource({
            "0, 0, testUser",
            "1, -1, testUser",
            "1, 0, shortUs",
            "101, 0, testUser",
            "1, 0, tooLongUser123456789012345678901234567890"
    })
    public void testGetUserProducts_InvalidParams(int limit, int offset, String userLogin) throws Exception {
        mockMvc.perform(get("/products/user-products")
            .param("limit", String.valueOf(limit))
            .param("offset", String.valueOf(offset))
            .param("user-login", userLogin)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test get user products with ConstraintViolationException")
    public void testGetUserProducts_ConstraintViolationException() throws Exception {
        when(productService.getUserProducts(anyInt(), anyInt(), anyString()))
                .thenThrow(new ConstraintViolationException(new HashSet<>()));

        mockMvc.perform(get("/products/user-products")
                .param("limit", "10")
                .param("offset", "0")
                .param("user-login", "testUser")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test get user products with IllegalRequestArgumentException")
    public void testGetUserProducts_IllegalRequestArgumentException() throws Exception {
        when(productService.getUserProducts(anyInt(), anyInt(), anyString()))
                .thenThrow(IllegalRequestArgumentException.class);

        mockMvc.perform(get("/products/user-products")
                .param("limit", "10")
                .param("offset", "0")
                .param("user-login", "testUser")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Test search products - Success")
    @ParameterizedTest
    @CsvSource({
            "1, 0, testUser, t",
            "100, 1000, 123456789012345678901234567890, qy2&^%$#^&*!@#$%^&*()_+{qy2&^%$#^&}|:52<>?`1234567890-=/qy2&^%$#^&/][';/.imnbvcxzlkjhgfdsapoiuytrewq"     
        })
    public void testSearchProducts_Success(int limit, int offset, String userLogin, String pattern) throws Exception {

        when(productService.searchProducts(anyInt(), anyInt(), anyString(), anyString())).thenReturn(listProductPostDTOs);

        mockMvc.perform(get("/products/search-products")
                .param("limit", String.valueOf(limit))
                .param("offset", String.valueOf(offset))
                .param("user-login", userLogin)
                .param("pattern", pattern)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(listProductPostDTOs)));
    }

    @DisplayName("Test search products with invalid request params")
    @ParameterizedTest
    @CsvSource({
            "0, 0, testUser, testPattern",
            "1, -1, testUser, testPattern",
            "1, 0, shortUs, testPattern",
            "1, 0, testUser,",
            "101, 0, testUser, testPattern",
            "1, 0, tooLongUser123456789012345678901234567890, testPattern",
            "1, 0, testUser, qy2&^%$#^&*!@#$%^&*()_+{qy2&^%$#^&}|:52<>?`1234567890-=/qy2&^%$#^&/][';/.ymnbvcxzlkjhgfdsapoiuytrewqw"
    })
    public void testSearchProducts_InvalidParams(int limit, int offset, String userLogin, String pattern) throws Exception {
        mockMvc.perform(get("/products/search-products")
                .param("limit", String.valueOf(limit))
                .param("offset", String.valueOf(offset))
                .param("user-login", userLogin)
                .param("pattern", pattern)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test search products with ConstraintViolationException")
    public void testSearchProducts_ConstraintViolationException() throws Exception {
        when(productService.searchProducts(anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(new ConstraintViolationException(new HashSet<>()));

        mockMvc.perform(get("/products/search-products")
                .param("limit", "10")
                .param("offset", "0")
                .param("user-login", "testUser")
                .param("pattern", "testPattern")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test search products with IllegalRequestArgumentException")
    public void testSearchProducts_IllegalRequestArgumentException() throws Exception {
        when(productService.searchProducts(anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(IllegalRequestArgumentException.class);

        mockMvc.perform(get("/products/search-products")
                .param("limit", "10")
                .param("offset", "0")
                .param("user-login", "testUser")
                .param("pattern", "testPattern")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

