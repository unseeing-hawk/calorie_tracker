package ru.unfatcrew.restcalorietracker.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProductDAOTests {

    private User user;
    private Product product;

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ProductDAO productDAO;

    @BeforeEach
    public void setup() {
        user = new User("User Name",
                                "userlogin",
                                "{bcrypt}$2y$10$Sp/ZFpu6i5LJ7lgaEa09yOTgXp0PAA2xwkf5Gb51rwTGKZiJtNnQa",
                                90.17f);
        userDAO.save(user); 
        product  = new Product(
                                123456L, 
                                user,
                                "Random Product", 
                                200, 
                                15.5f, 
                                10.2f, 
                                30.8f, 
                                true);
        productDAO.save(product);
    }

    @Test
    @DisplayName("Find product by Fatsecret ID")
    public void testFindByFatsecretId() {
        long fatsecretId = product.getFatsecretId();
        long diffFatSecID = fatsecretId > 0 ? fatsecretId - 1 : 1L;

        Product anotheFSIDProduct = new Product();
        anotheFSIDProduct.setName("test product");
        anotheFSIDProduct.setFatsecretId(diffFatSecID);
        productDAO.save(anotheFSIDProduct);

        Product foundProduct = productDAO.findByFatsecretId(fatsecretId);

        assertNotNull(foundProduct);
        assertEquals(fatsecretId, foundProduct.getFatsecretId());
    }

    @Test
    @DisplayName("Find products by user login and active status")
    public void testFindByUserLoginAndIsActiveTrue() {
        String userLogin = user.getLogin();
        
        Product disableProduct = new Product();
        disableProduct.setName("test product");
        disableProduct.setUser(user);
        disableProduct.setActive(false); 
        productDAO.save(disableProduct);

        Page<Product> productsPage = productDAO.findByUserLoginAndIsActiveTrue(userLogin, PageRequest.of(0, 10));

        assertEquals(1, productsPage.getTotalElements());
        assertTrue(productsPage.getContent().stream().allMatch(Product::isActive));
    }

    @Test
    @DisplayName("Find products by user login, name pattern and active status")
    public void testFindByUserLoginAndNameContainingIgnoreCaseAndIsActiveTrue() {
        String userLogin = user.getLogin();
        String productNamePattern = product.getName().substring(0, 3);
        String nameContainPattern = productNamePattern + "test";

        // must to find
        Product product1 = new Product();
        product1.setUser(user);
        product1.setName(nameContainPattern);
        product1.setActive(true);
        productDAO.save(product1);

        User testUser = new User(user.getName(),
                            userLogin + "2",
                            user.getPassword(),
                            user.getWeight());
        userDAO.save(testUser);
        
        Product product2 = new Product();
        product2.setUser(testUser); // another user
        product2.setName(productNamePattern);
        product2.setActive(true);
        productDAO.save(product2);

        Product product3 = new Product();
        product3.setUser(user);
        product3.setName(productNamePattern);
        product3.setActive(false); // deactivated product
        productDAO.save(product3);


        List<Product> products = productDAO.findByUserLoginAndNameContainingIgnoreCaseAndIsActiveTrue(userLogin, productNamePattern);

        assertEquals(2, products.size());
        assertEquals(product.getName(), products.get(0).getName());
        assertEquals(nameContainPattern, products.get(1).getName());
    }

}
