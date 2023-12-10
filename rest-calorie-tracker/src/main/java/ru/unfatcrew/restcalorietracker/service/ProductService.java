package ru.unfatcrew.restcalorietracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.dao.ProductDAO;
import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPostDTO;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.Violation;

@Service
public class ProductService {

    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    @Autowired
    public ProductService(UserDAO userDAO, ProductDAO productDAO) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
    }

    @Transactional
    public Product addProduct(@Valid ProductPostDTO productPostDTO) {
        List<Violation> violationList = new ArrayList<>();
        
        if (productPostDTO.getFatsecretId() == 0L && productPostDTO.getUserLogin() == "") {
         violationList.add(new Violation("addProduct.productPostDTO.userLoginAndFatsecretId",
                    "one value mast be non-zero"));
        }
    
        User user = null;
        if (productPostDTO.getFatsecretId() == 0L) {
            user = userDAO.findByLogin(productPostDTO.getUserLogin());
            if (user == null) {
                violationList.add(new Violation("addProduct.productPostDTO.userLogin",
                        "not found"));
            }
        } 

        if (!violationList.isEmpty()) {
            throw new IllegalRequestArgumentException(violationList);
        }

        Product product = new Product(productPostDTO.getFatsecretId(),
                                    user,
                                    productPostDTO.getName(),
                                    productPostDTO.getCalories(),
                                    productPostDTO.getProteins(),
                                    productPostDTO.getFats(),
                                    productPostDTO.getCarbohydrates(),
                                    true);

        productDAO.save(product);

        return product;
    }
}
