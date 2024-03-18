package ru.unfatcrew.clientcalorietracker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
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
import ru.unfatcrew.clientcalorietracker.pojo.dto.MealGetDTO;
import ru.unfatcrew.clientcalorietracker.pojo.dto.MealGetDataDTO;
import ru.unfatcrew.clientcalorietracker.pojo.dto.MealPutDataDTO;
import ru.unfatcrew.clientcalorietracker.pojo.dto.ProductDTO;
import ru.unfatcrew.clientcalorietracker.pojo.requests.ChangeMealsRequest;
import ru.unfatcrew.clientcalorietracker.rest_service.config.RestTemplateConfig;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import static ru.unfatcrew.clientcalorietracker.utils.DateUtils.dateFormatter;

@WebMvcTest
@ContextConfiguration(classes = {RestTemplateConfig.class})
@ComponentScan("ru.unfatcrew.clientcalorietracker")
public class ChangeMealTests {
    private String restURL;
    private WebApplicationContext context;
    private WebClient webClient;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;
    private MealGetDTO mealGetDTO;
    private ChangeMealsRequest changeMealsRequest;
    private String searchDate;

    @Autowired
    public ChangeMealTests(WebApplicationContext context,
                           RestTemplate restTemplate,
                           @Value("${application.rest.api.url}") String restURL) {
        this.context = context;
        this.restTemplate = restTemplate;
        this.restURL = restURL;
        this.objectMapper = new ObjectMapper();
        this.searchDate = "2023-03-18";
    }

    @BeforeEach
    public void init() {
        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        mockServer = MockRestServiceServer.createServer(restTemplate);

        mealGetDTO = new MealGetDTO(List.of(new MealGetDataDTO(1L,
                new ProductDTO(1L, null, "user", "Product", 10, 15.0f, 5.0f, 10.0f, true),
                10.0f, "Breakfast")), "user", searchDate);
        changeMealsRequest = new ChangeMealsRequest(List.of(new MealPutDataDTO(1L, 15.0f, "Breakfast")), List.of(), "user");
    }

    @Test
    @DisplayName("Successful meal change")
    @WithMockUser
    public void testSuccessfulMealChange() throws IOException {
        HtmlPage page = webClient.getPage("http://localhost/change-meal");

        HtmlInput searchInput = page.getFirstByXPath("//input[@id=\"date\"]");
        searchInput.setValue(searchDate);

        mockServer.expect(requestTo(restURL + "meals?user-login=%s&date=%s"
                .formatted("user", LocalDate.parse(searchDate).format(dateFormatter))))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(mealGetDTO), MediaType.APPLICATION_JSON));

        HtmlButton searchButton = page.getFirstByXPath("//button[@name=\"search\"]");
        page = searchButton.click();

        mockServer.verify();

        List<HtmlTableRow> rows = page.getByXPath("//table/tbody/tr");
        Assertions.assertEquals(mealGetDTO.getMealGetDataList().size(), rows.size());

        HtmlInput weightInput = rows.get(0).getFirstByXPath("//input[contains(@name, \"weight\")]");
        weightInput.setValue("15.0");

        mockServer.reset();
        mockServer.expect(requestTo(restURL + "meals"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(objectMapper.writeValueAsString(changeMealsRequest)))
                .andRespond(withSuccess());

        HtmlButton applyButton = page.getFirstByXPath("//button[@name=\"apply\"]");
        applyButton.click();

        mockServer.verify();
    }
}
