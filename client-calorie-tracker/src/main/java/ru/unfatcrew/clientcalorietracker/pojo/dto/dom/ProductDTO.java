package ru.unfatcrew.clientcalorietracker.pojo.dto.dom;

import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDTO {
    private List<Product> products;
    private List<Long> idsToDelete;

    public ProductDTO() {
        this.products = new ArrayList<>();
        this.idsToDelete = new ArrayList<>();
    }

    public ProductDTO(List<Product> products) {
        this.products = products;
        this.idsToDelete = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Long> getIdsToDelete() {
        return idsToDelete;
    }

    public void setIdsToDelete(List<Long> idsToDelete) {
        this.idsToDelete = idsToDelete;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProductDTO that = (ProductDTO) object;
        return Objects.equals(products, that.products) &&
                Objects.equals(idsToDelete, that.idsToDelete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, idsToDelete);
    }
}
