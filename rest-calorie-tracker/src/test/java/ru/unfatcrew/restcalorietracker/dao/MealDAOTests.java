package ru.unfatcrew.restcalorietracker.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.unfatcrew.restcalorietracker.pojo.dto.DaySummaryDTO;
import ru.unfatcrew.restcalorietracker.pojo.entity.Meal;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MealDAOTests {

    @Autowired
    private MealDAO mealDAO;

    @Autowired
    private MealTimeDAO mealTimeDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private UserDAO userDAO;

    private User user;
    private List<Product> productList;
    private List<MealTime> mealTimeList;
    private List<Meal> mealList;

    @BeforeEach
    public void setupDependencies() {
        user = new User("Beer Master",
                "beermastergood",
                "{bcrypt}$2a$10$RSvT7b55.bwDLBg4c1Rw/uWygwjfvxFw.MJo/ZlRDnEr1xSwPkJU2",
                49.54f);

        productList = List.of(new Product(0L,
                        user,
                        "Carbonara",
                        13,
                        15.67f,
                        48.89f,
                        31.01f,
                        true),
                new Product(0L,
                        user,
                        "Pepperoni",
                        71,
                        12.09f,
                        31.9f,
                        3.2f,
                        false));

        mealTimeList = List.of(new MealTime("Breakfast"),
                new MealTime("Lunch"),
                new MealTime("Dinner"));

        mealList = new ArrayList<>();
        mealList.add(new Meal(user,
                productList.get(0),
                126.3f,
                LocalDate.of(2024, 02, 12),
                mealTimeList.get(0)));
        mealList.add(new Meal(user,
                productList.get(1),
                15.39f,
                mealList.get(0).getDate(),
                mealTimeList.get(1)));

        userDAO.save(user);
        productDAO.saveAll(productList);
        mealTimeDAO.saveAll(mealTimeList);
    }

    @DisplayName("Find by date and login when database is empty")
    @Test
    public void givenDateAndLoginAndEmptyDatabase_whenFindByDateAndLogin_thenReturnEmptyList() {
        List<Meal> foundMeals = mealDAO.findByDateAndUserLogin(mealList.get(0).getDate(),
                mealList.get(0).getUser().getLogin());

        assertEquals(0, foundMeals.size());
    }

    @DisplayName("Find by not existing date and login")
    @Test
    public void givenNotExistingDateAndLogin_whenFindByDateAndLogin_thenReturnEmptyList() {
        mealDAO.saveAll(mealList);

        List<Meal> foundMeals = mealDAO.findByDateAndUserLogin(mealList.get(0).getDate().plusDays(1),
                mealList.get(0).getUser().getLogin());

        assertEquals(0, foundMeals.size());
    }

    @DisplayName("Find by date and not existing login")
    @Test
    public void givenDateAndNotExistingLogin_whenFindByDateAndLogin_thenReturnEmptyLis() {
        mealDAO.saveAll(mealList);

        List<Meal> foundMeals = mealDAO.findByDateAndUserLogin(mealList.get(0).getDate(),
                "bebebe");

        assertEquals(0, foundMeals.size());
    }

    @DisplayName("Find by date and login")
    @Test
    public void givenDateAndLogin_whenFindByDateAndLogin_thenReturnListOfMeals() {
        mealDAO.saveAll(mealList);

        List<Meal> foundMeals = mealDAO.findByDateAndUserLogin(mealList.get(0).getDate(),
                mealList.get(0).getUser().getLogin());

        assertEquals(mealList.size(), foundMeals.size());
    }

    @DisplayName("Find summary with invalid dates")
    @Test
    public void givenInvalidDatesAndLogin_whenFindSummary_thenReturnEmptyDaySummaryDtoList() {
        mealDAO.saveAll(mealList);

        List<DaySummaryDTO> daySummaryDTOList = mealDAO.findSummary(mealList.get(0).getDate().minusDays(1),
                mealList.get(0).getDate().minusDays(1),
                mealList.get(0).getUser().getLogin());

        assertEquals(0, daySummaryDTOList.size());
    }

    @DisplayName("Find summary with invalid login")
    @Test
    public void givenDatesAndInvalidLogin_whenFindSummary_thenReturnEmptyDaySummaryDtoList() {
        mealDAO.saveAll(mealList);

        List<DaySummaryDTO> daySummaryDTOList = mealDAO.findSummary(mealList.get(0).getDate(),
                mealList.get(0).getDate(),
                "bebebebe");

        assertEquals(0, daySummaryDTOList.size());
    }

    @DisplayName("Find summary")
    @Test
    public void givenStartDateAndEndDateAndLogin_whenFindSummary_thenReturnDaySummaryDtoObject() {
        mealDAO.saveAll(mealList);

        List<DaySummaryDTO> daySummaryDTOList = mealDAO.findSummary(mealList.get(0).getDate(),
                mealList.get(0).getDate(),
                mealList.get(0).getUser().getLogin());

        assertEquals(1, daySummaryDTOList.size());

        Meal meal1 = mealList.get(0);
        Meal meal2 = mealList.get(1);
        DaySummaryDTO foundDaySummaryDTO = daySummaryDTOList.get(0);
        assertAll("day summary dtos",
                () -> assertEquals(meal1.getDate(), foundDaySummaryDTO.getDate()),
                () -> assertEquals(meal1.getWeight() + meal2.getWeight(),
                        foundDaySummaryDTO.getWeight(),
                        0.001),
                () -> assertEquals(meal1.getWeight() / 100 * meal1.getProduct().getCalories()
                                + meal2.getWeight() / 100 * meal2.getProduct().getCalories(),
                        foundDaySummaryDTO.getCalories(),
                        0.001),
                () -> assertEquals(meal1.getWeight() / 100 * meal1.getProduct().getCarbohydrates()
                                + meal2.getWeight() / 100 * meal2.getProduct().getCarbohydrates(),
                        foundDaySummaryDTO.getCarbohydrates(),
                        0.001),
                () -> assertEquals(meal1.getWeight() / 100 * meal1.getProduct().getProteins()
                                + meal2.getWeight() / 100 * meal2.getProduct().getProteins(),
                        foundDaySummaryDTO.getProteins(),
                        0.001),
                () -> assertEquals(meal1.getWeight() / 100 * meal1.getProduct().getFats()
                                + meal2.getWeight() / 100 * meal2.getProduct().getFats(),
                        foundDaySummaryDTO.getFats(),
                        0.001));
    }
}
