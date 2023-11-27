package ru.unfatcrew.restcalorietracker.response;

import ru.unfatcrew.restcalorietracker.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;


public class ChangeProductsResponse {
    private List<ProductDTO> productsForChange;
    private List<ProductDTO> productsForDeletion;

    public ChangeProductsResponse() {
        this.productsForChange = new ArrayList<>();
        this.productsForDeletion = new ArrayList<>();
    }

    public ChangeProductsResponse(List<ProductDTO> productsForChange, List<ProductDTO> productsForDeletion) {
        this.productsForChange = productsForChange;
        this.productsForDeletion = productsForDeletion;
    }

    public List<ProductDTO> getProductsForChange() {
        return productsForChange;
    }

    public List<ProductDTO> getProductsForDeletion() {
        return productsForDeletion;
    }
    
    public void setProductsForChange(List<ProductDTO> productsForChange) {
        this.productsForChange = productsForChange;
    }

    public void setProductsForDeletion(List<ProductDTO> productsForDeletion) {
        this.productsForDeletion = productsForDeletion;
    }
}
