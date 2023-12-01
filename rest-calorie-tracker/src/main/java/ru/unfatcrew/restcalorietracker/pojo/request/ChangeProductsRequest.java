package ru.unfatcrew.restcalorietracker.pojo.request;

import ru.unfatcrew.restcalorietracker.pojo.dto.ProductDTO;

import java.util.ArrayList;
import java.util.List;


public class ChangeProductsRequest {
    private List<ProductDTO> productsForChange;
    private List<Long> productIdsForDeletion;

    public ChangeProductsRequest() {
        this.productsForChange = new ArrayList<>();
        this.productIdsForDeletion = new ArrayList<>();
    }

    public ChangeProductsRequest(List<ProductDTO> productsForChange, List<Long> productIdsForDeletion) {
        this.productsForChange = productsForChange;
        this.productIdsForDeletion = productIdsForDeletion;
    }

    public List<ProductDTO> getProductsForChange() {
        return productsForChange;
    }

    public List<Long> getProductIdsForDeletion() {
        return productIdsForDeletion;
    }

    public void setProductsForChange(List<ProductDTO> productsForChange) {
        this.productsForChange = productsForChange;
    }

    public void setProductIdsForDeletion(List<Long> productIdsForDeletion) {
        this.productIdsForDeletion = productIdsForDeletion;
    }
}
