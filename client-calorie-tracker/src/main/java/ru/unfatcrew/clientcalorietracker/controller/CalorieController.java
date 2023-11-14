package ru.unfatcrew.clientcalorietracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalorieController {
    @GetMapping("/login")
    public String getLoginPage() {
        return "signin";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "signup";
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
