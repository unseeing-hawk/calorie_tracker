package ru.unfatcrew.clientcalorietracker.integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import ru.unfatcrew.clientcalorietracker.rest_service.config.RestTemplateConfig;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@WebMvcTest
@ContextConfiguration(classes = {RestTemplateConfig.class})
@ComponentScan("ru.unfatcrew.clientcalorietracker")
public class AddProductTest {
    private String restURL;
    private WebApplicationContext context;
    private WebClient webClient;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;

    @Autowired
    public AddProductTest(WebApplicationContext context,
                     RestTemplate restTemplate,
                     @Value("${application.rest.api.url}") String restURL,
                     MockMvc mockMvc) {
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

    @DisplayName("Successfully create product")
    @Test
    @WithMockUser()
    public void testSuccessfullyCreateProduct() throws Exception {

        mockServer.expect(requestTo(restURL + "products"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        HtmlPage page = webClient.getPage("http://localhost/create-product");
        Assertions.assertEquals("Add product", page.getTitleText());
        final HtmlForm form = page.getFirstByXPath("//form[@id='createProductForm']");

        HtmlInput inputName = form.getFirstByXPath("//input[@id='name']");
        inputName.setValueAttribute("Apple");
        HtmlInput inputCalories = form.getFirstByXPath("//input[@id='calories']");
        inputCalories.setValueAttribute("100");
        HtmlInput inputProteins = form.getFirstByXPath("//input[@id='proteins']");
        inputProteins.setValueAttribute("10.0");
        HtmlInput inputFats = form.getFirstByXPath("//input[@id='fats']");
        inputFats.setValueAttribute("5.0");
        HtmlInput inputCarbohydrates = form.getFirstByXPath("//input[@id='carbohydrates']");
        inputCarbohydrates.setValueAttribute("2.5");

        final HtmlButton button = form.getFirstByXPath("//button[@type='button']");
        final HtmlPage nextPage = button.click();

        mockServer.verify();
    }
}
