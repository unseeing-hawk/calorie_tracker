package ru.unfatcrew.restcalorietracker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.unfatcrew.restcalorietracker.dao.MealDAO;
import ru.unfatcrew.restcalorietracker.dao.MealTimeDAO;
import ru.unfatcrew.restcalorietracker.dao.ProductDAO;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDataDto;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDto;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPutDataDto;
import ru.unfatcrew.restcalorietracker.pojo.entity.Meal;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.pojo.request.ChangeMealsRequest;
import ru.unfatcrew.restcalorietracker.validation.DateValidationUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MealRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private MealDAO mealDAO;

    @Autowired
    private MealTimeDAO mealTimeDAO;

    private User user;
    private List<Product> productList;
    private List<MealTime> mealTimeList;
    private List<Meal> mealList;
    private MealPostDto mealPostDto;

    @BeforeEach
    private void setup() {
        mealDAO.deleteAll();
        mealTimeDAO.deleteAll();
        productDAO.deleteAll();
        userDAO.deleteAll();

        user = new User("Beer Master",
                "beermastergood",
                "{bcrypt}$2a$10$RSvT7b55.bwDLBg4c1Rw/uWygwjfvxFw.MJo/ZlRDnEr1xSwPkJU2",
                49.54f);
        userDAO.save(user);

        productList = List.of(new Product(0l,
                        user,
                        "Carbonara",
                        13,
                        15.67f,
                        48.89f,
                        31.01f,
                        true),
                new Product(6L,
                        user,
                        "Pepperoni",
                        71,
                        12.09f,
                        31.9f,
                        3.2f,
                        true));
        productDAO.saveAll(productList);

        mealTimeList = List.of(new MealTime("Breakfast"), new MealTime("Lunch"), new MealTime("Dinner"));
        mealTimeDAO.saveAll(mealTimeList);

        mealPostDto = new MealPostDto(List.of(new MealPostDataDto(productList.get(0).getId(), 123.21f),
                new MealPostDataDto(productList.get(1).getId(), 341.21f)),
                user.getLogin(),
                LocalDate.now().format(DateValidationUtils.DateFormat),
                mealTimeList.get(1).getName());

        LocalDate date = LocalDate.now();
        mealList = List.of(new Meal(user, productList.get(0), 13.48f, date, mealTimeList.get(0)),
                new Meal(user, productList.get(1), 17.68f, date, mealTimeList.get(1)));
    }

    @DisplayName("Add meals with invalid request body")
    @Test
    public void givenInvalidMealPostDto_whenAddMeals_thenThrowException() throws Exception {
        mealPostDto.setDate("fesfef");

        ResultActions response = mockMvc.perform(post("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealPostDto)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Add meals where some of request body's components do not exist")
    @Test
    public void givenMealPostDtoWithNotExistingComponents_whenAddMeals_thenThrowException() throws Exception {
        mealPostDto.setMealTime("yhuihuihy");

        ResultActions response = mockMvc.perform(post("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealPostDto)));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Add meals")
    @Test
    public void givenChangeMealsRequest_whenAddMeals_thenReturnMealGetDto() throws Exception {
        ResultActions response = mockMvc.perform(post("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealPostDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.mealGetDataList", is(instanceOf(ArrayList.class))))
                .andExpect(jsonPath("$.mealGetDataList", hasSize(2)))
                .andExpect(jsonPath("$.userLogin", is(mealPostDto.getUserLogin())))
                .andExpect(jsonPath("$.date", is(LocalDate.parse(mealPostDto.getDate(), DateValidationUtils.DateFormat).toString())));
    }

    @DisplayName("Change meals with validated but invalid request body")
    @Test
    public void givenValidatedButInvalidChangeMealsRequest_whenChangeMeals_thenThrowException() throws Exception {
        mealDAO.saveAll(mealList);
        ChangeMealsRequest changeMealsRequest = new ChangeMealsRequest(new ArrayList<>(), new ArrayList<>(), user.getLogin());

        ResultActions response = mockMvc.perform(put("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeMealsRequest)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Change meals where some of request body's components do not exist")
    @Test
    public void givenChangeMealsRequestWithNotExistingComponents_whenChangeMeals_thenThrowException() throws Exception {
        mealDAO.saveAll(mealList);
        ChangeMealsRequest changeMealsRequest = new ChangeMealsRequest(new ArrayList<>(),
                List.of(mealList.get(1).getId() + 1),
                user.getLogin());

        ResultActions response = mockMvc.perform(put("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeMealsRequest)));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Change meals")
    @Test
    public void givenChangeMealsRequest_whenChangeMeals_thenThrowException() throws Exception{
        mealDAO.saveAll(mealList);
        ChangeMealsRequest changeMealsRequest = new ChangeMealsRequest(List.of(new MealPutDataDto(
                        mealList.get(0).getId(),
                        17.8f,
                        mealTimeList.get(2).getName())),
                List.of(mealList.get(1).getId()),
                user.getLogin());

        ResultActions response = mockMvc.perform(put("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeMealsRequest)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.mealsForChange.userLogin", is(user.getLogin())))
                .andExpect(jsonPath("$.mealsForChange.mealGetDataList", hasSize(1)))
                .andExpect(jsonPath("$.mealsForDeletion.userLogin", is(user.getLogin())))
                .andExpect(jsonPath("$.mealsForDeletion.mealGetDataList", hasSize(1)));
    }

    @DisplayName("Get meal times")
    @Test
    public void givenNothing_whenGetMealtimes_thenReturnListOfMealtimes() throws Exception {
        ResultActions response = mockMvc.perform(get("/meals/mealtimes"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", is(instanceOf(List.class))))
                .andExpect(jsonPath("$", hasSize(mealTimeList.size())));
    }

    @DisplayName("Get meals with invalid parameters")
    @ParameterizedTest
    @CsvFileSource(resources="/invalid-parameters-get-meals.csv",
            numLinesToSkip=1,
            delimiter=';',
            nullValues={"NULL"})
    public void givenInvalidDateOrLogin_whenGetMeals_thenThrowException(String stringDate, String login) throws Exception {
        String date = null;
        if (stringDate != null) {
            if (stringDate.equals("MAX")) {
                date = LocalDate.now().format(DateValidationUtils.DateFormat);
            } else if (stringDate.equals("OVERMAX")) {
                date = LocalDate.now().plusDays(1).format(DateValidationUtils.DateFormat);
            } else if (stringDate.equals("OVERMIN")) {
                date = LocalDate.now().minusYears(10).format(DateValidationUtils.DateFormat);
            } else {
                date = stringDate;
            }
        }

        ResultActions response = mockMvc.perform(get("/meals")
                .param("date", date)
                .param("user-login", login));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Get meals with not existing login")
    @Test
    public void givenDateAndNotExistingLogin_whenGetMeals_thenThrowException() throws Exception {
        ResultActions response = mockMvc.perform(get("/meals")
                .param("date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("user-login", "bebebebe"));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Get meals")
    @Test
    public void givenDateAndLogin_whenGetMeals_thenReturnMealGetDto() throws Exception{
        mealDAO.saveAll(mealList);

        ResultActions response = mockMvc.perform(get("/meals")
                .param("date", mealList.get(0).getDate().format(DateValidationUtils.DateFormat))
                .param("user-login", user.getLogin()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.userLogin", is(user.getLogin())))
                .andExpect(jsonPath("$.mealGetDataList", hasSize(mealList.size())));
    }

    @DisplayName("Get summary with invalid parameters")
    @ParameterizedTest
    @CsvFileSource(resources="/invalid-parameters-get-summary.csv",
            numLinesToSkip=1,
            delimiter=';',
            nullValues={"NULL"})
    public void givenInvalidStartDateOrEndDateOrLogin_whenGetSummary_thenReturnListOfDaySummaryDto(String stringStartDate,
                                                                                                   String stringEndDate,
                                                                                                   String login) throws Exception{
        String startDate = null;
        if (stringStartDate != null) {
            if (stringStartDate.equals("MAX")) {
                startDate = LocalDate.now().format(DateValidationUtils.DateFormat);
            } else if (stringStartDate.equals("OVERMAX")) {
                startDate = LocalDate.now().plusDays(1).format(DateValidationUtils.DateFormat);
            } else if (stringStartDate.equals("OVERMIN")) {
                startDate = LocalDate.now().minusYears(10).format(DateValidationUtils.DateFormat);
            } else {
                startDate = stringStartDate;
            }
        }

        String endDate = null;
        if (stringEndDate != null) {
            if (stringEndDate.equals("MAX")) {
                endDate = LocalDate.now().format(DateValidationUtils.DateFormat);
            } else if (stringEndDate.equals("OVERMAX")) {
                endDate = LocalDate.now().plusDays(1).format(DateValidationUtils.DateFormat);
            } else if (stringEndDate.equals("OVERMIN")) {
                endDate = LocalDate.now().minusYears(10).format(DateValidationUtils.DateFormat);
            } else {
                endDate = stringEndDate;
            }
        }

        ResultActions response = mockMvc.perform(get("/meals/summary")
                .param("start-date", startDate)
                .param("end-date", endDate)
                .param("user-login", login));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Get summary with incorrect date order")
    @Test
    public void givenIncorrectDatesAndLogin_whenGetSummary_thenThrowException() throws Exception {
        ResultActions response = mockMvc.perform(get("/meals/summary")
                .param("start-date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("end-date", LocalDate.now().minusDays(1).format(DateValidationUtils.DateFormat))
                .param("user-login", user.getLogin()));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Get summary with not existing user")
    @Test
    public void givenDatesAndNotExistingLogin_whenGetSummary_thenThrowException() throws Exception {
        ResultActions response = mockMvc.perform(get("/meals/summary")
                .param("start-date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("end-date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("user-login", "bebebebe"));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Get summary")
    @Test
    public void givenDatesAndLogin_whenGetSummary_thenReturnListOfDaySummaryDto() throws Exception {
        mealDAO.saveAll(mealList);

        String date = mealList.get(0).getDate().format(DateValidationUtils.DateFormat);
        ResultActions response = mockMvc.perform(get("/meals/summary")
                .param("start-date", date)
                .param("end-date", date)
                .param("user-login", user.getLogin()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", is(instanceOf(List.class))))
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
