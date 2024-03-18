package ru.unfatcrew.clientcalorietracker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import ru.unfatcrew.clientcalorietracker.pojo.dto.MealPostDTO;
import ru.unfatcrew.clientcalorietracker.pojo.dto.MealPostDataDTO;
import ru.unfatcrew.clientcalorietracker.pojo.dto.SearchProductDTO;
import ru.unfatcrew.clientcalorietracker.rest_service.config.RestTemplateConfig;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static ru.unfatcrew.clientcalorietracker.utils.DateUtils.*;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@WebMvcTest
@ContextConfiguration(classes = {RestTemplateConfig.class})
@ComponentScan("ru.unfatcrew.clientcalorietracker")
public class AddMealTests {
    private String restURL;
    private WebApplicationContext context;
    private WebClient webClient;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;
    private List<SearchProductDTO> searchedProducts;
    private MealPostDTO mealPostDTO;
    private String mealTimeDate;

    @Autowired
    public AddMealTests(WebApplicationContext context,
                        RestTemplate restTemplate,
                        @Value("${application.rest.api.url}") String restURL) {
        this.context = context;
        this.restTemplate = restTemplate;
        this.restURL = restURL;
        this.objectMapper = new ObjectMapper();
        this.mealTimeDate = "2024-03-18";
    }

    @BeforeEach
    public void init() {
        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        mockServer = MockRestServiceServer.createServer(restTemplate);

        searchedProducts = List.of(new SearchProductDTO(1L, "user", "Water 1", 10, 12.0f, 15.0f, 20.0f),
                new SearchProductDTO(2L, "user", "Water 2", 15, 20.0f, 25.0f, 50.0f));
        mealPostDTO = new MealPostDTO(List.of(new MealPostDataDTO(1L, 10.0f)), "user",
                LocalDate.parse(mealTimeDate).format(dateFormatter), "Breakfast");
    }

    @Test
    @DisplayName("Successful addition of a meal")
    @WithMockUser
    public void testSuccessfulAdditionOfMeal() throws IOException {
        String searchPattern = "Water";

        mockServer.expect(requestTo(restURL + "products/search-products?user-login=%s&pattern=%s"
                .formatted("user", searchPattern)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(searchedProducts), MediaType.APPLICATION_JSON));

        HtmlPage page = webClient.getPage("http://localhost/add-meal");

        HtmlInput searchInput = page.getFirstByXPath("//input[@name=\"searchPattern\"]");
        searchInput.setValue("Water");

        HtmlButton searchButton = page.getFirstByXPath("//button[@name=\"search\"]");
        page = searchButton.click();

        mockServer.verify();

        List<HtmlTableRow> productTableRows = page.getByXPath("//table[@id=\"products-table\"]/tbody/tr");
        Assertions.assertEquals(searchedProducts.size(), productTableRows.size());

        HtmlInput checkbox = page.getFirstByXPath("//*[@type=\"checkbox\"]");
        checkbox.click();

        HtmlButton addProductButton = page.getFirstByXPath("//button[@name=\"addProductBtn\"]");
        page = addProductButton.click();

        HtmlInput chosenProductInput = page.getFirstByXPath("//table[@id=\"list-of-chosen-product-table\"]/tbody/tr/td/input");
        chosenProductInput.setValue("10.0");

        HtmlButton addToMealButton = page.getFirstByXPath("//button[@id=\"btn-add-meal\"]");
        addToMealButton.click();

        HtmlInput dateInput = page.getFirstByXPath("//input[@id=\"date\"]");
        dateInput.setValue(mealTimeDate);

        mockServer.reset();
        mockServer.expect(requestTo(restURL + "meals"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(objectMapper.writeValueAsString(mealPostDTO)))
                .andRespond(withSuccess());

        HtmlButton addMealButton = page.getFirstByXPath("//button[@name=\"addMeal\"]");
        addMealButton.click();

        mockServer.verify();
    }
}
