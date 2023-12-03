package ru.unfatcrew.restcalorietracker.pojo.response;

import ru.unfatcrew.restcalorietracker.pojo.dto.ProductPostDTO;

import java.util.ArrayList;
import java.util.List;


public class ChangeProductsResponse {
    private List<ProductPostDTO> productsForChange;

    public ChangeProductsResponse() {
        this.productsForChange = new ArrayList<>();
    }

    public ChangeProductsResponse(List<ProductPostDTO> productsForChange, List<ProductPostDTO> productsForDeletion) {
        this.productsForChange = productsForChange;
    }

    public List<ProductPostDTO> getProductsForChange() {
        return productsForChange;
    }
    
    public void setProductsForChange(List<ProductPostDTO> productsForChange) {
        this.productsForChange = productsForChange;
    }
}
