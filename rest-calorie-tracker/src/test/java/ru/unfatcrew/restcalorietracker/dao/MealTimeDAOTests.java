package ru.unfatcrew.restcalorietracker.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MealTimeDAOTests {

    @Autowired
    MealTimeDAO mealTimeDAO;

    private List<MealTime> mealTimes;

    @BeforeEach
    private void setupMealTimes() {
        mealTimes = List.of(new MealTime("breakfast"),
                new MealTime("lunch"),
                new MealTime("dinner"));
    }

    @DisplayName("Find meal time by not existing name")
    @Test
    public void givenNotExistingName_whenFindByName_thenReturnNull() {
        MealTime foundMealTime = mealTimeDAO.findByName(mealTimes.get(0).getName());

        assertNull(foundMealTime);
    }

    @DisplayName("Find meal time by existing name")
    @Test
    public void givenName_whenFindByName_thenReturnMealTimeObject() {
        MealTime mealTime = mealTimes.get(0);
        mealTimeDAO.save(mealTimes.get(0));

        MealTime foundMealTime = mealTimeDAO.findByName(mealTime.getName());
        assertAll("found meal time",
                () -> {
                    assertNotNull(foundMealTime);
                },
                () -> {
                    assertEquals(mealTime.getName(), foundMealTime.getName());
                });
    }
}
