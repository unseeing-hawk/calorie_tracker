package ru.unfatcrew.restcalorietracker.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPostDTO;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.request.ChangeProductsRequest;
import ru.unfatcrew.restcalorietracker.pojo.response.ChangeProductsResponse;
import ru.unfatcrew.restcalorietracker.service.ProductService;

@Validated
@RestController
@RequestMapping("/products")
public class ProductRestController {

    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@RequestBody ProductPostDTO product) {
        return productService.addProduct(product);
    }

    @PutMapping
    public ChangeProductsResponse changeProducts(@RequestBody ChangeProductsRequest changeProductsRequest) {
        return productService.changeProducts(changeProductsRequest);
    }

    @GetMapping("/user-products")
    public List<ProductPostDTO> getUserProducts(@RequestParam(name="limit", defaultValue="10")
                                                @Valid
                                                @Min(value = 1, message = "limit should not be less than 1")
                                                @Max(value = 100, message = "limit should not exceed 100")
                                                int limit, 
                                                @RequestParam(name="offset", defaultValue="0")
                                                @Valid
                                                @Min(value = 0, message = "offset should not be less than 0")
                                                int offset,
                                                @RequestParam(name="user-login", defaultValue="")
                                                @Size(min=8, max=30)
                                                @Valid
                                                String userLogin) {
        return productService.getUserProducts(limit, offset, userLogin);
    }

    @GetMapping("/search-products")
    public List<ProductPostDTO> searchProducts(@RequestParam(name="limit", defaultValue="10")
                                                @Valid
                                                @Min(value = 1, message = "limit should not be less than 1")
                                                @Max(value = 100, message = "limit should not exceed 100")
                                                int limit, 
                                                @RequestParam(name="offset", defaultValue="0")
                                                @Valid
                                                @Min(value = 0, message = "offset should not be less than 0")
                                                int offset,
                                                @RequestParam(name="user-login", defaultValue="")
                                                @Size(min=8, max=30)
                                                @Valid
                                                String userLogin,
                                                @RequestParam(name="pattern", defaultValue="")
                                                @Size(min=1, max=100)
                                                @Valid
                                                String pattern) {
        return productService.searchProducts(limit, offset, userLogin, pattern);
    }
}
