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
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
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

@WebMvcTest
@ContextConfiguration(classes = {RestTemplateConfig.class})
@ComponentScan("ru.unfatcrew.clientcalorietracker")
public class SigupTests {
    private String restURL;
    private WebApplicationContext context;
    private WebClient webClient;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;

    @Autowired
    public SigupTests(WebApplicationContext context,
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

    @DisplayName("Successfully user register")
    @Test
    @WithAnonymousUser
    public void testSuccessfullyUserRegister() throws IOException  {
        mockServer.expect(requestTo(restURL + "users"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        HtmlPage page = webClient.getPage("http://localhost/register");
        Assertions.assertEquals("Sign up", page.getTitleText());

        final HtmlForm form = page.getFirstByXPath("//form[@id='registerForm']");

        HtmlInput inputName = form.getFirstByXPath("//input[@id='name']");
        inputName.setValueAttribute("FirstName LastName MiddleName");
        HtmlInput inputWeight = form.getFirstByXPath("//input[@id='weight']");
        inputWeight.setValueAttribute("60.0");
        HtmlInput inputUsername = form.getFirstByXPath("//input[@id='username']");
        inputUsername.setValueAttribute("testuser");
        HtmlInput inputPassword = form.getFirstByXPath("//input[@id='password']");
        inputPassword.setValueAttribute("password");

        final HtmlButton button = form.getFirstByXPath("//button[@type='button']");
        final HtmlPage nextPage = button.click();

        Assertions.assertEquals("my-webapp", nextPage.getTitleText());

        mockServer.verify();
    }

    @DisplayName("Error user register with invalid name")
    @Test
    @WithAnonymousUser
    public void testErrorUserRegister() throws Exception {
        mockServer.expect(requestTo(restURL + "users"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));
        HtmlPage page = webClient.getPage("http://localhost/register");
        Assertions.assertEquals("Sign up", page.getTitleText());

        final HtmlForm form = page.getFirstByXPath("//form[@id='registerForm']");

        HtmlInput inputName = form.getFirstByXPath("//input[@id='name']");
        inputName.setValueAttribute("irstName LastName MiddleName");
        HtmlInput inputWeight = form.getFirstByXPath("//input[@id='weight']");
        inputWeight.setValueAttribute("60.0");
        HtmlInput inputUsername = form.getFirstByXPath("//input[@id='username']");
        inputUsername.setValueAttribute("testuser");
        HtmlInput inputPassword = form.getFirstByXPath("//input[@id='password']");
        inputPassword.setValueAttribute("password");

        final HtmlButton button = form.getFirstByXPath("//button[@type='button']");
        Assertions.assertEquals("Sign up", button.getTextContent());
        final HtmlPage nextPage = button.click();

        final HtmlSpan errorSpan = form.getFirstByXPath("//span[@id='name-error-span']");
        Assertions.assertEquals("Format error! The first letters of the name must be capitalized.", errorSpan.getTextContent());
    }
}
