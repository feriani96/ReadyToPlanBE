package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.service.dto.BusinessPlanInputDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BusinessPlanGeneratorService {

    private final WebClient webClient;

    public BusinessPlanGeneratorService(WebClient webClient) {
        this.webClient = webClient;
    }

    private String createPromptFromDTO(BusinessPlanInputDTO dto) {
        String promptTemplate = "Génère un plan d'affaires clair et professionnel basé sur les informations suivantes :\n\n" +
            "➤ Nom de l'entreprise : %s\n" +
            "➤ Description : %s\n" +
            "➤ Date de création : %s\n" +
            "➤ Company Description Details : %s\n" +
            "➤ Aperçu des risques : %s\n" +
            "➤ Analyse de marché : %s\n" +
            "➤ Cible utilisateur : %s\n" +
            "➤ Besoins en investissement : %s\n" +
            "➤ Évaluation de l'entreprise : %s\n" +
            "➤ Stress Test et Scénarios : %s\n" +
            "➤ Clauses légales : %s\n" +
            "➤ Roadmap produit : %s\n" +
            "➤ Équipe fondatrice : %s\n" +
            "➤ Partenariats stratégiques : %s\n" +
            "➤ KPIs : %s\n" +
            "➤ Stratégies d’acquisition : %s\n" +
            "➤ CAC vs LTV : %s\n" +
            "➤ Canaux marketing : %s\n" +
            "➤ Architecture technique : %s\n" +
            "➤ Avantages technologiques : %s\n" +
            "➤ Dépendances critiques : %s";

        return String.format(
            promptTemplate,
            dto.getCompanyName(),
            dto.getCompanyDescription(),
            dto.getCompanyStartDate(),
            dto.getCompanyDescriptionDetails(),
            dto.getRiskOverview(),
            dto.getMarketAnalysis(),
            dto.getUserTarget(),
            dto.getInvestmentNeeds(),
            dto.getCompanyValuation(),
            dto.getStressTestData(),
            dto.getLegalClauses(),
            dto.getRoadmap(),
            dto.getFoundingTeam(),
            dto.getStrategicPartners(),
            dto.getKpis(),
            dto.getAcquisitionStrategies(),
            dto.getCacVsLtv(),
            dto.getMarketingChannels(),
            dto.getTechArchitecture(),
            dto.getTechAdvantages(),
            dto.getCriticalDependencies()
        );
    }


    public Mono<String> callGemini(String prompt, String apiKey) {
        WebClient client = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        return client.post()
            .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class);
    }


    public String generatePresentation(BusinessPlanInputDTO dto, String apiKey) {
        String prompt = createPromptFromDTO(dto);
        try {
            Optional<String> response = Optional.ofNullable(callGemini(prompt, apiKey).block());

            return response.orElseThrow(() ->
                new RuntimeException("Réponse vide de l'API Gemini lors de la génération du plan d'affaires"));

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du plan d'affaires : " + e.getMessage(), e);
        }
    }




}
