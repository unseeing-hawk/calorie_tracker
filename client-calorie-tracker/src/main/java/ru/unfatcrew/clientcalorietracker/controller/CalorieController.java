package ru.unfatcrew.clientcalorietracker.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.unfatcrew.clientcalorietracker.pojo.entity.User;
import ru.unfatcrew.clientcalorietracker.rest_service.RestApiService;

@Controller
public class CalorieController {
    private static final String PASSWORD_PREFIX = "{bcrypt}";
    private RestApiService restService;

    @Autowired
    public CalorieController(RestApiService restService) {
        this.restService = restService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "signin";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = PASSWORD_PREFIX + passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User userToAdd = new User(user.getName(), user.getLogin(), encodedPassword, user.getWeight());
        restService.registerNewUser(userToAdd);

        return "redirect:/login";
    }

    @GetMapping("/")
    public String getHomePage() {
        return "main";
    }

    @GetMapping("/settings")
    public String getSettingsPage() {
        return "settings";
    }

    @GetMapping("/create-product")
    public String getCreateProductPage() {
        return "add_product";
    }

    @GetMapping("/my-products")
    public String getMyProductsPage() {
        return "list_product";
    }

    @GetMapping("/add-meal")
    public String getAddMealPage() {
        return "add_meal";
    }

    @GetMapping("/change-meal")
    public String getChangeMealPage() {
        return "change_meal";
    }

    @GetMapping("/summary-form")
    public String getSummaryFormPage() {
        return "get_summary";
    }
}
