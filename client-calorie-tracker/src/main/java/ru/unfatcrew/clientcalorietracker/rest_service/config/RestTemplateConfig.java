package ru.unfatcrew.clientcalorietracker.rest_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.unfatcrew.clientcalorietracker.rest_service.request_initializer.HttpRequestInitializer;

import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rest = new RestTemplate();
        rest.setClientHttpRequestInitializers(List.of(new HttpRequestInitializer()));
        return rest;
    }
}
