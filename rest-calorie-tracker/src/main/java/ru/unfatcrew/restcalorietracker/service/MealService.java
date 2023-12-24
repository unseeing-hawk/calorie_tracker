package ru.unfatcrew.restcalorietracker.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
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
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.Violation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static ru.unfatcrew.restcalorietracker.validation.DateValidationUtils.DateFormat;

@Service
@Validated
public class MealService {

    private final UserDAO userDAO;
    private final ProductDAO productDAO;
    private final MealDAO mealDAO;
    private final MealTimeDAO mealTimeDAO;

    @Autowired
    public MealService(UserDAO userDAO, ProductDAO productDAO, MealDAO mealDAO, MealTimeDAO mealTimeDAO) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
        this.mealDAO = mealDAO;
        this.mealTimeDAO = mealTimeDAO;
    }

    @Transactional
    public MealGetDto addMeals(@Valid MealPostDto mealPostDto) {
        List<Violation> violationList = new ArrayList<>();

        User user = userDAO.findByLogin(mealPostDto.getUserLogin());
        if (user == null) {
            violationList.add(new Violation("addMeals.mealPostDto.userLogin", "not found"));
        }

        MealTime mealTime = mealTimeDAO.findByName(mealPostDto.getMealTime());
        if (mealTime == null) {
            violationList.add(new Violation("addMeals.mealPostDto.mealTime", "not found"));
        }

        if (!violationList.isEmpty()) {
            throw new ResourceNotFoundException(violationList);
        }

        LocalDate date = LocalDate.parse(mealPostDto.getDate(), DateFormat);
        List<Meal> mealList = new ArrayList<>();
        List<MealPostDataDto> mealPostDataDtoList = mealPostDto.getMealPostDataList();
        for (int i = 0; i < mealPostDataDtoList.size(); i++) {
            MealPostDataDto mealPostDataDto = mealPostDataDtoList.get(i);

            Optional<Product> optionalProduct = productDAO.findById(mealPostDataDto.getProductId());
            Product product = null;
            if (optionalProduct.isEmpty()) {
                violationList.add(new Violation("addMeals.mealPostDTO.mealPostDataList["
                        + Integer.toString(i)
                        + "].productId",
                        "not found"));
            } else {
                product = optionalProduct.get();
            }

            if (product != null && !product.isActive()) {
                violationList.add(new Violation("addMeals.mealPostDTO.mealPostDataList["
                        + Integer.toString(i)
                        + "].productId",
                        "is not active"));
            }

            if (product != null
                    && product.getUser() != null
                    && product.getUser().getId() != user.getId()) {
                violationList.add(new Violation("addMeals.mealPostDTO.mealPostDataList["
                        + Integer.toString(i)
                        + "].productId-userLogin",
                        "are not connected"));
            }

            mealList.add(new Meal(user, product, mealPostDataDto.getWeight(), date, mealTime));
        }

        if (!violationList.isEmpty()) {
            throw new ResourceNotFoundException(violationList);
        }

        List<MealGetDataDto> mealGetDataList = new ArrayList<>();
        for (Meal meal : mealList) {
            mealDAO.save(meal);
            mealGetDataList.add(new MealGetDataDto(meal));
        }

        return new MealGetDto(mealGetDataList, user.getLogin(), date.toString());
    }

    public ChangeMealsResponse changeMeals(@Valid ChangeMealsRequest changeMealsRequest) {
        List<Violation> violationList = new ArrayList<>();
        List<MealPutDataDto> mealsForChange = changeMealsRequest.getMealsForChange();
        List<Long> mealIdsForDeletion = changeMealsRequest.getMealIdsForDeletion();

        if (mealsForChange.isEmpty() && mealIdsForDeletion.isEmpty()) {
            violationList.add(new Violation("changeMeals.changeMealsRequest.mealsForChange-mealIdsForDeletion",
                    "can not both be empty"));
        }

        if (!violationList.isEmpty()) {
            throw new IllegalRequestArgumentException(violationList);
        }

        String userLogin = changeMealsRequest.getUserLogin();
        if (userDAO.findByLogin(userLogin) == null) {
            violationList.add(new Violation("changeMeals.changeMealsRequest.userLogin",
                    "not found"));
        }

        if (!violationList.isEmpty()) {
            throw new ResourceNotFoundException(violationList);
        }

        HashMap<Long, Meal> mealsForDeletionHashMap = new HashMap<>();
        for (int i = 0; i < mealIdsForDeletion.size(); i++) {
            if (mealsForDeletionHashMap.containsKey(mealIdsForDeletion.get(i))) {
                violationList.add(new Violation("changeMeals.changeMealsRequest.mealIdsForDeletion["
                        + Integer.toString(i)
                        + "].<list element>"
                        , "already exists in mealIdsForDeletion"));
            }

            Optional<Meal> optionalMeal = mealDAO.findById(mealIdsForDeletion.get(i));
            Meal meal = null;
            if (optionalMeal.isEmpty()) {
                violationList.add(new Violation("changeMeals.changeMealsRequest.mealIdsForDeletion["
                        + Integer.toString(i)
                        + "].<list element>"
                        , "not found"));
            } else {
                meal = optionalMeal.get();
            }

            if (meal != null
                    && !meal.getUser().getLogin().equals(userLogin)) {
                violationList.add(new Violation("changeMeals.changeMealsRequest.mealIdsForDeletion["
                        + Integer.toString(i)
                        + "].<list element>-userLogin"
                        , "not connected"));
            }

            if (meal != null) {
                mealsForDeletionHashMap.put(meal.getId(), meal);
            }
        }

        HashMap<Long, Meal> mealsForChangeHashMap = new HashMap<>();
        for (int i = 0; i < mealsForChange.size(); i++) {
            MealPutDataDto mealForChange = mealsForChange.get(i);
            long id = mealForChange.getId();
            if (mealsForChangeHashMap.containsKey(id)) {
                violationList.add(new Violation("changeMeals.changeMealsRequest.mealsForChange["
                        + Integer.toString(i)
                        + "].id"
                        , "already exists in mealsForChange"));
            }

            if (mealsForDeletionHashMap.containsKey(id)) {
                violationList.add(new Violation("changeMeals.changeMealsRequest.mealsForChange["
                        + Integer.toString(i)
                        + "].id"
                        , "already exists in mealIdsForDeletion"));
            }

            Optional<Meal> optionalMeal = mealDAO.findById(id);
            Meal meal = null;
            if (optionalMeal.isEmpty()) {
                violationList.add(new Violation("changeMeals.changeMealsRequest.mealsForChange["
                        + Integer.toString(i)
                        + "].id"
                        , "not found"));
            } else {
                meal = optionalMeal.get();
            }

            if (meal != null
                    && !meal.getUser().getLogin().equals(userLogin)) {
                violationList.add(new Violation("changeMeals.changeMealsRequest.mealsForChange["
                        + Integer.toString(i)
                        + "].id-userLogin"
                        , "not connected"));
            }

            String mealTimeString = mealForChange.getMealTime();
            MealTime mealTime = mealTimeDAO.findByName(mealTimeString);
            if (mealTime == null) {
                violationList.add(new Violation("changeMeals.changeMealsRequest.mealsForChange["
                        + Integer.toString(i)
                        + "].mealTime"
                        , "not found"));
            }

            if (meal != null
                    && mealTime != null
                    && !mealsForChangeHashMap.containsKey(id)
                    && !mealsForDeletionHashMap.containsKey(id)) {
                meal.setWeight(mealForChange.getWeight());
                meal.setMealTime(mealTime);
                mealsForChangeHashMap.put(id, meal);
            }
        }

        if (!violationList.isEmpty()) {
            throw new ResourceNotFoundException(violationList);
        }

        List<MealGetDataDto> mealsForChangeGetDataDtoList = new ArrayList<>();
        for (Map.Entry<Long, Meal> entry : mealsForChangeHashMap.entrySet()) {
            Meal meal = entry.getValue();
            mealDAO.save(meal);
            mealsForChangeGetDataDtoList.add(new MealGetDataDto(meal));
        }

        List<MealGetDataDto> mealsForDeletionGetDataDtoList = new ArrayList<>();
        for (Map.Entry<Long, Meal> entry : mealsForDeletionHashMap.entrySet()) {
            Meal meal = entry.getValue();
            mealDAO.delete(meal);
            mealsForDeletionGetDataDtoList.add(new MealGetDataDto(meal));
        }

        return new ChangeMealsResponse(new MealGetDto(mealsForChangeGetDataDtoList, userLogin, null),
                new MealGetDto(mealsForDeletionGetDataDtoList, userLogin, null));
    }

    public List<MealTime> getMealTimes() {
        return mealTimeDAO.findAll();
    }

    public MealGetDto getMeals(String dateString, String userLogin) {
        List<Violation> violationList = new ArrayList<>();
        User user = userDAO.findByLogin(userLogin);
        if (user == null) {
            violationList.add(new Violation("getMeals.userLogin", "not found"));
        }

        if (!violationList.isEmpty()) {
            throw new ResourceNotFoundException(violationList);
        }

        LocalDate date = LocalDate.parse(dateString, DateFormat);
        List<Meal> mealList = mealDAO.findByDateAndUserLogin(date, userLogin);

        List<MealGetDataDto> mealGetDataDtoList = new ArrayList<>();
        for (Meal meal : mealList) {
            mealGetDataDtoList.add(new MealGetDataDto(meal));
        }

        return new MealGetDto(mealGetDataDtoList, userLogin, dateString);
    }

    public List<DaySummaryDTO> getSummary(String startDateString, String endDateString, String userLogin) {
        List<Violation> violationList = new ArrayList<>();
        LocalDate startDate = LocalDate.parse(startDateString, DateFormat);
        LocalDate endDate = LocalDate.parse(endDateString, DateFormat);

        if (endDate.isBefore(startDate)) {
            violationList.add(new Violation("getSummary.startDate-endDate", "incorrect chronology"));
        }

        if (!violationList.isEmpty()) {
            throw new IllegalRequestArgumentException(violationList);
        }

        User user = userDAO.findByLogin(userLogin);
        if (user == null) {
            violationList.add(new Violation("getSummary.userLogin", "not found"));
        }

        if (!violationList.isEmpty()) {
            throw new ResourceNotFoundException(violationList);
        }

        return mealDAO.findSummary(startDate, endDate, userLogin);
    }
}
