package ru.unfatcrew.restcalorietracker.pojo.request;

import ru.unfatcrew.restcalorietracker.pojo.entity.Product;

import java.util.ArrayList;
import java.util.List;


public class ChangeProductsRequest {
    private List<Product> productsForChange;
    private List<Long> productIdsForDeletion;

    public ChangeProductsRequest() {
        this.productsForChange = new ArrayList<>();
        this.productIdsForDeletion = new ArrayList<>();
    }

    public ChangeProductsRequest(List<Product> productsForChange, List<Long> productIdsForDeletion) {
        this.productsForChange = productsForChange;
        this.productIdsForDeletion = productIdsForDeletion;
    }

    public List<Product> getProductsForChange() {
        return productsForChange;
    }

    public List<Long> getProductIdsForDeletion() {
        return productIdsForDeletion;
    }

    public void setProductsForChange(List<Product> productsForChange) {
        this.productsForChange = productsForChange;
    }

    public void setProductIdsForDeletion(List<Long> productIdsForDeletion) {
        this.productIdsForDeletion = productIdsForDeletion;
    }
}
