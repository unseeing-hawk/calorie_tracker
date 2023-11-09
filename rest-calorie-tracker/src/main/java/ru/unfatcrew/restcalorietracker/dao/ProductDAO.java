package ru.unfatcrew.restcalorietracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product, Long> {
}
