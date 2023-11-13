package ru.unfatcrew.restcalorietracker.dto;

public class ProductsDTO {
    private Long productId;
    private Long productFatsecretId;
    private Long productUserId;
    private String[] productName;
    private Integer productCalories;
    private Double productProteins;
    private Double productFats;
    private Double productCarbohydrates;
    private Boolean productIsActive;

    public Long getProductId() {
        return productId;
    }

    public Long getProductFatsecretId() {
        return productFatsecretId;
    }

    public Long getProductUserId() {
        return productUserId;
    }

    public String[] getProductName() {
        return productName;
    }

    public Integer getProductCalories() {
        return productCalories;
    }

    public Double getProductProteins() {
        return productProteins;
    }

    public Double getProductFats() {
        return productFats;
    }

    public Double getProductCarbohydrates() {
        return productCarbohydrates;
    }

    public Boolean getProductIsActive() {
        return productIsActive;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductFatsecretId(Long productFatsecretId) {
        this.productFatsecretId = productFatsecretId;
    }

    public void setProductUserId(Long productUserId) {
        this.productUserId = productUserId;
    }

    public void setProductName(String[] productName) {
        this.productName = productName;
    }

    public void setProductCalories(Integer productCalories) {
        this.productCalories = productCalories;
    }

    public void setProductProteins(Double productProteins) {
        this.productProteins = productProteins;
    }

    public void setProductFats(Double productFats) {
        this.productFats = productFats;
    }

    public void setProductCarbohydrates(Double productCarbohydrates) {
        this.productCarbohydrates = productCarbohydrates;
    }

    public void setProductIsActive(Boolean productIsActive) {
        this.productIsActive = productIsActive;
    }
}
