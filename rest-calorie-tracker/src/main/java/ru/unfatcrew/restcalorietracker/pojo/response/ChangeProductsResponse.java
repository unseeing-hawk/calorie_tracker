package ru.unfatcrew.restcalorietracker.pojo.response;

import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPutDTO;

import java.util.ArrayList;
import java.util.List;


public class ChangeProductsResponse {
    private List<ProductPutDTO> productsForChange;
    private List<Long> productIdsForDeletion;

    public ChangeProductsResponse() {
        this.productsForChange = new ArrayList<>();
        productIdsForDeletion = new ArrayList<>();
    }

    public ChangeProductsResponse(List<ProductPutDTO> productsForChange, List<Long> productIdsForDeletion) {
        this.productsForChange = productsForChange;
        this.productIdsForDeletion = productIdsForDeletion;
    }

    public List<ProductPutDTO> getProductsForChange() {
        return productsForChange;
    }

    public List<Long> getProductIdsForDeletion() {
        return productIdsForDeletion;
    }
    
    public void setProductIdsForDeletion(List<Long> productIdsForDeletion) {
        this.productIdsForDeletion = productIdsForDeletion;
    }

    public void setProductsForChange(List<ProductPutDTO> productsForChange) {
        this.productsForChange = productsForChange;
    }
}
