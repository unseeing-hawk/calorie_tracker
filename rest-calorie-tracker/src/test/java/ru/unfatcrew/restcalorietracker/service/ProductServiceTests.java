package ru.unfatcrew.restcalorietracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.core.io.Resource;

import ru.unfatcrew.restcalorietracker.dao.ProductDAO;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPostDTO;
import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPutDTO;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.pojo.request.ChangeProductsRequest;
import ru.unfatcrew.restcalorietracker.pojo.response.ChangeProductsResponse;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    private User user;
    private Product product;
    private ProductPostDTO productPostDTO;
    private ProductPutDTO productPutDTO;

    private static final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @BeforeEach
    public void setup() {
        user = new User("User Name",
                        "userlogin",
                        "{bcrypt}$2y$10$Sp/ZFpu6i5LJ7lgaEa09yOTgXp0PAA2xwkf5Gb51rwTGKZiJtNnQa",
                        90.17f);
        product  = new Product(0L, 
                                user,
                                "Random Product", 
                                200, 
                                15.5f, 
                                10.2f, 
                                30.8f, 
                                true);
        productPostDTO = new ProductPostDTO(user.getLogin(),
                                product.getName(),
                                product.getCalories(),
                                product.getProteins(),
                                product.getFats(),
                                product.getCarbohydrates());
        productPutDTO = new ProductPutDTO(1L,
                                product.getName(),
                                product.getCalories(),
                                product.getProteins(),
                                product.getFats(),
                                product.getCarbohydrates());
    }

    @Mock
    private UserDAO userDAO;

    @Mock
    private ProductDAO productDAO;

    @InjectMocks
    private ProductService productService;

    public static String loadJsonFromFile(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("Test adding user product – success")
    public void testAddUserProduct_ValidInput() {

        when(userDAO.findByLogin(user.getLogin())).thenReturn(user);
        when(productDAO.save(any(Product.class))).thenReturn(product);

        Product addedProduct = productService.addProduct(productPostDTO);

        assertNotNull(addedProduct);
        assertNotNull(addedProduct.getId());
        assertEquals(productPostDTO.getFatsecretId(), addedProduct.getFatsecretId());
        assertEquals(productPostDTO.getUserLogin(), addedProduct.getUser().getLogin());
        assertEquals(productPostDTO.getCalories(), addedProduct.getCalories());
        assertEquals(productPostDTO.getProteins(), addedProduct.getProteins());
        assertEquals(productPostDTO.getFats(), addedProduct.getFats());
        assertEquals(productPostDTO.getCarbohydrates(), addedProduct.getCarbohydrates());
    }

    @Test
    @DisplayName("Test adding fatsecret product – success")
    public void testAddFatSecProduct_ValidInput() {

        when(productDAO.findByFatsecretId(1L)).thenReturn(null);
        when(productDAO.save(any(Product.class))).thenReturn(product);

        productPostDTO.setFatsecretId(1L);
        productPostDTO.setUserLogin("");
        
        Product addedProduct = productService.addProduct(productPostDTO);

        assertNotNull(addedProduct);
        assertNotNull(addedProduct.getId());
        assertNull(addedProduct.getUser());
        assertEquals(productPostDTO.getFatsecretId(), addedProduct.getFatsecretId());
        assertEquals(productPostDTO.getCalories(), addedProduct.getCalories());
        assertEquals(productPostDTO.getProteins(), addedProduct.getProteins());
        assertEquals(productPostDTO.getFats(), addedProduct.getFats());
        assertEquals(productPostDTO.getCarbohydrates(), addedProduct.getCarbohydrates());
    }

    @Test
    @DisplayName("Test adding existing fatsecret product – success")
    public void testAddExistFatSecProduct_ValidInput() {
        product.setFatsecretId(1L);
        product.setUser(null);

        when(productDAO.findByFatsecretId(1L)).thenReturn(product);

        productPostDTO.setFatsecretId(1L);
        productPostDTO.setUserLogin("");
        
        Product fsProduct = productService.addProduct(productPostDTO);

        assertNotNull(fsProduct);
        assertNotNull(fsProduct.getId());
        assertNull(fsProduct.getUser());
        assertEquals(productPostDTO.getFatsecretId(), fsProduct.getFatsecretId());
        assertEquals(productPostDTO.getCalories(), fsProduct.getCalories());
        assertEquals(productPostDTO.getProteins(), fsProduct.getProteins());
        assertEquals(productPostDTO.getFats(), fsProduct.getFats());
        assertEquals(productPostDTO.getCarbohydrates(), fsProduct.getCarbohydrates());
    }

    @Test
    @DisplayName("Test adding product with null user and null FS id")
    public void testAddProduct_InvalidInput() {

        productPostDTO.setUserLogin("");

        assertThrows(IllegalRequestArgumentException.class, () -> productService.addProduct(productPostDTO));
    }

    @Test
    @DisplayName("Test adding product with null user and null FS id")
    public void testAddProduct_UnexistingUser() {

        when(userDAO.findByLogin(productPostDTO.getUserLogin())).thenReturn(null);

        assertThrows(IllegalRequestArgumentException.class, () -> productService.addProduct(productPostDTO));
    }

    @Test
    @DisplayName("Test changing products - success")
    public void testChangeProducts_Success() {
        Product product1 = new Product(0L, user, "Product 1", 100, 10.0f, 5.0f, 20.0f, true);
        Product product2 = new Product(0L, user, "Product 2", 150, 15.0f, 8.0f, 25.0f, true);
        Product product3 = new Product(0L, user, "Product 1", 100, 10.0f, 5.0f, 20.0f, true);
        Product product4 = new Product(0L, user, "Product 2", 150, 15.0f, 8.0f, 25.0f, true);

        List<Long> productIdsForDeletion = Arrays.asList(3L, 4L);
        product3.setId(3L);
        product4.setId(4L);

        when(productDAO.findById(1L)).thenReturn(Optional.of(product1));
        when(productDAO.findById(2L)).thenReturn(Optional.of(product2));
        when(productDAO.findById(3L)).thenReturn(Optional.of(product3));
        when(productDAO.findById(4L)).thenReturn(Optional.of(product4));

        when(productDAO.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productDAO.save(product3)).thenReturn(product3);
        when(productDAO.save(product4)).thenReturn(product4);

        ChangeProductsRequest request = new ChangeProductsRequest();
        request.setProductsForChange(List.of(
                new ProductPutDTO(1L, "Updated Product 1", 120, 12.0f, 6.0f, 22.0f),
                new ProductPutDTO(2L, "Updated Product 2", 170, 17.0f, 9.0f, 27.0f)
        ));
        request.setProductIdsForDeletion(productIdsForDeletion);
        request.setUserLogin(user.getLogin());

        when(userDAO.findByLogin(user.getLogin())).thenReturn(user);

        ChangeProductsResponse response = productService.changeProducts(request);

        List<ProductPutDTO> changedProducts = response.getProductsForChange();
        assertEquals(2, changedProducts.size());
        assertEquals("Updated Product 1", changedProducts.get(0).getName());
        assertEquals(120, changedProducts.get(0).getCalories());
        assertEquals(12.0f, changedProducts.get(0).getProteins());
        assertEquals(6.0f, changedProducts.get(0).getFats());
        assertEquals(22.0f, changedProducts.get(0).getCarbohydrates());

        assertEquals("Updated Product 2", changedProducts.get(1).getName());
        assertEquals(170, changedProducts.get(1).getCalories());
        assertEquals(17.0f, changedProducts.get(1).getProteins());
        assertEquals(9.0f, changedProducts.get(1).getFats());
        assertEquals(27.0f, changedProducts.get(1).getCarbohydrates());

        List<Long> deletedProductIds = response.getProductIdsForDeletion();
        assertEquals(2, deletedProductIds.size());
        assertTrue(deletedProductIds.contains(3L));
        assertTrue(deletedProductIds.contains(4L));
    }

    @Test
    @DisplayName("Test changing products with empty deleting/changing products")
    public void testChangeProducts_NoChanges() {
        
        ChangeProductsRequest request = new ChangeProductsRequest();
        request.setUserLogin(user.getLogin());

        assertThrows(IllegalRequestArgumentException.class, () -> productService.changeProducts(request));
    }

    @Test
    @DisplayName("Test changing products with no user found")
    public void testChangeProducts_NoUserFound() {
        
        ChangeProductsRequest request = new ChangeProductsRequest();
        request.setUserLogin("nonExistingUser");
        request.setProductIdsForDeletion(List.of(1L));

        when(userDAO.findByLogin("nonExistingUser")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> productService.changeProducts(request));
    }

    @Test
    @DisplayName("Test changing products with no product for delete found")
    public void testChangeProducts_DeleteProductNotFound() {
        
        ChangeProductsRequest request = new ChangeProductsRequest();
        request.setUserLogin(user.getLogin());
        request.setProductIdsForDeletion(List.of(1L));

        when(userDAO.findByLogin(user.getLogin())).thenReturn(user);
        when(productDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.changeProducts(request));
    }

    @Test
    @DisplayName("Test changing products with invalid user in product for delete")
    public void testChangeProducts_DeleteProductUserNotFound() {
        
        ChangeProductsRequest request = new ChangeProductsRequest();
        request.setUserLogin("some diff login");
        request.setProductIdsForDeletion(List.of(1L));

        when(userDAO.findByLogin("some diff login")).thenReturn(new User());
        when(productDAO.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ResourceNotFoundException.class, () -> productService.changeProducts(request));
    }

    @Test
    @DisplayName("Test changing products with already deleted product")
    public void testChangeProducts_DeleteDeletedProduct() {
        
        ChangeProductsRequest request = new ChangeProductsRequest();
        request.setUserLogin(user.getLogin());
        request.setProductIdsForDeletion(List.of(1L));

        when(userDAO.findByLogin(user.getLogin())).thenReturn(user);

        product.setActive(false);
        when(productDAO.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ResourceNotFoundException.class, () -> productService.changeProducts(request));
    }

    @Test
    @DisplayName("Test changing products that already in deleted product")
    public void testChangeProducts_changeDeletedProduct() {
        
        ChangeProductsRequest request = new ChangeProductsRequest();
        request.setUserLogin(user.getLogin());
        request.setProductIdsForDeletion(List.of(1L));
        request.setProductsForChange(List.of(productPutDTO));

        when(userDAO.findByLogin(user.getLogin())).thenReturn(user);
        when(productDAO.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ResourceNotFoundException.class, () -> productService.changeProducts(request));
    }

    @Test
    @DisplayName("Test changing products with no product for change found")
    public void testChangeProducts_ChangeProductNotFound() {
        
        ChangeProductsRequest request = new ChangeProductsRequest();
        request.setUserLogin(user.getLogin());
        request.setProductsForChange(List.of(productPutDTO));

        when(userDAO.findByLogin(user.getLogin())).thenReturn(user);
        when(productDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.changeProducts(request));
    }

    @Test
    @DisplayName("Test changing products with invalid user in product for change")
    public void testChangeProducts_ChangeProductUserNotFound() {
        
        ChangeProductsRequest request = new ChangeProductsRequest();
        request.setUserLogin("some diff login");
        request.setProductsForChange(List.of(productPutDTO));

        when(userDAO.findByLogin("some diff login")).thenReturn(new User());
        when(productDAO.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ResourceNotFoundException.class, () -> productService.changeProducts(request));
    }

    @Test
    @DisplayName("Test getting user products with valid input")
    public void testGetUserProducts_ValidInput() {
        
        when(userDAO.findByLogin(user.getLogin())).thenReturn(user);

        Page<Product> products = new PageImpl<>(List.of(new Product(), new Product()));
        when(productDAO.findByUserLoginAndIsActiveTrue(eq(user.getLogin()), any())).thenReturn(products);

        List<ProductPostDTO> result = productService.getUserProducts(10, 0, user.getLogin());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test getting user products with invalid user")
    public void testGetUserProducts_InvalidUser() {
        String userLogin = "unexisted login";

        when(userDAO.findByLogin(userLogin)).thenReturn(null);

        assertThrows(IllegalRequestArgumentException.class,
                () -> productService.getUserProducts(10, 0, userLogin));
    }

    @Test
    @DisplayName("Test searching products with valid input")
    public void testSearchProducts_ValidInput() throws IOException {
        String userLogin = user.getLogin();
        int limit = 10;
        int offset = 0;
        String pattern = "Product";

        when(userDAO.findByLogin(userLogin)).thenReturn(user);

        List<Product> userProductsDB = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Product product = new Product();
            product.setFatsecretId(0l);
            product.setUser(user);
            product.setName("Product " + i);
            userProductsDB.add(product);
        }
        when(productDAO.findByUserLoginAndNameContainingIgnoreCaseAndIsActiveTrue(userLogin, pattern))
               .thenReturn(userProductsDB);
        
        String fs_json = loadJsonFromFile("/fatsecret-products.json");
        List<ProductPostDTO> result;

        try(MockedStatic<FatsecretService> fatsecretService = Mockito.mockStatic(FatsecretService.class)) {
            fatsecretService.when(() -> FatsecretService.searchInFatsecretByPattern(pattern, "0"))
                            .thenReturn(fs_json);    

            assertEquals(FatsecretService.searchInFatsecretByPattern(pattern, "0"), fs_json);
            result = productService.searchProducts(limit, offset, userLogin, pattern);
        }

        assertNotNull(result);
        assertEquals(10, result.size()); 

        for (int i = 0; i < 5; i++) {
            assertEquals(result.get(i).getFatsecretId(), null);
            assertEquals(result.get(i).getUserLogin(), userLogin);
            assertEquals(result.get(i).getName(), userProductsDB.get(i).getName());
        }

        List<ProductPostDTO> fs_products = ProductService.convertJsonToProductPostDTO(fs_json);
        for (int i = 5, j = 0; i < 10; i++, j++) {
            assertEquals(result.get(i).getFatsecretId(),fs_products.get(j).getFatsecretId());
            assertEquals(result.get(i).getUserLogin(), "");
            assertEquals(result.get(i).getName(), fs_products.get(j).getName());
            assertEquals(result.get(i).getCalories(), fs_products.get(j).getCalories());
            assertEquals(result.get(i).getProteins(), fs_products.get(j).getProteins());
            assertEquals(result.get(i).getFats(), fs_products.get(j).getFats());
            assertEquals(result.get(i).getCarbohydrates(), fs_products.get(j).getCarbohydrates());
        }
    }

    @Test
    @DisplayName("Test searching products with invalid user")
    public void testSearchProducts_InvalidUser() {
        String userLogin = "unexisted login";

        when(userDAO.findByLogin(userLogin)).thenReturn(null);

        assertThrows(IllegalRequestArgumentException.class,
                () -> productService.searchProducts(10, 0, userLogin, "pattern"));
    }

    @Test
    public void testConvertJsonToProductPostDTO() throws IOException {
        String jsonString = loadJsonFromFile("/fatsecret-products.json");

        List<ProductPostDTO> result = ProductService.convertJsonToProductPostDTO(jsonString);

        assertNotNull(result);
        assertEquals(20, result.size());

        assertEquals("Natto (Fermented Soybean Product)", result.get(0).getName());
        assertEquals(3270, result.get(0).getFatsecretId());
        assertEquals(212, result.get(0).getCalories());
        assertEquals(11.00f, result.get(0).getFats());
        assertEquals(17.72f, result.get(0).getProteins());
        assertEquals(14.36f, result.get(0).getCarbohydrates());

        assertEquals("Liquid Egg Product", result.get(5).getName());
        assertEquals("Fruit Smoothie Drink (with Fruit Juice and Dairy Products)", result.get(19).getName());
        
    }
}

