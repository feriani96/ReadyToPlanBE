package com.readytoplanbe.myapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readytoplanbe.myapp.domain.Slide;
import com.readytoplanbe.myapp.service.dto.BusinessPlanInputDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BusinessPlanGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(BusinessPlanGeneratorService.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public BusinessPlanGeneratorService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String createPromptFromDTO(BusinessPlanInputDTO dto) {
        String language = dto.getLanguages().toString().toLowerCase();
        boolean isEnglish = language.contains("english");

        String promptTemplate;
        String sectionsInstruction;

        if (isEnglish) {
            promptTemplate = "You are an expert business consultant. Generate a professional business plan presentation with these strict rules:\n\n" +
                "STRUCTURE RULES:\n" +
                "1. Use exactly '===SLIDE===' on a separate line to separate slides\n" +
                "2. First slide must be '# [CompanyName] Business Plan Presentation' followed by '## Table of Contents' with ACTUAL section titles\n" +
                "3. Each main section starts with '# [SectionNumber]. [SectionTitle]' on a new slide\n" +
                "4. Table of Contents must EXACTLY match these section titles\n" +
                "5. DO NOT include any technical markup labels like 'markdown'\n" +
                "6. Content should begin immediately after the title without title repetition\n" +
                "7. Use bullet points (-) for lists and **bold** for emphasis\n" +
                "8. Keep subsections (##) within the same slide\n\n" +
                "COMPANY INFORMATION:\n" +
                "➤ Company Name: %s\n" +
                "➤ Description: %s\n" +
                "➤ Start Date: %s\n" +
                "➤ Country: %s\n" +
                "➤ Languages: %s\n" +
                "➤ Anticipated Project Size: %s %s\n\n" +
                "CONTENT REQUIREMENTS:\n";

            sectionsInstruction = "# %s Business Plan Presentation\n\n" +
                "## Table of Contents\n\n" +
                "1. Company Overview\n" +
                "2. Business Description\n" +
                "3. Market Analysis\n" +
                "4. Organization & Management\n" +
                "5. Service/Product Line\n" +
                "6. Marketing & Sales\n" +
                "7. Funding Requirements\n" +
                "8. Financial Projections\n" +
                "9. Risk Analysis\n" +
                "10. Appendix\n\n" +
                "===SLIDE===\n\n" +
                "Generate ONLY professional content:\n" +
                "1. Without any technical markup labels\n" +  // Ajout
                "2. With exact title matching between TOC and slides\n" +
                "3. Direct content after title (3-5 key points)\n" +
                "4. Using company-specific details provided";
        } else {
            promptTemplate = "Vous êtes un expert en création d'entreprise. Générez un contenu professionnel de plan d'affaires avec ces règles strictes :\n\n" +
                "RÈGLES DE STRUCTURE :\n" +
                "1. Utilisez exactement '===SLIDE===' entre les sections\n" +
                "2. Première slide : '# Présentation du Plan d'Affaires [NomEntreprise]' avec '## Table des Matières'\n" +
                "3. Chaque section commence par '# [NuméroSection]. [TitreSection]' sur nouvelle slide\n" +
                "4. La Table des Matières doit CORRESPONDRE exactement aux titres\n" +
                "5. NE PAS inclure de balises techniques comme 'markdown'\n" +  // Ajout
                "6. Contenu doit commencer immédiatement après le titre\n" +
                "7. Utilisez des puces (-) pour les listes\n" +
                "8. Sous-sections (##) dans la même slide\n\n" +
                "INFORMATIONS ENTREPRISE :\n" +
                "➤ Nom Entreprise : %s\n" +
                "➤ Description : %s\n" +
                "➤ Date Création : %s\n" +
                "➤ Pays : %s\n" +
                "➤ Langues : %s\n" +
                "➤ Taille Projet : %s %s\n\n" +
                "EXIGENCES DE CONTENU :\n";

            sectionsInstruction = "# Présentation du Plan d'Affaires %s\n\n" +
                "## Table des Matières\n\n" +
                "1. Aperçu de l'Entreprise\n" +
                "2. Description de l'Activité\n" +
                "3. Analyse de Marché\n" +
                "4. Organisation & Management\n" +
                "5. Ligne de Produits/Services\n" +
                "6. Stratégie Marketing & Ventes\n" +
                "7. Besoins de Financement\n" +
                "8. Projections Financières\n" +
                "9. Analyse des Risques\n" +
                "10. Annexes\n\n" +
                "===SLIDE===\n\n" +
                "Contenu professionnel uniquement :\n" +
                "1. Sans aucune balise technique visible\n" +  // Ajout
                "2. Table des matières synchronisée avec les titres\n" +
                "3. 3-5 points clés par section\n" +
                "4. Détails spécifiques à l'entreprise";
        }

        return String.format(promptTemplate,
            dto.getCompanyName(),
            dto.getCompanyDescription(),
            dto.getCompanyStartDate(),
            dto.getCountry(),
            dto.getLanguages(),
            dto.getAnticipatedProjectSize(),
            dto.getCurrency()
        ) + String.format(sectionsInstruction, dto.getCompanyName());
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
        log.info("Génération de la présentation pour {}", dto.getCompanyName());

        String prompt = createPromptFromDTO(dto);

        log.debug("Prompt généré:\n{}", prompt);

        try {
            String jsonResponse = callGemini(prompt, apiKey)
                .doOnSubscribe(s -> log.debug("Appel à l'API Gemini initié"))
                .block();

            log.debug("Réponse brute de Gemini: {}", jsonResponse);
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                throw new RuntimeException("Réponse vide de l'API Gemini.");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            JsonNode textNode = root.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text");

            if (textNode.isMissingNode()) {
                throw new RuntimeException("Le champ texte attendu est manquant dans la réponse de Gemini.");
            }

            return textNode.asText();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'analyse de la réponse Gemini : " + e.getMessage(), e);
        }
    }


    // Génère uniquement le contenu de la présentation (sans l'image)
    private Mono<String> generatePresentationContent(BusinessPlanInputDTO dto, String apiKey) {
        String prompt = createPromptFromDTO(dto);
        return callGemini(prompt, apiKey)
            .map(this::extractContentFromGeminiResponse);
    }



    // Construit la liste complète des slides (image + contenu)
    private List<Slide> buildCompleteSlideList(String coverImageUrl, String presentationContent) {
        List<Slide> slides = new ArrayList<>();

        // Ajouter la slide de couverture si l'image existe
        if (coverImageUrl != null) {
            slides.add(new Slide(String.format("# Business Plan Cover\n\n![Cover Image](%s)", coverImageUrl)));
        }

        // Parser le contenu de la présentation
        String[] slideContents = presentationContent.split("===SLIDE===");
        for (String content : slideContents) {
            String trimmedContent = content.trim();
            if (!trimmedContent.isEmpty()) {
                slides.add(new Slide(trimmedContent));
            }
        }

        return slides;
    }

    // Extrait le texte de la réponse Gemini
    private String extractContentFromGeminiResponse(String geminiResponse) {
        try {
            JsonNode root = objectMapper.readTree(geminiResponse);
            JsonNode textNode = root.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text");

            if (textNode.isMissingNode()) {
                throw new RuntimeException("Le champ texte attendu est manquant dans la réponse de Gemini.");
            }

            return textNode.asText();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'analyse de la réponse Gemini", e);
        }
    }


}
