package ru.unfatcrew.clientcalorietracker.controller;

import static ru.unfatcrew.clientcalorietracker.utils.DateUtils.dateFormatter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.servlet.support.RequestContextUtils;
import ru.unfatcrew.clientcalorietracker.pojo.dto.*;
import ru.unfatcrew.clientcalorietracker.pojo.dto.dom.ChangeMealDTO;
import ru.unfatcrew.clientcalorietracker.pojo.dto.dom.ProductDTO;
import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;
import ru.unfatcrew.clientcalorietracker.pojo.entity.User;
import ru.unfatcrew.clientcalorietracker.pojo.requests.ChangeMealsRequest;
import ru.unfatcrew.clientcalorietracker.pojo.requests.ChangeProductsRequest;
import ru.unfatcrew.clientcalorietracker.rest_service.RestApiService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CalorieController {
    private RestApiService restService;
    private static final String csvFileName = "summary.csv";
    private List<Long> idsProductsToDelete;
    private List<DaySummaryDTO> summaryList;
    private List<Long> idsMealsToDelete;

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
    public String getCreateProductPage(Model model) {
        model.addAttribute("product", new ProductPostDTO());
        return "add_product";
    }

    @PostMapping("/create-product")
    public String createUserProduct(@Valid @ModelAttribute ProductPostDTO product) {
        restService.addUserProduct(product);
        return "redirect:/create-product";
    }

    @GetMapping("/my-products")
    public String getMyProductsPage(HttpServletRequest request, Model model) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        List<Product> products = (flashMap == null || flashMap.isEmpty()) ?
                restService.getUserProducts() : new ArrayList<>();

        idsProductsToDelete = new ArrayList<>();

        model.addAttribute("productDTO", new ProductDTO(products));
        return "list_product";
    }

    @PostMapping(value = "/my-products", params = {"apply"})
    public String applyProductsChanges(@ModelAttribute ProductDTO productDTO) {
        restService.changeUserProducts(new ChangeProductsRequest(productDTO.getProducts(), idsProductsToDelete));
        return "redirect:/my-products";
    }

    @PostMapping(value = "/my-products", params = {"remove"})
    public String addToProductsToDelete(@ModelAttribute ProductDTO productDTO) {
        idsProductsToDelete.addAll(productDTO.getIdsToDelete());
        productDTO.getProducts().removeIf(product -> idsProductsToDelete.contains(product.getId()));
        return "list_product";
    }

    @GetMapping("/add-meal")
    public String getAddMealPage() {
        return "add_meal";
    }

    @GetMapping("/change-meal")
    public String getChangeMealPage(Model model) {
        idsMealsToDelete = new ArrayList<>();
        model.addAttribute("changeDTO", new ChangeMealDTO());
        return "change_meal";
    }

    @PostMapping(value = "/change-meal", params = {"search"})
    public String searchMeals(@ModelAttribute("date") String date,
                              @ModelAttribute("changeDTO") ChangeMealDTO changeDTO) {
        MealGetDTO mealGetDto = restService.getMeals(LocalDate.parse(date).format(dateFormatter));

        changeDTO.setMealsToChange(mealGetDto.getMealGetDataList());
        return "change_meal";
    }

    @PostMapping(value = "/change-meal", params = {"apply"})
    public String applyMealsChanges(@ModelAttribute("changeDTO") ChangeMealDTO changeDTO) {
        List<MealPutDataDTO> mealsToChange = new ArrayList<>();
        changeDTO.getMealsToChange().forEach(meal -> mealsToChange.add(new MealPutDataDTO(meal.getId(),
                meal.getWeight(), meal.getMealTime())));
        restService.changeMeals(new ChangeMealsRequest(mealsToChange, idsMealsToDelete));

        return "redirect:/change-meal";
    }

    @PostMapping(value = "/change-meal", params = {"remove"})
    public String addMealsToDeleteList(@ModelAttribute("changeDTO") ChangeMealDTO changeDTO) {
        idsMealsToDelete.addAll(changeDTO.getIdsMealsToDelete());
        changeDTO.getMealsToChange().removeIf(value -> idsMealsToDelete.contains(value.getId()));
        return "change_meal";
    }

    @GetMapping("/summary-form")
    public String getSummaryFormPage(Model model) {
        summaryList = new ArrayList<>();
        model.addAttribute("showTable", false);
        return "get_summary";
    }

    @PostMapping(value = "/summary-form", params = {"getTable"})
    public String getSummary(@ModelAttribute("startDate") String startDate,
                             @ModelAttribute("endDate") String endDate,
                             Model model) {
        summaryList = restService.getDaySummary(LocalDate.parse(startDate).format(dateFormatter),
                LocalDate.parse(endDate).format(dateFormatter));
        summaryList.forEach(value -> value.setDate(LocalDate.parse(value.getDate()).format(dateFormatter)));
        model.addAttribute("summaryDTO", summaryList);
        model.addAttribute("showTable", true);
        return "get_summary";
    }

    @PostMapping(value = "/summary-form", params = {"csv"})
    public ResponseEntity<Resource> getSummaryCsv() throws IOException {
        String[] headers = {"Date", "Weight", "Calories", "Proteins", "Fats", "Carbohydrates"};
        StringBuilder strBuilder = new StringBuilder();

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(headers)
                .build();

        try (CSVPrinter printer = new CSVPrinter(strBuilder, csvFormat)) {
            for (DaySummaryDTO row : summaryList) {
                printer.printRecord(row.getDate(), row.getWeight(), row.getCalories(), row.getProteins(),
                        row.getFats(), row.getCarbohydrates());
            }
        }
        Resource resource = new ByteArrayResource(strBuilder.toString().getBytes());

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + csvFileName)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
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
