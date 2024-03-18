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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import ru.unfatcrew.clientcalorietracker.rest_service.config.RestTemplateConfig;
import org.springframework.http.HttpMethod;
import java.io.IOException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import ru.unfatcrew.clientcalorietracker.pojo.entity.User;

@WebMvcTest
@ContextConfiguration(classes = {RestTemplateConfig.class})
@ComponentScan("ru.unfatcrew.clientcalorietracker")
public class SettingsTest {
    private String restURL;
    private WebApplicationContext context;
    private WebClient webClient;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;

    @Autowired
    public SettingsTest(WebApplicationContext context,
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

    @DisplayName("Successfully opening of the settings page")
    @Test
    @WithMockUser
    public void testSuccessfullyOpeningOfTheSettingsPage() throws IOException  {
        User user = new User( 1L,"FirstName LastName MiddleName", "user", "12345678", "60.0");
        mockServer.expect(requestTo(restURL + "users?login=user"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(user), MediaType.APPLICATION_JSON));

        HtmlPage page = webClient.getPage("http://localhost/settings");
        Assertions.assertEquals("Settings", page.getTitleText());

        final HtmlForm form = page.getFirstByXPath("//form[@id='settingsForm']");

        HtmlInput inputName = form.getFirstByXPath("//input[@id='name']");
        HtmlInput inputWeight = form.getFirstByXPath("//input[@id='weight']");

        Assertions.assertEquals("FirstName LastName MiddleName", inputName.getValue());
        Assertions.assertEquals("60.0", inputWeight.getValue());

        mockServer.verify();
    }
}
