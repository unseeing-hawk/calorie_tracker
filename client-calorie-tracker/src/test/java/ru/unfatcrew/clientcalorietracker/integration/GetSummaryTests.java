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
import ru.unfatcrew.clientcalorietracker.pojo.dto.DaySummaryDTO;
import ru.unfatcrew.clientcalorietracker.rest_service.config.RestTemplateConfig;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import static ru.unfatcrew.clientcalorietracker.utils.DateUtils.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebMvcTest
@ContextConfiguration(classes = {RestTemplateConfig.class})
@ComponentScan("ru.unfatcrew.clientcalorietracker")
public class GetSummaryTests {
    private String restURL;
    private WebApplicationContext context;
    private WebClient webClient;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;
    private List<DaySummaryDTO> summary;

    @Autowired
    public GetSummaryTests(WebApplicationContext context,
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
        summary = List.of(new DaySummaryDTO("2021-03-20", 10.0, 11.0, 12.0, 13.0, 14.0),
                new DaySummaryDTO("2022-03-20", 100.0, 110.0, 120.0, 130.0, 140.0));
    }

    @Test
    @DisplayName("Successful loading of the summary table")
    @WithMockUser
    public void testSuccessfulLoadingOfSummaryTable() throws IOException {
        String startDate = "2020-03-20";
        String endDate = "2024-03-18";
        String expectedPageTitle = "Get summary";

        HtmlPage page = webClient.getPage("http://localhost/summary-form");
        Assertions.assertEquals(expectedPageTitle, page.getTitleText());

        mockServer.expect(requestTo(restURL + "meals/summary?start-date=%s&end-date=%s&user-login=%s"
                        .formatted(LocalDate.parse(startDate).format(dateFormatter),
                                LocalDate.parse(endDate).format(dateFormatter),
                                "user")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(summary), MediaType.APPLICATION_JSON));

        HtmlInput startDateInput = page.getFirstByXPath("//input[@name=\"startDate\"]");
        HtmlInput endDateInput = page.getFirstByXPath("//input[@name=\"endDate\"]");

        startDateInput.setValue(startDate);
        endDateInput.setValue(endDate);

        HtmlButton button = page.getFirstByXPath("//button[@name=\"getTable\"]");
        page = button.click();

        List<HtmlTableRow> rows = page.getByXPath("//table/tbody/tr");
        Assertions.assertEquals(summary.size(), rows.size());

        for (int i = 0; i < rows.size(); i++) {
            List<HtmlElement> columns = rows.get(i).getElementsByTagName("td");

            Assertions.assertEquals(LocalDate.parse(summary.get(i).getDate()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    columns.get(0).getTextContent());
            Assertions.assertEquals(summary.get(i).getWeight(),
                    Double.parseDouble(columns.get(1).getTextContent()));
            Assertions.assertEquals(summary.get(i).getCalories(),
                    Double.parseDouble(columns.get(2).getTextContent()));
            Assertions.assertEquals(summary.get(i).getProteins(),
                    Double.parseDouble(columns.get(3).getTextContent()));
            Assertions.assertEquals(summary.get(i).getFats(),
                    Double.parseDouble(columns.get(4).getTextContent()));
            Assertions.assertEquals(summary.get(i).getCarbohydrates(),
                    Double.parseDouble(columns.get(5).getTextContent()));
        }

        mockServer.verify();
    }
}
