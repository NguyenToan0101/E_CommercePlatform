package org.example.ecommerce.service;


import org.example.ecommerce.common.dto.EmbedResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmbedService {

    private final RestTemplate restTemplate;
    private final String embedUrl;

    public EmbedService(
            RestTemplate restTemplate,
            @Value("${embed.service.url:http://localhost:8000/embed}") String embedUrl
    ) {
        this.restTemplate = restTemplate;
        this.embedUrl = embedUrl;
    }

    public List<Double> fetchEmbedding(String imageUrl) {
        // Build headers & body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,String> body = Map.of("url", imageUrl);
        HttpEntity<Map<String,String>> request = new HttpEntity<>(body, headers);

        // Call Python microservice
        ResponseEntity<EmbedResponse> response =
                restTemplate.postForEntity(embedUrl, request, EmbedResponse.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException(
                    "Embedding service error: " + response.getStatusCode()
            );
        }
        return response.getBody().getVector();
    }
}
