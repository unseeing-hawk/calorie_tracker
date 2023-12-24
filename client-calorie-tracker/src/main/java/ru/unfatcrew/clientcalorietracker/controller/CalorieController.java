package ru.unfatcrew.clientcalorietracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.unfatcrew.clientcalorietracker.pojo.entity.User;
import ru.unfatcrew.clientcalorietracker.rest_service.RestApiService;

@Controller
public class CalorieController {
    private RestApiService restService;

    @Autowired
    public CalorieController(RestApiService restService) {
        this.restService = restService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        if (!isAnonymous()) {
            return "redirect:/";
        }

        return "signin";
    }

    @GetMapping("/login-error")
    public String getLoginPageWithError(HttpServletRequest request, RedirectAttributes attributes) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }

        AuthenticationException exception = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception == null) {
            return "redirect:/login";
        }

        attributes.addFlashAttribute("loginError", exception.getMessage());
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        if (!isAnonymous()) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute User user) {
        restService.registerNewUser(user);
        return "redirect:/login";
    }

    @GetMapping("/")
    public String getHomePage() {
        return "main";
    }

    @GetMapping("/settings")
    public String getSettingsPage(Model model,
                                  @RequestParam(value = "server-error", required = false) Object error) {
        User user = error != null ? new User() : restService.getUserData();
        model.addAttribute("user", user);
        return "settings";
    }

    @PostMapping("/settings/save")
    public String saveUserSettings(@ModelAttribute User user) {
        restService.saveUserSettings(user);
        setNewPasswordInSecurityContext(user.getPassword());
        return "redirect:/settings";
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

    private static boolean isAnonymous() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth == null) || (auth instanceof AnonymousAuthenticationToken);
    }

    private static void setNewPasswordInSecurityContext(String password) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(username, password, AuthorityUtils.NO_AUTHORITIES));
    }
}
