package com.readytoplanbe.myapp.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AIClient {

    private static final String GEMINI_API_KEY = "AIzaSyBjhxIwHG-PolSzKgAnPUhQ__Tb-jc1eRY";

    // URL complète vers Gemini (modèle text-bison-001)
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-002:generateContent?key=" + GEMINI_API_KEY;


    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generatePresentation(String prompt) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construction du body JSON manuellement (compatible Java 11)
        String escapedPrompt = prompt
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "");
        String body = "{"
            + "\"contents\": [{"
            + "\"parts\": [{\"text\": \"" + escapedPrompt + "\"}]"
            + "}]"
            + "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.at("/candidates/0/content/parts/0/text").asText();
        } else {
            throw new RuntimeException("Erreur API Gemini : " + response.getStatusCode() + " - " + response.getBody());
        }
    }
}
