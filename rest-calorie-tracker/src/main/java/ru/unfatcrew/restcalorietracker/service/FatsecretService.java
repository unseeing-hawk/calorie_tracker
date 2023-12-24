package ru.unfatcrew.restcalorietracker.service;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.crypto.Mac;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;


public class FatsecretService {
    private static final String SERVICE_URL = "https://platform.fatsecret.com/rest/server.api";
    private static final String HTTP_METHOD = "GET";
    private static final String CONSUMER_KEY = "9dfc3d741d7e4778b3c893ee0256deac";
    private static final String CONSUMER_SECRET = "a31ab15b384f48d99c667928a18b84e0";
    private static final String SIGNATURE_METHOD = "HMAC-SHA1";
    private static final String OAUTH_VERSION = "1.0";


    public static String searchInFatsecretByPattern(String pattern, String pageNumber) {
        pattern = pattern.replace("\\s", "");
        String responseBody = "";  

        while(responseBody == "") { 
            long timestamp = Instant.now().getEpochSecond();
            RestTemplate restTemplate = new RestTemplate();

            Map<String, String> params = new HashMap<>(Map.of("method", "foods.search", 
                                        "search_expression", pattern, 
                                        "page_number", pageNumber, 
                                        "format", "json", 
                                        "oauth_consumer_key", CONSUMER_KEY, 
                                        "oauth_signature_method", SIGNATURE_METHOD, 
                                        "oauth_timestamp", Long.toString(timestamp),
                                        "oauth_nonce", "request_" + timestamp, 
                                        "oauth_version", OAUTH_VERSION));

            String signature = getOAuthSignature(params);
            signature = signature.replace("+", "p");
            params.put("oauth_signature", signature);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVICE_URL);
            params.forEach(builder::queryParam);
            String query = builder.build().toUriString();

            RequestEntity<?> request = RequestEntity.get(query).build();
            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            
            if (!response.getBody().toLowerCase().contains("error")) {
                responseBody = response.getBody();
            } 
        }

        return responseBody;
    }

    private static String getOAuthSignature(Map<String, String> params) {
        String encoredURL = UriUtils.encode(SERVICE_URL, StandardCharsets.UTF_8);
        String encodedParameters = concatenateAndEncodeParameters(params);

        String baseString = HTTP_METHOD + "&" + encoredURL + "&" + encodedParameters;
        String hmacKey = CONSUMER_SECRET + "&";

        Mac mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_1, hmacKey.getBytes());
        mac.update(baseString.getBytes());

        return Base64.getEncoder().encodeToString(mac.doFinal());
    }

    private static String concatenateAndEncodeParameters(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();

        String[] paramsArray = params.keySet().stream()
                .sorted()
                .map(key -> key + "=" + params.get(key))
                .toArray(String[]::new);
        builder.append(String.join("&", paramsArray));

        return UriUtils.encode(builder.toString(), StandardCharsets.UTF_8);
    }   
}
