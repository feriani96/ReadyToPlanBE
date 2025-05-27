package com.readytoplanbe.myapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        String language = dto.getLanguages().toString().toLowerCase();
        boolean isEnglish = language.contains("english");

        String introTemplate;
        String planInstruction;

        if (isEnglish) {
            introTemplate = "You are an expert business consultant. " +
                "Based on the following information about a company, generate a clear, professional and detailed business plan presentation " +
                "that helps entrepreneurs create their startup or company.\n\n" +
                "Please respect the input data and generate the plan in English.\n\n" +
                "➤ Company Name: %s\n" +
                "➤ Description: %s\n" +
                "➤ Start Date: %s\n" +
                "➤ Country: %s\n" +
                "➤ Languages: %s\n" +
                "➤ Anticipated Project Size: %s\n" +
                "➤ Currency: %s\n\n";
            planInstruction = "First, provide a detailed table of contents outlining all the following sections:\n" +
                "1. Company Description Details\n" +
                "2. Risk Overview\n" +
                "3. Market Analysis\n" +
                "4. User Target\n" +
                "5. Investment Needs\n" +
                "6. Company Valuation\n" +
                "7. Stress Test and Scenarios\n" +
                "8. Legal Clauses\n" +
                "9. Product Roadmap\n" +
                "10. Founding Team\n" +
                "11. Strategic Partnerships\n" +
                "12. KPIs\n" +
                "13. Acquisition Strategies\n" +
                "14. CAC vs LTV\n" +
                "15. Marketing Channels\n" +
                "16. Technical Architecture\n" +
                "17. Technological Advantages\n" +
                "18. Critical Dependencies\n\n" +
                "After the table of contents, expand each section in detail based on the information you have or generate the missing parts professionally.";
        } else {
            // Par défaut, français
            introTemplate = "Vous êtes un expert consultant en création d'entreprise. " +
                "En vous basant sur les informations suivantes concernant une société, générez une présentation claire, professionnelle et détaillée d'un plan d'affaires " +
                "qui aide les entrepreneurs à créer leur startup ou entreprise.\n\n" +
                "Merci de respecter les données fournies et de générer la présentation en français.\n\n" +
                "➤ Nom de l'entreprise : %s\n" +
                "➤ Description : %s\n" +
                "➤ Date de création : %s\n" +
                "➤ Pays : %s\n" +
                "➤ Langues : %s\n" +
                "➤ Taille prévue du projet : %s\n" +
                "➤ Devise : %s\n\n";
            planInstruction = "D'abord, fournissez une table des matières détaillée listant toutes les sections suivantes :\n" +
                "1. Détails de la description de l'entreprise\n" +
                "2. Aperçu des risques\n" +
                "3. Analyse de marché\n" +
                "4. Cible utilisateur\n" +
                "5. Besoins en investissement\n" +
                "6. Évaluation de l'entreprise\n" +
                "7. Test de résistance et scénarios\n" +
                "8. Clauses légales\n" +
                "9. Feuille de route du produit\n" +
                "10. Équipe fondatrice\n" +
                "11. Partenariats stratégiques\n" +
                "12. KPIs\n" +
                "13. Stratégies d’acquisition\n" +
                "14. CAC vs LTV\n" +
                "15. Canaux marketing\n" +
                "16. Architecture technique\n" +
                "17. Avantages technologiques\n" +
                "18. Dépendances critiques\n\n" +
                "Après la table des matières, développez chaque section en détail, en vous basant sur les informations disponibles ou en générant professionnellement les parties manquantes.";
        }

        return String.format(introTemplate,
            dto.getCompanyName(),
            dto.getCompanyDescription(),
            dto.getCompanyStartDate(),
            dto.getCountry(),
            dto.getLanguages(),
            dto.getAnticipatedProjectSize(),
            dto.getCurrency()
        ) + planInstruction.replace("\n\n", "\n\n===SLIDE===\n\n");

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
            String jsonResponse = callGemini(prompt, apiKey).block();

            if (jsonResponse == null || jsonResponse.isEmpty()) {
                throw new RuntimeException("Réponse vide de l'API Gemini.");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            // Accéder à candidates[0].content.parts[0].text
            JsonNode textNode = root.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text");

            if (textNode.isMissingNode()) {
                throw new RuntimeException("Le champ texte attendu est manquant dans la réponse de Gemini.");
            }

            return textNode.asText(); // ✅ Affichage propre sans JSON autour

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'analyse de la réponse Gemini : " + e.getMessage(), e);
        }
    }



}
