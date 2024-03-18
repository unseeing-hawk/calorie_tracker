package ru.unfatcrew.restcalorietracker.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealGetDto;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDataDto;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDto;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.pojo.request.ChangeMealsRequest;
import ru.unfatcrew.restcalorietracker.pojo.response.ChangeMealsResponse;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import ru.unfatcrew.restcalorietracker.service.MealService;
import ru.unfatcrew.restcalorietracker.validation.DateValidationUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MealRestController.class)
public class MealRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private List<Product> productList;
    private List<MealTime> mealTimeList;
    private MealPostDto mealPostDto;
    private ChangeMealsRequest changeMealsRequest;

    @BeforeEach
    public void setup() {
        user = new User("Beer Master",
                "beermastergood",
                "{bcrypt}$2a$10$RSvT7b55.bwDLBg4c1Rw/uWygwjfvxFw.MJo/ZlRDnEr1xSwPkJU2",
                49.54f);

        productList = List.of(new Product(5L,
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

        mealTimeList = List.of(new MealTime("Breakfast"), new MealTime("Lunch"), new MealTime("Dinner"));

        mealPostDto = new MealPostDto(List.of(new MealPostDataDto(productList.get(0).getId(), 123.21f),
                new MealPostDataDto(productList.get(1).getId(), 341.21f)),
                user.getLogin(),
                LocalDate.now().format(DateValidationUtils.DateFormat),
                mealTimeList.get(1).getName());

        changeMealsRequest = new ChangeMealsRequest(new ArrayList<>(), new ArrayList<>(), user.getLogin());
    }

    @DisplayName("Add meals with invalid request body")
    @Test
    public void givenInvalidMealPostDto_whenAddMeals_thenThrowException() throws Exception {
        given(mealService.addMeals(any())).willThrow(new ConstraintViolationException(new HashSet<>()));

        ResultActions response = mockMvc.perform(post("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealPostDto)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Add meals where some of request body's components do not exist")
    @Test
    public void givenMealPostDtoWithNotExistingComponents_whenAddMeals_thenThrowException() throws Exception {
        given(mealService.addMeals(any())).willThrow(new ResourceNotFoundException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(post("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealPostDto)));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Add meals")
    @Test
    public void givenChangeMealsRequest_whenAddMeals_thenReturnMealGetDto() throws Exception {
        given(mealService.addMeals(any())).willReturn(
                new MealGetDto(
                        new ArrayList<>(),
                        mealPostDto.getUserLogin(),
                        LocalDate.parse(mealPostDto.getDate(), DateValidationUtils.DateFormat).toString()));

        ResultActions response = mockMvc.perform(post("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mealPostDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.mealGetDataList", is(instanceOf(ArrayList.class))))
                .andExpect(jsonPath("$.userLogin", is(mealPostDto.getUserLogin())))
                .andExpect(jsonPath("$.date", is(LocalDate.parse(mealPostDto.getDate(), DateValidationUtils.DateFormat).toString())));
    }

    @DisplayName("Change meals with validated but invalid request body")
    @Test
    public void givenValidatedButInvalidChangeMealsRequest_whenChangeMeals_thenThrowException() throws Exception {
        given(mealService.changeMeals(any())).willThrow(new IllegalRequestArgumentException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(put("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeMealsRequest)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Change meals where some of request body's components do not exist")
    @Test
    public void givenChangeMealsRequestWithNotExistingComponents_whenChangeMeals_thenThrowException() throws Exception {
        given(mealService.changeMeals(any())).willThrow(new ResourceNotFoundException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(put("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeMealsRequest)));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Change meals")
    @Test
    public void givenChangeMealsRequest_whenChangeMeals_thenThrowException() throws Exception{
        given(mealService.changeMeals(any()))
                .willReturn(new ChangeMealsResponse(
                        new MealGetDto(new ArrayList<>(), user.getLogin(), "11.11.2023"),
                        new MealGetDto(new ArrayList<>(), user.getLogin(), "11.11.2023")));

        ResultActions response = mockMvc.perform(put("/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeMealsRequest)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.mealsForChange.userLogin", is(user.getLogin())))
                .andExpect(jsonPath("$.mealsForDeletion.userLogin", is(user.getLogin())));
    }

    @DisplayName("Get meal times")
    @Test
    public void givenNothing_whenGetMealtimes_thenReturnListOfMealtimes() throws Exception {
        given(mealService.getMealTimes()).willReturn(mealTimeList);

        ResultActions response = mockMvc.perform(get("/meals/mealtimes"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", is(instanceOf(List.class))));
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
        given(mealService.getMeals(any(), any())).willThrow(new ResourceNotFoundException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(get("/meals")
                .param("date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("user-login", "bebebebe"));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Get meals")
    @Test
    public void givenDateAndLogin_whenGetMeals_thenReturnMealGetDto() throws Exception{
        String date = LocalDate.now().format(DateValidationUtils.DateFormat);
        given(mealService.getMeals(any(), any()))
                .willReturn(new MealGetDto(new ArrayList<>(),
                        user.getLogin(),
                        LocalDate.parse(date, DateValidationUtils.DateFormat).toString()));

        ResultActions response = mockMvc.perform(get("/meals")
                .param("date",date)
                .param("user-login", user.getLogin()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.userLogin", is(user.getLogin())));
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
        given(mealService.getSummary(any(), any(), any())).willThrow(new IllegalRequestArgumentException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(get("/meals/summary")
                .param("start-date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("end-date", LocalDate.now().minusDays(1).format(DateValidationUtils.DateFormat))
                .param("user-login", user.getLogin()));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Get summary with not existing user")
    @Test
    public void givenDatesAndNotExistingLogin_whenGetSummary_thenThrowException() throws Exception {
        given(mealService.getSummary(any(), any(), any())).willThrow(new ResourceNotFoundException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(get("/meals/summary")
                .param("start-date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("end-date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("user-login", "bebebebe"));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Get summary")
    @Test
    public void givenDatesAndLogin_whenGetSummary_thenReturnListOfDaySummaryDto() throws Exception {
        given(mealService.getSummary(any(), any(), any())).willReturn(new ArrayList<>());

        ResultActions response = mockMvc.perform(get("/meals/summary")
                .param("start-date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("end-date", LocalDate.now().format(DateValidationUtils.DateFormat))
                .param("user-login", user.getLogin()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", is(instanceOf(List.class))));
    }
}
