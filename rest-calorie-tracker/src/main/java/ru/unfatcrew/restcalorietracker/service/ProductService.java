package ru.unfatcrew.restcalorietracker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
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
        } else {
            Product fatsecretProduct = productDAO.findByFatsecretId(productPostDTO.getFatsecretId());
            if (fatsecretProduct != null) {
                return fatsecretProduct;
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

    public List<ProductPostDTO> getUserProducts(@Valid int limit, 
                                                @Valid int offset, 
                                                @Valid String userLogin) {
        List<Violation> violationList = new ArrayList<>();
        
        User user = userDAO.findByLogin(userLogin);
        if (user == null) {
            violationList.add(new Violation("getUserProducts.productPostDTO.user-login",
                    "not found"));
        }

        if (!violationList.isEmpty()) {
            throw new IllegalRequestArgumentException(violationList);
        }

        Pageable pageable = PageRequest.of(offset, limit);
        Page<Product> productsPage = productDAO.findByUserLoginAndIsActiveTrue(userLogin, pageable);
        
        List<ProductPostDTO> productPostDTOList = convertPageToProductPostDTOList(productsPage, userLogin);

        return productPostDTOList;
    }

    public List<ProductPostDTO> searchProducts(@Valid int limit, 
                                                @Valid int offset, 
                                                @Valid String userLogin,
                                                @Valid String pattern) {
        List<Violation> violationList = new ArrayList<>();
        
        User user = userDAO.findByLogin(userLogin);
        if (user == null) {
            violationList.add(new Violation("getUserProducts.productPostDTO.user-login",
                    "not found"));
        }

        if (!violationList.isEmpty()) {
            throw new IllegalRequestArgumentException(violationList);
        }

        List<ProductPostDTO> products = new ArrayList<>();
        int startIndex = limit * offset;
        int endIndex = limit * offset + limit; //includ last element

        List<Product> userProductsDB = productDAO.findByUserLoginAndNameContainingIgnoreCaseAndIsActiveTrue(userLogin, pattern);
        int countUserProducts = userProductsDB.size();

        if (startIndex < countUserProducts){
            int userEndIndex = endIndex;

            if (endIndex > countUserProducts) {
                userEndIndex = countUserProducts;
            }

            for (int i = startIndex; i < userEndIndex; i++) {
                Product userProduct = userProductsDB.get(i);

                products.add(new ProductPostDTO(
                    userProduct.getUser().getLogin(),
                    userProduct.getName(),
                    userProduct.getCalories(),
                    userProduct.getProteins(),
                    userProduct.getFats(),
                    userProduct.getCarbohydrates()
                ));
            }
        } 
        
        final int FS_LIMIT = 20;
        if (countUserProducts < endIndex) {
            startIndex -= countUserProducts;
            if (startIndex < 0) startIndex = 0;
            endIndex -= countUserProducts;

            int pageNumber_first = startIndex / FS_LIMIT;
            int pageNumber_last = (int) Math.ceil((double) endIndex / FS_LIMIT); 

            List<ProductPostDTO> fatsecretProducts = new ArrayList<>();
            for (int i = pageNumber_first; i <= pageNumber_last; i++) {
                String fatsecretRequestBody = FatsecretService.searchInFatsecretByPattern(pattern, String.valueOf(i));
                fatsecretProducts.addAll(convertJsonToProductPostDTO(fatsecretRequestBody));
            }

            for (int i = startIndex % FS_LIMIT; i < (fatsecretProducts.size() - (FS_LIMIT - endIndex % FS_LIMIT)); i++) {
                products.add(fatsecretProducts.get(i));
            }
        }

        return products;
    }

    private static List<ProductPostDTO> convertJsonToProductPostDTO(String jsonString) {
        List<ProductPostDTO> productPostDTOList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            JsonNode foodsNode = jsonNode.path("foods").path("food");
            for (JsonNode foodNode : foodsNode) {
                Long fatsecretId = foodNode.path("food_id").asLong();
                String name = foodNode.path("food_name").asText();

                Integer calories = foodNode.path("food_description").asText().contains("Calories") ?
                        Math.round(extractValue("Calories", foodNode.path("food_description"))) : 0;
               
                Float proteins = extractValue("Protein", foodNode.path("food_description"));
                Float fats = extractValue("Fat", foodNode.path("food_description"));
                Float carbohydrates = extractValue("Carbs", foodNode.path("food_description"));

                ProductPostDTO productPostDTO = new ProductPostDTO(
                        fatsecretId, name, calories, proteins, fats, carbohydrates
                );

                productPostDTOList.add(productPostDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return productPostDTOList;
    }

    private static Float extractValue(String key, JsonNode descriptionNode) {
        String description = descriptionNode.asText();
        String valueString = "";

        int startIdx = description.indexOf(key + ":");
        if (startIdx != -1) {
            int endIdx = (key == "Calories") ? description.indexOf("k", startIdx) : description.indexOf("g", startIdx);
            if (endIdx != -1) {
                valueString = description.substring(startIdx + key.length() + 1, endIdx).trim();
    
                valueString = valueString.replaceAll("[^\\d.]", "");
            }
        }
    
        return valueString.isEmpty() ? 0.0f : Float.parseFloat(valueString);
    }

    private static  List<ProductPostDTO> convertPageToProductPostDTOList(Page<Product> productsPage, String userLogin) {
        return productsPage.getContent().stream()
                .map(product -> {
                    ProductPostDTO productPostDTO = new ProductPostDTO();
                    productPostDTO.setId(product.getId());
                    productPostDTO.setUserLogin(userLogin);
                    productPostDTO.setName(product.getName());
                    productPostDTO.setCalories(product.getCalories());
                    productPostDTO.setProteins(product.getProteins());
                    productPostDTO.setFats(product.getFats());
                    productPostDTO.setCarbohydrates(product.getCarbohydrates());
                    return productPostDTO;
                })
                .collect(Collectors.toList());
    }
    
}
