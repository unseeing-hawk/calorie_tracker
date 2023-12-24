package ru.unfatcrew.restcalorietracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.unfatcrew.restcalorietracker.pojo.dto.DaySummaryDTO;
import ru.unfatcrew.restcalorietracker.pojo.entity.Meal;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealDAO extends JpaRepository<Meal, Long> {
    @Query("SELECT m FROM Meal m WHERE date = :date and user.login = :userLogin")
    List<Meal> findByDateAndUserLogin(@Param("date") LocalDate date, @Param("userLogin") String userLogin);

    @Query("SELECT new ru.unfatcrew.restcalorietracker.pojo.dto.DaySummaryDTO(" +
                    "c.date, SUM(c.weight), SUM(c.weight / 100 * c.product.calories), " +
                    "SUM(c.weight / 100 * c.product.proteins), SUM(c.weight / 100 * c.product.fats), " +
                    "SUM(c.weight / 100 * c.product.carbohydrates)) " +
            "FROM Meal as c " +
            "WHERE c.user.login = :userLogin " +
            "AND c.date >= :startDate " +
            "AND c.date <= :endDate " +
            "GROUP BY c.date " +
            "ORDER BY c.date")
    List<DaySummaryDTO> findSummary(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("userLogin") String userLogin);
}
