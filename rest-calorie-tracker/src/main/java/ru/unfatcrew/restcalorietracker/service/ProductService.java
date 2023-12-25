package ru.unfatcrew.restcalorietracker.service;

import java.io.IOException;
import java.util.*;
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

import org.springframework.validation.annotation.Validated;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.dao.ProductDAO;
import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPostDTO;
import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPutDTO;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.pojo.request.ChangeProductsRequest;
import ru.unfatcrew.restcalorietracker.pojo.response.ChangeProductsResponse;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.Violation;

@Validated
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

    @Transactional
    public ChangeProductsResponse changeProducts(@Valid ChangeProductsRequest changeProductsRequest) {
        List<Violation> violationList = new ArrayList<>();
        List<ProductPutDTO> productsForChange = changeProductsRequest.getProductsForChange();
        List<Long> productsIdsForDeletion = changeProductsRequest.getProductIdsForDeletion();

        if (productsForChange.isEmpty() && productsIdsForDeletion.isEmpty()) {
            violationList.add(new Violation("changeProducts.changeProductsRequest.productsForChange-productsIdsForDeletion",
                    "cannot both be empty"));
        }

        if (!violationList.isEmpty()) {
            throw new IllegalRequestArgumentException(violationList);
        }
        
        String userLogin = changeProductsRequest.getUserLogin();
        if (userDAO.findByLogin(userLogin) == null) {
            violationList.add(new Violation("changeProducts.changeProductsRequest.user-login",
                    "not found"));
        }

        if (!violationList.isEmpty()) {
            throw new ResourceNotFoundException(violationList);
        }

        HashMap<Long, Product> productsForDeletionHashMap = new HashMap<>();
        for (int i = 0; i < productsIdsForDeletion.size(); i++) {
            if (productsForDeletionHashMap.containsKey(productsIdsForDeletion.get(i))) {
                violationList.add(new Violation("changeProducts.changeProductsRequest.productsIdsForDeletion["
                        + Integer.toString(i)
                        + "].<list element>"
                        , "already exists in productsIdsForDeletion"));
            }

            Optional<Product> optionalProduct = productDAO.findById(productsIdsForDeletion.get(i));
            Product product = null;
            if (optionalProduct.isEmpty()) {
                violationList.add(new Violation("changeProducts.changeProductsRequest.productsIdsForDeletion["
                        + Integer.toString(i)
                        + "].<list element>"
                        , "not found"));
            }  else {
                product = optionalProduct.get();
            }

            if (product != null
                    && !product.getUser().getLogin().equals(userLogin)) {
                violationList.add(new Violation("changeProducts.changeProductsRequest.productsIdsForDeletion["
                        + Integer.toString(i)
                        + "].<list element>-userLogin"
                        , "not connected"));
            }

            if (product != null
                    && !product.isActive()) {
                violationList.add(new Violation("changeProducts.changeProductsRequest.productsIdsForDeletion["
                        + Integer.toString(i)
                        + "].<list element>-isActive"
                        , "already deleted"));
            }

            if (product != null) {
                product.setActive(false);
                productsForDeletionHashMap.put(product.getId(), product);
            }
        }

        HashMap<Long, Product> productsForChangeHashMap = new HashMap<>();
        for (int i = 0; i < productsForChange.size(); i++) {
            ProductPutDTO productForChange = productsForChange.get(i);
            long id = productForChange.getId();
            if (productsForChangeHashMap.containsKey(id)) {
                violationList.add(new Violation("changeProducts.changeProductsRequest.productsForChange["
                        + Integer.toString(i)
                        + "].id"
                        , "already exists in productsForChange"));
            }

            if (productsForDeletionHashMap.containsKey(id)) {
                violationList.add(new Violation("changeProducts.changeProductsRequest.productsForChange["
                        + Integer.toString(i)
                        + "].id"
                        , "already exists in productsForDeletion"));
            }

            Optional<Product> optionalProduct = productDAO.findById(id);
            Product product = null;
            if (optionalProduct.isEmpty()) {
                violationList.add(new Violation("changeProducts.changeProductsRequest.productsForChange["
                        + Integer.toString(i)
                        + "].id"
                        , "not found"));
            } else {
                product = optionalProduct.get();
            }

            if (product != null
                    && !product.getUser().getLogin().equals(userLogin)) {
                violationList.add(new Violation("changeProducts.changeProductsRequest.productsForChange["
                        + Integer.toString(i)
                        + "].id-userLogin"
                        , "not connected"));
            }
             if (product != null
                    && !product.isActive()) {
                violationList.add(new Violation("changeProducts.changeProductsRequest.productsForChange["
                        + Integer.toString(i)
                        + "].<list element>-isActive"
                        , "this product deleted"));
            }

            if (product != null) {
                product.setName(productForChange.getName());
                product.setCalories(productForChange.getCalories());
                product.setProteins(productForChange.getProteins());
                product.setFats(productForChange.getFats());
                product.setCarbohydrates(productForChange.getCarbohydrates());
                productsForChangeHashMap.put(id, product);
            }
        }

        if (!violationList.isEmpty()) {
            throw new ResourceNotFoundException(violationList);
        }

        List<ProductPutDTO> productsForChangeList = new ArrayList<>();
        for (Map.Entry<Long, Product> entry : productsForChangeHashMap.entrySet()) {
            Product product = entry.getValue();
            productDAO.save(product);
            productsForChangeList.add(new ProductPutDTO(product.getId(),
                                                    product.getName(),
                                                    product.getCalories(),
                                                    product.getProteins(),
                                                    product.getFats(),
                                                    product.getCarbohydrates()));
        }

        List<Long> productsForDeletionList = new ArrayList<>();
        for (Map.Entry<Long, Product> entry : productsForDeletionHashMap.entrySet()) {
            Product product = entry.getValue();
            productDAO.save(product);
            productsForDeletionList.add(product.getId());
        }

        return new ChangeProductsResponse(productsForChangeList, productsForDeletionList);
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
                    userProduct.getId(),
                    userProduct.getUser().getLogin(),
                    userProduct.getName(),
                    userProduct.getCalories(),
                    userProduct.getProteins(),
                    userProduct.getFats(),
                    userProduct.getCarbohydrates()
                ));
            }
        } 
        
        final String REGEX = "^[A-Za-z ]*$";
        final int FS_LIMIT = 20;
        if (countUserProducts < endIndex && pattern.matches(REGEX)) {
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
