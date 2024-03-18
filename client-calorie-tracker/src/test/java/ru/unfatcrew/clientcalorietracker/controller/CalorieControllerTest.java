package ru.unfatcrew.clientcalorietracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestTemplate;
import ru.unfatcrew.clientcalorietracker.pojo.dto.*;
import ru.unfatcrew.clientcalorietracker.pojo.dto.dom.AddMealDTO;
import ru.unfatcrew.clientcalorietracker.pojo.dto.dom.ChangeMealDTO;
import ru.unfatcrew.clientcalorietracker.pojo.dto.dom.ProductDTO;
import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;
import ru.unfatcrew.clientcalorietracker.pojo.entity.User;
import ru.unfatcrew.clientcalorietracker.pojo.requests.ChangeMealsRequest;
import ru.unfatcrew.clientcalorietracker.pojo.requests.ChangeProductsRequest;
import ru.unfatcrew.clientcalorietracker.rest_service.config.RestTemplateConfig;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static ru.unfatcrew.clientcalorietracker.utils.DateUtils.*;

@WebMvcTest(CalorieController.class)
@ContextConfiguration(classes = {RestTemplateConfig.class})
@ComponentScan("ru.unfatcrew.clientcalorietracker")
public class CalorieControllerTest {
    @Value("${application.rest.api.url}")
    private String restURL;
    private RestTemplate rest;
    private MockRestServiceServer mockServer;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User userWithIncorrectWeight;
    private User validUser;
    private ProductPostDTO validProductPostDTO;
    private List<Product> productList;
    private AddMealDTO addMealDTO;
    private ProductDTO productDTO;
    private MealPostDTO mealPostDTO;
    private String date;
    private String startDate;
    private String endDate;

    @Autowired
    public CalorieControllerTest(RestTemplate restTemplate,
                                 MockMvc mockMvc) {
        this.rest = restTemplate;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.startDate = "2022-03-20";
        this.endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(rest);

        validUser = new User("FirstName LastName MiddleName", "testuser", "password", "60.0");
        userWithIncorrectWeight = new User("FirstName LastName MiddleName", "testuser", "password", "-60.0");
        validProductPostDTO = new ProductPostDTO("User", "Test Product", 15, 10.0f, 5.0f, 2.5f);
        productList = List.of(new Product(1L, "Product 1", 12, 20.0f, 15.0f, 5.0f),
                new Product(2L, "Product 2", 255, 125.0f, 55.0f, 0.0f));

        addMealDTO = new AddMealDTO();
        addMealDTO.setProductsToAdd(List.of(new AddMealDTO.ProductToAdd(
                new SearchProductDTO(1L, "testuser", "Product", 150, 5.0f, 10.0f, 20.0f),
                150.0f),
                new AddMealDTO.ProductToAdd(
                        new SearchProductDTO(2L, "Product FS", 15, 10.0f, 20.0f, 30.0f),
                        250.0f)));
        addMealDTO.setDate(date);
        addMealDTO.setMealTime("Breakfast");

        mealPostDTO = new MealPostDTO(List.of(new MealPostDataDTO(1L, 150.0f),
                new MealPostDataDTO(2L, 250.0f)),
                "user", LocalDate.parse(date).format(dateFormatter), "Breakfast");

        productDTO = new ProductDTO(productList);
    }

    @DisplayName("Successfully opening of the login page with unauthorised user")
    @Test
    @WithAnonymousUser
    public void testSuccessfullyLoginPageOpenWithUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @DisplayName("Successfully redirection to main page when login page open with authorised user")
    @Test
    @WithMockUser
    public void testSuccessfullyRedirectionFromLoginToMainPageWithAuthorizedUser() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("Successfully opening of the register page with unauthorised user")
    @Test
    @WithAnonymousUser
    public void testSuccessfullyRegisterPageOpenWithUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @DisplayName("Successfully redirection to main page when register page open with authorised user")
    @Test
    @WithMockUser
    public void testSuccessfullyRedirectionFromRegisterToMainPageWithAuthorizedUser() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @DisplayName("Successfully user register")
    @Test
    @WithAnonymousUser
    public void testSuccessfullyUserRegister() throws Exception {
        mockServer.expect(requestTo(restURL + "users"))
                .andRespond(withSuccess());

        mockMvc.perform(post("/register")
                        .flashAttr("user", validUser)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        mockServer.verify();
    }

    @DisplayName("User register with incorrect user weight")
    @Test
    @WithAnonymousUser
    public void testUserRegisterWithIncorrectUserWeight() throws Exception {
        mockMvc.perform(post("/register")
                        .flashAttr("user", userWithIncorrectWeight)
                        .with(csrf()))
                .andExpect(result -> Assertions.assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @DisplayName("Successfully opening of the main page")
    @Test
    @WithMockUser
    public void testSuccessfullyMainPageOpen() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"));
    }

    @DisplayName("Successfully opening of the settings page")
    @Test
    @WithMockUser
    public void testSuccessfullyOpeningOfSettingsPage() throws Exception {
        mockServer.expect(requestTo(restURL + "users?login=%s".formatted("user")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(validUser), MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", validUser))
                .andExpect(view().name("settings"));

        mockServer.verify();
    }

    @DisplayName("Successfully opening the product creation page")
    @Test
    @WithMockUser
    public void testSuccessfullyCreateProductPageOpen() throws Exception {
        mockMvc.perform(get("/create-product"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_product"))
                .andExpect(model().attributeExists("product"));
    }

    @DisplayName("Successfully products searching")
    @Test
    @WithMockUser
    public void testSuccessfullyProductsSearching() throws Exception {
        String searchPattern = "searchPattern";
        AddMealDTO addMealDTO_ = new AddMealDTO();
        addMealDTO_.setSearchProductList(addMealDTO.getSearchProductList());

        mockServer.expect(requestTo(restURL + "products/search-products?user-login=%s&pattern=%s"
                        .formatted("user", searchPattern)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(addMealDTO.getSearchProductList()), MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/add-meal?search")
                        .flashAttr("searchPattern", searchPattern)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("addMealDTO", addMealDTO_))
                .andExpect(view().name("add_meal"));

        mockServer.verify();
    }

    @DisplayName("Successfully opening of the add meal page")
    @Test
    @WithMockUser
    public void testSuccessfullyAddMealPageOpening() throws Exception {
        mockMvc.perform(get("/add-meal"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_meal"))
                .andExpect(model().attributeExists("addMealDTO"));
    }

    @DisplayName("Successful addition of a meal")
    @Test
    @WithMockUser
    public void testSuccessfulAdditionOfMeal() throws Exception {
        SearchProductDTO fatSecretProduct = addMealDTO.getProductsToAdd().get(1).getProduct();
        Product fatSecretProductWithId = new Product(2L,  "Product FS", 15, 10.0f, 20.0f, 30.0f);
        fatSecretProductWithId.setId(2L);

        mockServer.expect(requestTo(restURL + "products"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().json(objectMapper.writeValueAsString(fatSecretProduct)))
                .andRespond(withSuccess(objectMapper.writeValueAsString(fatSecretProductWithId), MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo(restURL + "meals"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().json(objectMapper.writeValueAsString(mealPostDTO)))
                .andRespond(withSuccess());

        mockMvc.perform(post("/add-meal?addMeal")
                        .flashAttr("addMealDTO", addMealDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add-meal"));

        mockServer.verify();
    }

    @DisplayName("Successful change meal page opening")
    @Test
    @WithMockUser
    public void testSuccessfulChangeMealPageOpening() throws Exception {
        mockMvc.perform(get("/change-meal"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("changeDTO"))
                .andExpect(view().name("change_meal"));
    }

    @DisplayName("Successfully change meals apply")
    @Test
    @WithMockUser
    public void testSuccessfullyChangeMealsApply() throws Exception {
        ChangeMealDTO changeMealDTO = new ChangeMealDTO(List.of(
                new MealGetDataDTO(1L,
                        new ru.unfatcrew.clientcalorietracker.pojo.dto.ProductDTO(1L, 1L, "user","Product 1", 1, 2.0f, 3.0f, 4.0f, true),
                        10f, "Breakfast"),
                new MealGetDataDTO(2L,
                        new ru.unfatcrew.clientcalorietracker.pojo.dto.ProductDTO(2L, 2L, "user","Product 2", 5, 6.0f, 7.0f, 8.0f, true),
                        100f, "Breakfast")
        ));
        ChangeMealsRequest changeMealsRequest = new ChangeMealsRequest(List.of(
                new MealPutDataDTO(1L, 10f, "Breakfast"),
                new MealPutDataDTO(2L, 100f, "Breakfast")
        ), List.of());
        changeMealsRequest.setUserLogin("user");

        mockServer.expect(requestTo(restURL + "meals"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(MockRestRequestMatchers.content().json(objectMapper.writeValueAsString(changeMealsRequest)))
                .andRespond(withSuccess());

        mockMvc.perform(post("/change-meal?apply")
                        .flashAttr("changeDTO", changeMealDTO)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("change_meal"));

        mockServer.verify();
    }

    @DisplayName("Error opening the product creation page unauthorized")
    @Test
    @WithAnonymousUser
    public void testUnauthorizedCreateProductPageOpen() throws Exception {
        mockMvc.perform(get("/create-product"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Successfully product creation")
    @Test
    @WithMockUser
    public void testSuccessfullyProductCreation() throws Exception {
        mockServer.expect(requestTo(restURL + "products"))
                .andRespond(withSuccess());

        mockMvc.perform(post("/create-product")
                        .flashAttr("product", validProductPostDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/create-product"));

        mockServer.verify();
    }

    @DisplayName("Successfully products change")
    @Test
    @WithMockUser
    public void testSuccessfullyProductsChange() throws Exception {
        ChangeProductsRequest changeProductsRequest = new ChangeProductsRequest(productList, List.of());
        changeProductsRequest.setUserLogin("user");

        mockServer.expect(requestTo(restURL + "products"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(MockRestRequestMatchers.content().json(objectMapper.writeValueAsString(changeProductsRequest)))
                .andRespond(withSuccess());

        mockMvc.perform(post("/my-products?apply")
                        .flashAttr("productDTO", productDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-products"));

        mockServer.verify();
    }

    @Test
    @DisplayName("Successfully my products page opening")
    @WithMockUser
    public void testSuccessfullyMyProductsPageOpening() throws Exception {
        mockServer.expect(requestTo(restURL + "products/user-products?user-login=user&limit=100"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(productList), MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/my-products"))
                .andExpect(model().attribute("productDTO", new ProductDTO(productList)))
                .andExpect(view().name("list_product"));

        mockServer.verify();
    }

    @Test
    @DisplayName("Successfully meals searching")
    @WithMockUser
    public void testSuccessfullyMealsSearching() throws Exception {
        ChangeMealDTO changeMealDTO = new ChangeMealDTO(List.of(
                new MealGetDataDTO(1L,
                        new ru.unfatcrew.clientcalorietracker.pojo.dto.ProductDTO(1L, 1L, "user","Product 1", 1, 2.0f, 3.0f, 4.0f, true),
                        10f, "Breakfast"),
                new MealGetDataDTO(2L,
                        new ru.unfatcrew.clientcalorietracker.pojo.dto.ProductDTO(2L, 2L, "user","Product 2", 5, 6.0f, 7.0f, 8.0f, true),
                        100f, "Breakfast")
        ));
        MealGetDTO mealGetDTO = new MealGetDTO(
                List.of(new MealGetDataDTO(1L,
                                new ru.unfatcrew.clientcalorietracker.pojo.dto.ProductDTO(1L, 1L, "user", "Product 1", 1, 2.0f, 3.0f, 4.0f, true),
                                10f, "Breakfast"),
                        new MealGetDataDTO(2L,
                                new ru.unfatcrew.clientcalorietracker.pojo.dto.ProductDTO(2L, 2L, "user", "Product 2", 5, 6.0f, 7.0f, 8.0f, true),
                                100f, "Breakfast")),
                "user", date);

        mockServer.expect(requestTo(restURL + "meals?user-login=%s&date=%s"
                        .formatted("user", LocalDate.parse(date).format(dateFormatter))))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(mealGetDTO), MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/change-meal?search")
                        .flashAttr("date", date)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("changeDTO", changeMealDTO))
                .andExpect(view().name("change_meal"));

        mockServer.verify();
    }

    @Test
    @DisplayName("Successfully summary form page opening")
    @WithMockUser
    public void testSuccessfullySummaryFormPageOpening() throws Exception {
        mockMvc.perform(get("/summary-form"))
                .andExpect(model().attributeExists("showTable"))
                .andExpect(view().name("get_summary"));
    }

    @Test
    @DisplayName("Successfully summary loading")
    @WithMockUser
    public void testSuccessfullySummaryLoading() throws Exception {
        List<DaySummaryDTO> summaryList = List.of(
                new DaySummaryDTO("2023-12-01", 10.0, 15.0, 20.0, 25.0, 30.0),
                new DaySummaryDTO("2024-03-01", 100.0, 25.0, 18.0, 10.0, 5.0)
        );
        List<DaySummaryDTO> summaryListWithFormattedData = List.of(
                new DaySummaryDTO("01.12.2023", 10.0, 15.0, 20.0, 25.0, 30.0),
                new DaySummaryDTO("01.03.2024", 100.0, 25.0, 18.0, 10.0, 5.0)
        );

        mockServer.expect(requestTo(restURL + "meals/summary?start-date=%s&end-date=%s&user-login=%s"
                        .formatted(LocalDate.parse(startDate).format(dateFormatter), LocalDate.parse(endDate).format(dateFormatter), "user")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(summaryList), MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/summary-form?getTable")
                        .flashAttr("startDate", startDate)
                        .flashAttr("endDate", endDate)
                        .with(csrf()))
                .andExpect(model().attributeExists("showTable"))
                .andExpect(model().attribute("summaryDTO", summaryListWithFormattedData))
                .andExpect(view().name("get_summary"));

        mockServer.verify();
    }

    @Test
    @DisplayName("Successfully summary CSV generation")
    @WithMockUser
    public void testSuccessfullySummaryCSVGeneration() throws Exception {
        List<DaySummaryDTO> summaryList = List.of(
                new DaySummaryDTO("2023-12-01", 10.0, 15.0, 20.0, 25.0, 30.0),
                new DaySummaryDTO("2024-03-01", 100.0, 25.0, 18.0, 10.0, 5.0)
        );
        List<DaySummaryDTO> summaryListWithFormattedData = List.of(
                new DaySummaryDTO("01.12.2023", 10.0, 15.0, 20.0, 25.0, 30.0),
                new DaySummaryDTO("01.03.2024", 100.0, 25.0, 18.0, 10.0, 5.0)
        );

        mockServer.expect(requestTo(restURL + "meals/summary?start-date=%s&end-date=%s&user-login=%s"
                        .formatted(LocalDate.parse(startDate).format(dateFormatter), LocalDate.parse(endDate).format(dateFormatter), "user")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(summaryList), MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/summary-form?getTable")
                        .flashAttr("startDate", startDate)
                        .flashAttr("endDate", endDate)
                        .with(csrf()))
                .andExpect(model().attributeExists("showTable"))
                .andExpect(model().attribute("summaryDTO", summaryListWithFormattedData))
                .andExpect(view().name("get_summary"));

        Resource resource = generateCsv(summaryList);

        mockMvc.perform(post("/summary-form?csv")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .content(resource.getContentAsByteArray())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    private Resource generateCsv(List<DaySummaryDTO> summaryList) throws IOException {
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
        return new ByteArrayResource(strBuilder.toString().getBytes());
    }
}
