package ru.unfatcrew.restcalorietracker.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPostDTO;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.service.ProductService;

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
}
