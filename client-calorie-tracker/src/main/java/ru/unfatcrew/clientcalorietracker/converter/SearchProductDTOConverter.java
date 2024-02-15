package ru.unfatcrew.clientcalorietracker.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.unfatcrew.clientcalorietracker.pojo.dto.SearchProductDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class SearchProductDTOConverter implements Converter<String, SearchProductDTO> {
    @Override
    public SearchProductDTO convert(String source) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(Base64.getDecoder().decode(source.getBytes(StandardCharsets.UTF_8)), SearchProductDTO.class);
        } catch (IOException exception) {
            return new SearchProductDTO();
        }
    }
}
