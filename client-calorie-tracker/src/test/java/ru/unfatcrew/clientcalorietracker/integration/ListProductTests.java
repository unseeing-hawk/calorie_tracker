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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;
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
    }

    @Test
    @DisplayName("Successful display of the user's product list")
    @WithMockUser
    public void test() throws IOException {
        List<Product> products = List.of(new Product(1L, "Product 1", 12, 20.0f, 15.0f, 5.0f),
                new Product(2L, "Product 2", 255, 125.0f, 55.0f, 0.0f));
        String expectedPageTitle = "List product";

        mockServer.expect(requestTo(restURL + "products/user-products?user-login=user&limit=100"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(products), MediaType.APPLICATION_JSON));

        HtmlPage page = webClient.getPage("http://localhost/my-products");
        Assertions.assertEquals(expectedPageTitle, page.getTitleText());

        List<HtmlTableRow> rows = page.getByXPath("//table/tbody/tr");
        Assertions.assertEquals(products.size(), rows.size());

        for (int i = 0; i < products.size(); i++) {
            List<HtmlElement> inputList = rows.get(i).getElementsByTagName("input");

            Assertions.assertEquals(products.get(i).getName(),
                    ((HtmlInput) inputList.get(0)).getValue());
            Assertions.assertEquals(products.get(i).getCalories(),
                    Integer.parseInt(((HtmlInput) inputList.get(1)).getValue()));
            Assertions.assertEquals(products.get(i).getProteins(),
                    Float.parseFloat(((HtmlInput) inputList.get(2)).getValue()));
            Assertions.assertEquals(products.get(i).getFats(),
                    Float.parseFloat(((HtmlInput) inputList.get(3)).getValue()));
            Assertions.assertEquals(products.get(i).getCarbohydrates(),
                    Float.parseFloat(((HtmlInput) inputList.get(4)).getValue()));
        }

        mockServer.verify();
    }
}
