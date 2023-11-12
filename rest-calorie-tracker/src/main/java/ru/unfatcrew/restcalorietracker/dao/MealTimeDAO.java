package ru.unfatcrew.restcalorietracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;

@Repository
public interface MealTimeDAO extends JpaRepository<MealTime, Long> {
}
