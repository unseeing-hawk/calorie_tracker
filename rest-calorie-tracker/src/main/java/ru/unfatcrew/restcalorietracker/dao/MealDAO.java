package ru.unfatcrew.restcalorietracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.unfatcrew.restcalorietracker.pojo.entity.Meal;

@Repository
public interface MealDAO extends JpaRepository<Meal, Long> {
}
