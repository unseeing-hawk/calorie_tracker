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
import ru.unfatcrew.restcalorietracker.pojo.dto.MealGetDataDto;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealGetDto;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDataDto;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDto;
import ru.unfatcrew.restcalorietracker.pojo.entity.Meal;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.Violation;
import ru.unfatcrew.restcalorietracker.validation.DateValidationUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        LocalDate date = LocalDate.parse(mealPostDto.getDate(), DateValidationUtils.DateFormat);
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
}
