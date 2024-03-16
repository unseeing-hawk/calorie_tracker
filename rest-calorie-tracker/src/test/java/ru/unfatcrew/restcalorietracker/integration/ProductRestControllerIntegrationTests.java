package ru.unfatcrew.restcalorietracker.integration;

import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.dao.ProductDAO;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPutDTO;
import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPostDTO;
import ru.unfatcrew.restcalorietracker.pojo.request.ChangeProductsRequest;
import ru.unfatcrew.restcalorietracker.pojo.response.ChangeProductsResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRestControllerIntegrationTests {
    private User user;
    private List<Product> products;
    private ProductPostDTO productPostDTO;

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        productDAO.deleteAll();
        userDAO.deleteAll();

        user = new User("User Name",
                                "userlogin2",
                                "{bcrypt}$2y$10$Sp/ZFpu6i5LJ7lgaEa09yOTgXp0PAA2xwkf5Gb51rwTGKZiJtNnQa",
                                90.17f);
        userDAO.save(user);

        products  = Arrays.asList(new Product(
                                        0L, 
                                        user,
                                        "Random Product", 
                                        200, 
                                        15.5f, 
                                        10.2f, 
                                        30.8f, 
                                        true),
                                  new Product(
                                        0L,
                                        user, 
                                        "product1",
                                        400, 
                                        7f, 
                                        82.4f, 
                                        35.1f,
                                        true));
        productDAO.saveAll(products);

        productPostDTO = new ProductPostDTO(
                        user.getLogin(),
                        "product2",
                        100,
                        3.3f,
                        1.5f,
                        50.23f);
    }

    @Test
    @DisplayName("Test adding product - Success")
    public void testAddProduct() throws Exception {
        
        String productString = objectMapper.writeValueAsString(productPostDTO);
    
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fatsecretId", Float.class).value(productPostDTO.getFatsecretId()))
                .andExpect(jsonPath("$.user.login").value(productPostDTO.getUserLogin()))
                .andExpect(jsonPath("$.name").value(productPostDTO.getName()))
                .andExpect(jsonPath("$.calories").value(productPostDTO.getCalories()))
                .andExpect(jsonPath("$.proteins", Float.class).value(productPostDTO.getProteins()))
                .andExpect(jsonPath("$.fats", Float.class).value(productPostDTO.getFats()))
                .andExpect(jsonPath("$.carbohydrates", Float.class).value(productPostDTO.getCarbohydrates()))
                .andExpect(jsonPath("$.active").value("true"));
    }

    @Test
    @DisplayName("Test adding product with ConstraintViolationException")
    public void testAddProduct_ConstraintViolationException() throws Exception {
        
        productPostDTO.setId(-1L);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productPostDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Test adding product with IllegalRequestArgumentException")
    public void testAddProduct_IllegalRequestArgumentException() throws Exception {
        
        productPostDTO.setFatsecretId(0L);
        productPostDTO.setUserLogin("");
        
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productPostDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test changing product - Success")
    public void testChangeProducts() throws Exception {

        Product newProduct = products.get(1);
        List<ProductPutDTO> productsForChange = Arrays.asList(new ProductPutDTO(products.get(0).getId(), 
                                                            newProduct.getName(),
                                                            newProduct.getCalories(), 
                                                            newProduct.getProteins(), 
                                                            newProduct.getFats(), 
                                                            newProduct.getCarbohydrates()));
        List<Long> productIdsForDeletion = Arrays.asList(products.get(1).getId());
        ChangeProductsRequest changeProductsRequest = new ChangeProductsRequest(productsForChange, productIdsForDeletion, user.getLogin());
        ChangeProductsResponse response = new ChangeProductsResponse(productsForChange, productIdsForDeletion);

        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeProductsRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("Test changing product with ConstraintViolationException")
    public void testChangeProducts_ConstraintViolationException() throws Exception {

        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ChangeProductsRequest(
                                                        new ArrayList<>(),
                                                        new ArrayList<>(),
                                                        ""))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test change products with IllegalRequestArgumentException")
    public void testChangeProducts_IllegalRequestArgumentException() throws Exception {

        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ChangeProductsRequest(
                                                        new ArrayList<>(),
                                                        new ArrayList<>(),
                                                        user.getLogin()))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test сhange products with ResourceNotFoundException")
    public void testChangeProducts_ResourceNotFoundException() throws Exception {

        Product newProduct = products.get(1);
        List<ProductPutDTO> productsForChange = Arrays.asList(new ProductPutDTO(products.get(0).getId(), 
                                                            newProduct.getName(),
                                                            newProduct.getCalories(), 
                                                            newProduct.getProteins(), 
                                                            newProduct.getFats(), 
                                                            newProduct.getCarbohydrates()));
        List<Long> productIdsForDeletion = Arrays.asList(products.get(1).getId());
        ChangeProductsRequest changeProductsRequest = new ChangeProductsRequest(productsForChange, 
                                                                                productIdsForDeletion,
                                                                                "nonExistentLogin");
      
        mockMvc.perform(put("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeProductsRequest)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test get user products - Success")
    @ParameterizedTest
    @CsvSource({
            "1, 0, testUser",
            "100, 1000, 123456789012345678901234567890"
    })
    public void testGetUserProducts(int limit, int offset, String userLogin) throws Exception {

        user.setLogin(userLogin);
        userDAO.save(user);
              
        int hashSize = limit == 1 ? 1 : 0;
        mockMvc.perform(get("/products/user-products")
            .param("limit", String.valueOf(limit))
            .param("offset", String.valueOf(offset))
            .param("user-login", userLogin)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(instanceOf(List.class))))
            .andExpect(jsonPath("$", hasSize(hashSize)));    
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
    @DisplayName("Test get user products with IllegalRequestArgumentException")
    public void testGetUserProducts_IllegalRequestArgumentException() throws Exception {

        mockMvc.perform(get("/products/user-products")
                .param("limit", "10")
                .param("offset", "0")
                .param("user-login", "nonExistentLogin")
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

        user.setLogin(userLogin);
        userDAO.save(user);

        int hashSize = limit == 1 ? 1 : 0;

        mockMvc.perform(get("/products/search-products")
                .param("limit", String.valueOf(limit))
                .param("offset", String.valueOf(offset))
                .param("user-login", userLogin)
                .param("pattern", pattern)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(instanceOf(List.class))))
                .andExpect(jsonPath("$", hasSize(hashSize)));    
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
    @DisplayName("Test search products with IllegalRequestArgumentException")
    public void testSearchProducts_IllegalRequestArgumentException() throws Exception {

        mockMvc.perform(get("/products/search-products")
                .param("limit", "10")
                .param("offset", "0")
                .param("user-login", "testUser")
                .param("pattern", "nonExistentLogin")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
