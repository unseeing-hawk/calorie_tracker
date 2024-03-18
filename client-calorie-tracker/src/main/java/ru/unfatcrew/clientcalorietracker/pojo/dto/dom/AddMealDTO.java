package ru.unfatcrew.clientcalorietracker.pojo.dto.dom;

import ru.unfatcrew.clientcalorietracker.pojo.dto.SearchProductDTO;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class AddMealDTO {
    public static class ProductToAdd {
        private SearchProductDTO product;
        private Float weight;

        public ProductToAdd() {

        }

        public ProductToAdd(SearchProductDTO product) {
            this.product = product;
            this.weight = 0.0f;
        }

        public ProductToAdd(SearchProductDTO product, float weight) {
            this.product = product;
            this.weight = weight;
        }

        public SearchProductDTO getProduct() {
            return product;
        }

        public void setProduct(SearchProductDTO product) {
            this.product = product;
        }

        public Float getWeight() {
            return weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        public String toEncodedJson() {
            return Base64.getEncoder().encodeToString(toString().getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String toString() {
            return '{' +
                    "\"product\":" + product + ',' +
                    "\"weight\":" + weight +
                    '}';
        }
    }

    private List<SearchProductDTO> searchProductList;
    private List<SearchProductDTO> productsToSelect;
    private List<ProductToAdd> productsToAdd;
    private String date;
    private String mealTime;

    public AddMealDTO() {
        this.searchProductList = new ArrayList<>();
        this.productsToSelect = new ArrayList<>();
        this.productsToAdd = new ArrayList<>();
    }

    public List<SearchProductDTO> getSearchProductList() {
        return searchProductList;
    }

    public void setSearchProductList(List<SearchProductDTO> searchProductList) {
        this.searchProductList = searchProductList;
    }

    public List<SearchProductDTO> getProductsToSelect() {
        return productsToSelect;
    }

    public void setProductsToSelect(List<SearchProductDTO> productsToSelect) {
        this.productsToSelect = productsToSelect;
    }

    public List<ProductToAdd> getProductsToAdd() {
        return productsToAdd;
    }

    public void setProductsToAdd(List<ProductToAdd> productsToAdd) {
        this.productsToAdd = productsToAdd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AddMealDTO that = (AddMealDTO) object;
        return Objects.equals(searchProductList, that.searchProductList) &&
                Objects.equals(productsToSelect, that.productsToSelect) &&
                Objects.equals(productsToAdd, that.productsToAdd) &&
                Objects.equals(date, that.date) &&
                Objects.equals(mealTime, that.mealTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchProductList, productsToSelect, productsToAdd, date, mealTime);
    }
}
