package ru.unfatcrew.clientcalorietracker.pojo.requests;

import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

public class ChangeProductsRequest {
    @NotNull
    private List<@Valid Product> productsForChange;

    @NotNull
    private List<@Positive Long> productIdsForDeletion;

    @NotBlank
    @Size(min=8, max=30)
    private String userLogin;

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

    public String getUserLogin() {
        return userLogin;
    }

    public void setProductsForChange(List<Product> productsForChange) {
        this.productsForChange = productsForChange;
    }

    public void setProductIdsForDeletion(List<Long> productIdsForDeletion) {
        this.productIdsForDeletion = productIdsForDeletion;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
