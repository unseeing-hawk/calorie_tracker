package ru.unfatcrew.restcalorietracker.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.unfatcrew.restcalorietracker.dao.MealDAO;
import ru.unfatcrew.restcalorietracker.dao.MealTimeDAO;
import ru.unfatcrew.restcalorietracker.dao.ProductDAO;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.pojo.dto.*;
import ru.unfatcrew.restcalorietracker.pojo.entity.Meal;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.pojo.request.ChangeMealsRequest;
import ru.unfatcrew.restcalorietracker.pojo.response.ChangeMealsResponse;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import ru.unfatcrew.restcalorietracker.validation.DateValidationUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MealServiceTests {

    @InjectMocks
    private MealService mealService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private MealDAO mealDAO;

    @Mock
    private  MealTimeDAO mealTimeDAO;

    private static Validator validator;
    private User user;
    private List<Product> productList;
    private MealPostDto mealPostDto;
    private List<MealTime> mealTimeList;
    private Map<String, MealTime> mealTimeMap;
    private Map<Long, Meal> mealHashMap;
    private ChangeMealsRequest changeMealsRequest;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

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

        mealTimeMap = mealTimeList
                .stream()
                .collect(Collectors.toMap(data -> data.getName(), data -> data));

        mealPostDto = new MealPostDto(List.of(new MealPostDataDto(productList.get(0).getId(), 123.21f),
                        new MealPostDataDto(productList.get(1).getId(), 341.21f)),
                user.getLogin(),
                LocalDate.now().format(DateValidationUtils.DateFormat),
                mealTimeList.get(1).getName());

        List<Meal> mealList = List.of(new Meal(user, productList.get(0), 13.88f, LocalDate.now(), mealTimeList.get(1)),
                new Meal(user, productList.get(1), 275f, LocalDate.now(), mealTimeList.get(1)),
                new Meal(user, productList.get(0), 21, LocalDate.now(), mealTimeList.get(2)),
                new Meal(user, productList.get(1), 22, LocalDate.now(), mealTimeList.get(2)));

        for (int i = 0; i < mealList.size(); i++) {
            mealList.get(i).setId(i + 1);
        }

        mealHashMap = Stream.of(new Object[][] {
                {mealList.get(0).getId(), mealList.get(0)},
                {mealList.get(1).getId(), mealList.get(1)},
                {mealList.get(2).getId(), mealList.get(2)},
                {mealList.get(3).getId(), mealList.get(3)}
        }).collect(Collectors.toMap(data -> (Long) data[0], data -> (Meal) data[1]));

        changeMealsRequest = new ChangeMealsRequest(List.of(
                        new MealPutDataDto(mealList.get(0).getId(), 12.45f, "Breakfast"),
                        new MealPutDataDto(mealList.get(1).getId(), 250, "Breakfast")),
                List.of(mealList.get(2).getId(), mealList.get(3).getId()),
                user.getLogin());
    }

    @DisplayName("Validate valid MealPostDataDto object")
    @ParameterizedTest
    @ValueSource(floats = {1.32f, Float.MAX_VALUE})
    public void givenMealPostDataDto_whenValidate_thenNothing(float weight) {
        MealPostDataDto mealPostDataDto = new MealPostDataDto(7, weight);

        Set<ConstraintViolation<MealPostDataDto>> violations = validator.validate(mealPostDataDto);

        assertTrue(violations.isEmpty());
    }

    @DisplayName("Validate invalid MealPostDataDto object")
    @ParameterizedTest
    @CsvFileSource(resources="/invalid-meal-post-data-dto.csv",
            numLinesToSkip=1,
            delimiter=';')
    public void givenInvalidMealPostDataDto_whenValidate_thenReturnViolations(long id, String weight) {
        MealPostDataDto mealPostDataDto = new MealPostDataDto(id,
                Float.parseFloat(weight.replace(",", ".")));

        Set<ConstraintViolation<MealPostDataDto>> violations = validator.validate(mealPostDataDto);

        assertFalse(violations.isEmpty());
    }

    @DisplayName("Validate valid MealPostDto object")
    @ParameterizedTest
    @CsvFileSource(resources="/valid-meal-post-dto.csv",
            numLinesToSkip=1,
            delimiter=';')
    public void givenMealPostDto_whenValidate_thenNothing(String login, String stringDate, String stringMealTime) {
        MealPostDto mealPostDto = new MealPostDto(List.of(new MealPostDataDto(5, 5.67f)),
                login,
                stringDate.equals("MAX")
                        ? LocalDate.now().format(DateValidationUtils.DateFormat)
                        : LocalDate.now()
                                .minusYears(10)
                                .plusDays(1)
                                .format(DateValidationUtils.DateFormat),
                stringMealTime);

        Set<ConstraintViolation<MealPostDto>> violations = validator.validate(mealPostDto);

        assertTrue(violations.isEmpty());
    }

    @DisplayName("Validate invalid MealPostDto object")
    @ParameterizedTest
    @CsvFileSource(resources="/invalid-meal-post-dto.csv",
            numLinesToSkip=1,
            delimiter=';',
            nullValues={"NULL"})
    public void givenInvalidMealPostDto_whenValidate_thenReturnViolations(String login,
                                                                          String stringDate,
                                                                          String mealTime,
                                                                          long productId,
                                                                          String stringWeight) {
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

        List<MealPostDataDto> mealPostDataDtoList = null;
        if (stringWeight.equals("LIST_EMPTY")) {
            mealPostDataDtoList = new ArrayList<>();
        } else if (stringWeight.equals("LIST_NULL")) {
            mealPostDataDtoList = null;
        } else {
            mealPostDataDtoList = List.of(new MealPostDataDto(productId,
                    Float.parseFloat(stringWeight.replace(",", "."))));
        }

        MealPostDto mealPostDto = new MealPostDto(mealPostDataDtoList,
                login,
                stringDate,
                mealTime);

        Set<ConstraintViolation<MealPostDto>> violations = validator.validate(mealPostDto);

        assertFalse(violations.isEmpty());
    }

    @DisplayName("Validate valid MealPutDataDto object")
    @ParameterizedTest
    @CsvFileSource(resources="/valid-meal-put-data-dto.csv",
            numLinesToSkip=1,
            delimiter=';')
    public void givenMealPutDataDto_whenValidate_thenNothing(long id, String stringWeight, String mealTime) {
        float weight = stringWeight.equals("MAX")
                ? Float.MAX_VALUE
                : Float.parseFloat(stringWeight.replace(",", "."));
        MealPutDataDto mealPutDataDto = new MealPutDataDto(id, weight, mealTime);

        Set<ConstraintViolation<MealPutDataDto>> violations = validator.validate(mealPutDataDto);

        assertTrue(violations.isEmpty());
    }

    @DisplayName("Validate invalid MealPutDataDto object")
    @ParameterizedTest
    @CsvFileSource(resources="/invalid-meal-put-data-dto.csv",
            numLinesToSkip=1,
            delimiter=';',
            nullValues={"NULL"})
    public void givenInvalidMealPutDataDto_whenValidate_thenReturnViolations(long id, String stringWeight, String mealTime) {
        float weight = Float.parseFloat(stringWeight.replace(",", "."));
        MealPutDataDto mealPutDataDto = new MealPutDataDto(id, weight, mealTime);

        Set<ConstraintViolation<MealPutDataDto>> violations = validator.validate(mealPutDataDto);

        assertFalse(violations.isEmpty());
    }

    @DisplayName("Validate valid ChangeMealsRequest")
    @ParameterizedTest
    @CsvFileSource(resources="/valid-change-meals-request.csv",
            numLinesToSkip=1,
            delimiter=';')
    public void givenChangeMealsRequest_whenValidate_thenNothing(String stringMealsForChange,
                                                                 String stringMealIdsForDeletion,
                                                                 String login) {
        List<MealPutDataDto> mealsForChange = null;
        if (stringMealsForChange.equals("EMPTY")) {
            mealsForChange = new ArrayList<>();
        } else {
            mealsForChange = List.of(new MealPutDataDto(3, 45.12f, "Breakfast"));
        }

        List<Long> mealIdsForDeletion = null;
        if (stringMealIdsForDeletion.equals("EMPTY")) {
            mealIdsForDeletion = new ArrayList<>();
        } else {
            mealIdsForDeletion = Arrays.asList(stringMealIdsForDeletion.split(","))
                    .stream()
                    .map(token -> Long.parseLong(token))
                    .toList();
        }

        ChangeMealsRequest changeMealsRequest = new ChangeMealsRequest(mealsForChange, mealIdsForDeletion, login);

        Set<ConstraintViolation<ChangeMealsRequest>> violations = validator.validate(changeMealsRequest);

        assertTrue(violations.isEmpty());
    }

    @DisplayName("Validate invalid ChangeMealsRequest")
    @ParameterizedTest
    @CsvFileSource(resources="/invalid-change-meals-request.csv",
            numLinesToSkip=1,
            delimiter=';',
            nullValues={"NULL"})
    public void givenInvalidChangeMealsRequest_whenValidate_thenReturnViolations(String stringMealsForChange,
                                                                                 String stringMealIdsForDeletion,
                                                                                 String login) {
        List<MealPutDataDto> mealsForChange = null;
        if (stringMealsForChange == null) {
            mealsForChange = null;
        } else if (stringMealsForChange.equals("INVALID")) {
            mealsForChange = List.of(new MealPutDataDto(-7, 34.2f, "Breakfast"));
        } else {
            mealsForChange = List.of(new MealPutDataDto(7, 34.2f, "Breakfast"));
        }

        List<Long> mealIdsForDeletion = stringMealIdsForDeletion == null
                ? null
                : Arrays.asList(stringMealIdsForDeletion.split(","))
                        .stream()
                        .map(token -> Long.parseLong(token))
                        .toList();

        ChangeMealsRequest changeMealsRequest = new ChangeMealsRequest(mealsForChange, mealIdsForDeletion, login);

        Set<ConstraintViolation<ChangeMealsRequest>> violations = validator.validate(changeMealsRequest);

        assertFalse(violations.isEmpty());
    }

    @DisplayName("Create meals with not existing user login")
    @Test
    public void givenMealPostDtoWithNotExistingLogin_whenAddMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.addMeals(mealPostDto);
        });

        verify(mealDAO, never()).save(any(Meal.class));
    }

    @DisplayName("Create meals with not existing meal time")
    @Test
    public void givenMealPostDtoWithNotExistingMealTime_whenAddMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealTimeDAO.findByName(mealPostDto.getMealTime())).willReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.addMeals(mealPostDto);
        });

        verify(userDAO, atLeastOnce()).findByLogin(any(String.class));
        verify(productDAO, atMostOnce()).findById(any(Long.class));
        verify(mealDAO, never()).save(any(Meal.class));
    }

    @DisplayName("Create meals with not existing product")
    @Test
    public void givenMealPostDtoWithNotExistingProduct_whenAddMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealTimeDAO.findByName(mealPostDto.getMealTime())).willReturn(new MealTime(mealPostDto.getMealTime()));
        given(productDAO.findById(productList.get(0).getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.addMeals(mealPostDto);
        });

        verify(productDAO, atLeastOnce()).findById(any(Long.class));
        verify(mealDAO, never()).save(any(Meal.class));
    }

    @DisplayName("Create meals with inactive product")
    @Test
    public void givenMealPostDtoWithInactiveProduct_whenAddMeals_thenThrowException() {
        productList.get(0).setActive(false);
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealTimeDAO.findByName(mealPostDto.getMealTime())).willReturn(new MealTime(mealPostDto.getMealTime()));
        given(productDAO.findById(productList.get(0).getId())).willReturn(Optional.of(productList.get(0)));

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.addMeals(mealPostDto);
        });

        verify(productDAO, atLeastOnce()).findById(any(Long.class));
        verify(mealDAO, never()).save(any(Meal.class));
    }

    @DisplayName("Create meals with other user's product")
    @Test
    public void givenMealPostDtoWithOtherUserProduct_whenAddMeals_thenThrowException() {
        User otherUser = new User("Caramba Foe", "asdfghjk", "abc", 89.12f);
        otherUser.setId(user.getId() + 1);
        productList.get(0).setUser(otherUser);

        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealTimeDAO.findByName(mealPostDto.getMealTime())).willReturn(new MealTime(mealPostDto.getMealTime()));
        given(productDAO.findById(productList.get(0).getId())).willReturn(Optional.of(productList.get(0)));

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.addMeals(mealPostDto);
        });

        verify(productDAO, atLeastOnce()).findById(any(Long.class));
        verify(mealDAO, never()).save(any(Meal.class));
    }

    @DisplayName("Create meals")
    @Test
    public void givenMealPostDto_whenAddMeals_thenReturnMealGetDataDto() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealTimeDAO.findByName(mealPostDto.getMealTime())).willReturn(new MealTime(mealPostDto.getMealTime()));
        given(productDAO.findById(productList.get(0).getId())).willReturn(Optional.of(productList.get(0)));
        given(productDAO.findById(productList.get(1).getId())).willReturn(Optional.of(productList.get(1)));
        given(mealDAO.save(any(Meal.class))).willAnswer((invocation) -> invocation.getArgument(0));

        MealGetDto mealGetDto = mealService.addMeals(mealPostDto);

        assertNotNull(mealGetDto);
        assertAll("meal get dto",
                () -> {
                    assertNotNull(mealGetDto.getUserLogin());
                    assertEquals(user.getLogin(), mealGetDto.getUserLogin());
                },
                () -> {
                    assertNotNull(mealGetDto.getDate());
                    assertEquals(LocalDate.parse(mealPostDto.getDate(), DateValidationUtils.DateFormat).toString(),
                            mealGetDto.getDate());
                },
                () -> {
                    List<MealGetDataDto> mealGetDataDtoList = mealGetDto.getMealGetDataList();

                    assertNotNull(mealGetDto.getMealGetDataList());
                    assertEquals(2, mealGetDataDtoList.size());
                    assertAll("meal get data list",
                            () -> {
                                MealGetDataDto mealGetDataDto = mealGetDataDtoList.get(0);
                                assertNotNull(mealGetDataDto);
                                assertEquals(productList.get(0).getId(), mealGetDataDto.getProduct().getId());
                            },
                            () -> {
                                MealGetDataDto mealGetDataDto = mealGetDataDtoList.get(1);
                                assertNotNull(mealGetDataDto);
                                assertEquals(productList.get(1).getId(), mealGetDataDto.getProduct().getId());
                            });
                }
        );
    }

    @DisplayName("Change meals with empty lists for change and deletion")
    @Test
    public void givenChangeMealsRequestWithEmptyLists_whenChangeMeals_thenThrowException() {
        changeMealsRequest.setMealsForChange(new ArrayList<>());
        changeMealsRequest.setMealIdsForDeletion(new ArrayList<>());

        assertThrows(IllegalRequestArgumentException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals with not existing login")
    @Test
    public void givenChangeMealsRequestWithNotExistingLogin_whenChangeMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals with repeating meal ids for deletion")
    @Test
    public void givenChangeMealsRequestWithRepeatingMealIdsForDeletion_whenChangeMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findById(any()))
                .willAnswer((invocation) -> Optional.of(mealHashMap.get(invocation.getArgument(0))));
        given(mealTimeDAO.findByName(any()))
                .willAnswer(invocation -> mealTimeMap.getOrDefault(invocation.getArgument(0), null));
        changeMealsRequest.setMealIdsForDeletion(List.of(1l, 1l));

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals with not existing meal ids for deletion")
    @Test
    public void givenChangeMealsRequestWithNotExistingMealIdsForDeletion_whenChangeMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findById(any()))
                .willAnswer((invocation) -> Optional.of(mealHashMap.get(invocation.getArgument(0))));
        given(mealDAO.findById(changeMealsRequest.getMealIdsForDeletion().get(0)))
                .willAnswer((invocation) -> Optional.empty());
        given(mealTimeDAO.findByName(any()))
                .willAnswer(invocation -> mealTimeMap.getOrDefault(invocation.getArgument(0), null));

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals with meal id for deletion connected to the other user")
    @Test
    public void givenChangeMealsRequestWithMealIdsForDeletionOfOtherUser_whenChangeMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findById(any()))
                .willAnswer((invocation) -> Optional.of(mealHashMap.get(invocation.getArgument(0))));
        given(mealTimeDAO.findByName(any()))
                .willAnswer(invocation -> mealTimeMap.getOrDefault(invocation.getArgument(0), null));
        user.setLogin("bebebebe");

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals with repeating meals for change")
    @Test
    public void givenChangeMealsRequestWithRepeatingMealsForChange_whenChangeMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findById(any()))
                .willAnswer((invocation) -> Optional.of(mealHashMap.get(invocation.getArgument(0))));
        given(mealTimeDAO.findByName(any()))
                .willAnswer(invocation -> mealTimeMap.getOrDefault(invocation.getArgument(0), null));
        changeMealsRequest.getMealsForChange().get(0).setId(
                changeMealsRequest.getMealsForChange().get(1).getId());

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals with repeating meals for change ids repeating in meals for deletion ids")
    @Test
    public void givenChangeMealsRequestWithMealsForChangeIdsRepeatingInMealsForDeletionIds_whenChangeMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findById(any()))
                .willAnswer((invocation) -> Optional.of(mealHashMap.get(invocation.getArgument(0))));
        given(mealTimeDAO.findByName(any()))
                .willAnswer(invocation -> mealTimeMap.getOrDefault(invocation.getArgument(0), null));
        changeMealsRequest.getMealsForChange().get(0).setId(
                changeMealsRequest.getMealIdsForDeletion().get(0));

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals with not existing meals for change")
    @Test
    public void givenChangeMealsRequestWithNotExistingMealsForChange_whenChangeMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findById(any()))
                .willAnswer((invocation) -> Optional.of(mealHashMap.get(invocation.getArgument(0))));
        given(mealDAO.findById(changeMealsRequest.getMealsForChange().get(0).getId()))
                .willAnswer((invocation) -> Optional.empty());
        given(mealTimeDAO.findByName(any()))
                .willAnswer(invocation -> mealTimeMap.getOrDefault(invocation.getArgument(0), null));

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals with meals for change of another user")
    @Test
    public void givenChangeMealsRequestWithMealsForChangeOfAnotherUser_whenChangeMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findById(any()))
                .willAnswer((invocation) -> Optional.of(mealHashMap.get(invocation.getArgument(0))));
        given(mealTimeDAO.findByName(any()))
                .willAnswer(invocation -> mealTimeMap.getOrDefault(invocation.getArgument(0), null));
        user.setLogin("bebebebe");

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals with meals for change with not existing meal time")
    @Test
    public void givenChangeMealsRequestWithNotExistingMealTime_whenChangeMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findById(any()))
                .willAnswer((invocation) -> Optional.of(mealHashMap.get(invocation.getArgument(0))));
        given(mealTimeDAO.findByName(any()))
                .willAnswer(invocation -> mealTimeMap.getOrDefault(invocation.getArgument(0), null));
        changeMealsRequest.getMealsForChange().get(0).setMealTime("Branch");

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.changeMeals(changeMealsRequest);
        });

        verify(mealDAO, never()).save(any(Meal.class));
        verify(mealDAO, never()).delete(any(Meal.class));
    }

    @DisplayName("Change meals")
    @Test
    public void givenChangeMealsRequest_whenChangeMeals_thenReturnChangeMealsResponse() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findById(any()))
                .willAnswer((invocation) -> Optional.of(mealHashMap.get(invocation.getArgument(0))));
        given(mealTimeDAO.findByName(any()))
                .willAnswer(invocation -> mealTimeMap.getOrDefault(invocation.getArgument(0), null));
        given(mealDAO.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        ChangeMealsResponse changeMealsResponse = mealService.changeMeals(changeMealsRequest);

        assertNotNull(changeMealsResponse);
        assertAll("change meals response",
                () -> {
                    MealGetDto mealGetDto = changeMealsResponse.getMealsForDeletion();
                    assertNotNull(mealGetDto);
                    assertNotNull(mealGetDto.getMealGetDataList());
                    assertNull(mealGetDto.getDate());
                    assertNotNull(mealGetDto.getUserLogin());
                    assertEquals(changeMealsRequest.getMealIdsForDeletion().size(), mealGetDto.getMealGetDataList().size());
                    assertEquals(user.getLogin(), mealGetDto.getUserLogin());
                    assertAll("meals for deletion",
                            () -> {
                                List<MealGetDataDto> mealGetDataDtoList = mealGetDto.getMealGetDataList();
                                for (int i = 0; i < mealGetDataDtoList.size(); i++) {
                                    MealGetDataDto mealGetDataDto = mealGetDataDtoList.get(i);
                                    assertEquals(changeMealsRequest.getMealIdsForDeletion().get(i),
                                            mealGetDataDto.getId());
                                }
                            });
                },
                () -> {
                    MealGetDto mealGetDto = changeMealsResponse.getMealsForChange();
                    assertNotNull(mealGetDto);
                    assertNotNull(mealGetDto.getMealGetDataList());
                    assertNull(mealGetDto.getDate());
                    assertNotNull(mealGetDto.getUserLogin());
                    assertEquals(changeMealsRequest.getMealsForChange().size(), mealGetDto.getMealGetDataList().size());
                    assertEquals(user.getLogin(), mealGetDto.getUserLogin());
                    assertAll("meals for change",
                            () -> {
                                List<MealGetDataDto> mealGetDataDtoList = mealGetDto.getMealGetDataList();
                                for (int i = 0; i < mealGetDataDtoList.size(); i++) {
                                    MealGetDataDto mealGetDataDto = mealGetDataDtoList.get(i);
                                    MealPutDataDto mealForChange = changeMealsRequest.getMealsForChange().get(i);
                                    assertEquals(mealForChange.getId(), mealGetDataDto.getId());
                                    assertEquals(mealForChange.getMealTime(), mealGetDataDto.getMealTime());
                                    assertEquals(mealForChange.getWeight(), mealGetDataDto.getWeight(), 0.0001);
                                }
                            });
                });
    }

    @DisplayName("Get meal times")
    @Test
    public void givenNothing_whenGetMealTimes_thenReturnListOfMealTimes() {
        given(mealTimeDAO.findAll()).willReturn(mealTimeList);

        List<MealTime> mealTimes = mealService.getMealTimes();

        assertNotNull(mealTimes);
        assertEquals(mealTimeList.size(), mealTimes.size());
    }

    @DisplayName("Get meals with a not existing login")
    @Test
    public void givenDateStringAndNotExistingLogin_whenGetMeals_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
           mealService.getMeals(LocalDate.now().format(DateValidationUtils.DateFormat), user.getLogin());
        });

        verify(mealDAO, never()).findByDateAndUserLogin(any(), any());
    }

    @DisplayName("Get meals")
    @Test
    public void givenDateStringAndLogin_whenGetMeals_thenReturnMealGetDto() {
        LocalDate date = LocalDate.now();
        String stringDate = date.format(DateValidationUtils.DateFormat);
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findByDateAndUserLogin(date, user.getLogin()))
                .willReturn(List.of(mealHashMap.get(1l), mealHashMap.get(2l)));

        MealGetDto mealGetDto = mealService.getMeals(stringDate, user.getLogin());

        assertNotNull(mealGetDto);
        assertNotNull(mealGetDto.getDate());
        assertNotNull(mealGetDto.getUserLogin());
        assertEquals(stringDate, mealGetDto.getDate());
        assertEquals(user.getLogin(), mealGetDto.getUserLogin());
    }

    @DisplayName("Get summary with an end date before start date")
    @Test
    public void givenEndDateBeforeStartDate_whenGetSummary_thenThrowException() {
        LocalDate date = LocalDate.now();
        String startDateString = date.format(DateValidationUtils.DateFormat);
        String endDateString = date.minusDays(1).format(DateValidationUtils.DateFormat);

        assertThrows(IllegalRequestArgumentException.class, () -> {
            mealService.getSummary(startDateString, endDateString, user.getLogin());
        });

        verify(mealDAO, never()).findSummary(any(), any(), any());
    }

    @DisplayName("Get summary with a not existing login")
    @Test
    public void givenDatesAndNotExistingLogin_whenGetSummary_thenThrowException() {
        LocalDate date = LocalDate.now();
        String startDateString = date.format(DateValidationUtils.DateFormat);
        String endDateString = date.plusDays(1).format(DateValidationUtils.DateFormat);
        given(userDAO.findByLogin(user.getLogin())).willReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            mealService.getSummary(startDateString, endDateString, user.getLogin());
        });

        verify(mealDAO, never()).findSummary(any(), any(), any());
    }

    @DisplayName("Get summary")
    @Test
    public void givenDatesAndLogin_whenGetSummary_thenReturnDaySummaryDtoList() {
        LocalDate date = LocalDate.now();
        String startDateString = date.format(DateValidationUtils.DateFormat);
        String endDateString = date.plusDays(1).format(DateValidationUtils.DateFormat);
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);
        given(mealDAO.findSummary(any(), any(), any())).willReturn(new ArrayList<>());

        List<DaySummaryDTO> daySummaryDTOList = mealService.getSummary(startDateString, endDateString, user.getLogin());

        assertNotNull(daySummaryDTOList);
    }
}
