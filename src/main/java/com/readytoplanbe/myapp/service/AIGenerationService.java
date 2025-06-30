package com.readytoplanbe.myapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readytoplanbe.myapp.domain.AIGeneratedResponse;
import com.readytoplanbe.myapp.domain.enumeration.EntityType;
import com.readytoplanbe.myapp.repository.AIGeneratedResponseRepository;
import com.readytoplanbe.myapp.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class AIGenerationService {

    private final WebClient webClient;
    private final AIGeneratedResponseRepository responseRepository;

    private final CompanyRepository companyRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AIGenerationService(
        AIGeneratedResponseRepository responseRepository,
        CompanyRepository companyRepository
    ) {
        this.webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
        this.responseRepository = responseRepository;
        this.companyRepository = companyRepository;
    }

    public Mono<String> callGemini(String prompt) {
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        return webClient.post()
            .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(status -> !status.is2xxSuccessful(), response -> {
                return response.bodyToMono(String.class).flatMap(error -> {
                    System.err.println("Erreur Gemini : " + error);
                    return Mono.error(new RuntimeException("Erreur Gemini : " + error));
                });
            })
            .bodyToMono(String.class);
    }


    public String extractText(String jsonResponse) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);
        return root.path("candidates")
            .path(0)
            .path("content")
            .path("parts")
            .path(0)
            .path("text")
            .asText();
    }


    public void generateAndSave(String entityId, EntityType entityType, String prompt) {
        try {
            if (prompt == null || prompt.trim().isEmpty()) {
                throw new IllegalArgumentException("Le prompt ne peut pas être vide ou nul.");
            }

            System.out.println("📤 Appel IA pour " + entityType + " avec prompt: " + prompt);
            String json = callGemini(prompt).block();
            if (json == null) throw new RuntimeException("Réponse vide");

            String result = extractText(json);
            System.out.println("✅ Réponse IA : " + result);

            // 1. Sauvegarder dans la table AIGeneratedResponse (comme déjà)
            AIGeneratedResponse aiResponse = new AIGeneratedResponse();
            aiResponse.setEntityId(entityId);
            aiResponse.setEntityType(entityType);
            aiResponse.setAiResponse(result);
            aiResponse.setPrompt(prompt);
            responseRepository.save(aiResponse);
            System.out.println("💾 Réponse IA sauvegardée dans AIGeneratedResponse");

            // 2. Sauvegarder dans le champ aiPresentation de l'entité Company
            if (entityType == EntityType.COMPANY) {
                companyRepository.findById(entityId).ifPresent(company -> {
                    company.setAiPresentation(result);
                    companyRepository.save(company);
                    System.out.println("📝 aiPresentation sauvegardé dans Company");
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur IA : " + e.getMessage(), e);
        }
    }

    public String generateText(String prompt) {
        try {
            String json = callGemini(prompt).block();
            if (json == null) {
                throw new RuntimeException("Réponse vide");
            }
            return extractText(json);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de génération du texte IA", e);
        }
    }



}
