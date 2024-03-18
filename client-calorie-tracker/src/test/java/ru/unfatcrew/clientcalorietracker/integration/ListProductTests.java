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
import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;
import ru.unfatcrew.clientcalorietracker.pojo.requests.ChangeProductsRequest;
import ru.unfatcrew.clientcalorietracker.rest_service.config.RestTemplateConfig;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@WebMvcTest
@ContextConfiguration(classes = {RestTemplateConfig.class})
@ComponentScan("ru.unfatcrew.clientcalorietracker")
public class ListProductTests {
    private String restURL;
    private WebApplicationContext context;
    private WebClient webClient;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;
    private List<Product> validProducts;
    private List<Product> productsBeforeUpdate;

    @Autowired
    public ListProductTests(WebApplicationContext context,
                            RestTemplate restTemplate,
                            @Value("${application.rest.api.url}") String restURL) {
        this.context = context;
        this.restTemplate = restTemplate;
        this.restURL = restURL;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void init() {
        webClient = MockMvcWebClientBuilder
                .webAppContextSetup(context)
                .build();
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        mockServer = MockRestServiceServer.createServer(restTemplate);

        validProducts = List.of(new Product((Long) null, "Product 1", 12, 20.0f, 15.0f, 5.0f),
                new Product((Long) null, "Product 2", 255, 125.0f, 55.0f, 0.0f));
        productsBeforeUpdate = List.of(new Product((Long) null, "Updated  product  1", 12, 25.0f, 15.0f, 5.0f),
                new Product((Long) null, "Updated  product  2", 255, 120.0f, 55.0f, 0.0f));
    }

    @Test
    @DisplayName("Successful display of the user's product list")
    @WithMockUser
    public void testSuccessfulDisplayOfUserProducts() throws IOException {
        String expectedPageTitle = "List product";

        mockServer.expect(requestTo(restURL + "products/user-products?user-login=user&limit=100"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(validProducts), MediaType.APPLICATION_JSON));

        HtmlPage page = webClient.getPage("http://localhost/my-products");
        Assertions.assertEquals(expectedPageTitle, page.getTitleText());

        List<HtmlTableRow> rows = page.getByXPath("//table/tbody/tr");
        Assertions.assertEquals(validProducts.size(), rows.size());

        for (int i = 0; i < validProducts.size(); i++) {
            List<HtmlElement> inputList = rows.get(i).getElementsByTagName("input");

            Assertions.assertEquals(validProducts.get(i).getName(),
                    ((HtmlInput) inputList.get(0)).getValue());
            Assertions.assertEquals(validProducts.get(i).getCalories(),
                    Integer.parseInt(((HtmlInput) inputList.get(1)).getValue()));
            Assertions.assertEquals(validProducts.get(i).getProteins(),
                    Float.parseFloat(((HtmlInput) inputList.get(2)).getValue()));
            Assertions.assertEquals(validProducts.get(i).getFats(),
                    Float.parseFloat(((HtmlInput) inputList.get(3)).getValue()));
            Assertions.assertEquals(validProducts.get(i).getCarbohydrates(),
                    Float.parseFloat(((HtmlInput) inputList.get(4)).getValue()));
        }

        mockServer.verify();
    }

    @Test
    @DisplayName("Successful change of the user's products")
    @WithMockUser
    public void testSuccessfulChangeOfUserProducts() throws IOException {
        String expectedPageTitle = "List product";
        ChangeProductsRequest expectedChangeProductRequest = new ChangeProductsRequest(productsBeforeUpdate, List.of());
        expectedChangeProductRequest.setUserLogin("user");

        mockServer.expect(requestTo(restURL + "products/user-products?user-login=user&limit=100"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(validProducts), MediaType.APPLICATION_JSON));

        HtmlPage page = webClient.getPage("http://localhost/my-products");
        Assertions.assertEquals(expectedPageTitle, page.getTitleText());

        mockServer.verify();

        mockServer.reset();
        mockServer.expect(requestTo(restURL + "products"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedChangeProductRequest)))
                .andRespond(withSuccess());

        mockServer.expect(requestTo(restURL + "products/user-products?user-login=user&limit=100"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        List<HtmlInput> nameInputs = page.getByXPath("//input[contains(@id, \"name\")]");
        List<HtmlInput> proteinInputs = page.getByXPath("//input[contains(@id, \"proteins\")]");
        Assertions.assertEquals(validProducts.size(), nameInputs.size());
        Assertions.assertEquals(validProducts.size(), proteinInputs.size());

        nameInputs.get(0).setValue("Updated product 1");
        nameInputs.get(1).setValue("Updated product 2");
        proteinInputs.get(0).setValue("25.0");
        proteinInputs.get(1).setValue("120.0");

        HtmlButton applyButton = page.getFirstByXPath("//button[@name=\"apply\"]");
        applyButton.click();

        mockServer.verify();
    }
}
