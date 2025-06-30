package com.readytoplanbe.myapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readytoplanbe.myapp.domain.Slide;
import com.readytoplanbe.myapp.service.dto.BusinessPlanInputDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
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
            promptTemplate = "You are an expert business consultant. Generate a detailed professional business plan presentation with these strict rules:\n\n" +
                "STRUCTURE RULES:\n" +
                "1. Use exactly '===SLIDE===' on a separate line to separate slides\n" +
                "2. First slide must be '[CompanyName] Business Plan Presentation' followed by '## Table of Contents' with ACTUAL section titles\n" +
                "3. Each main section starts with '[SectionNumber]. [SectionTitle]' on a new slide\n" +
                "4. Table of Contents must EXACTLY match these section titles\n" +
                "5. For slides containing tables, add '[TABLE]' immediately after the section title\n" +
                "6. DO NOT include any other technical markup labels\n" +
                "7. Content should begin immediately after the title without title repetition\n" +
                "8. Use bullet points (-) for lists, **bold** for emphasis\n" +

                "9. STRICT TABLE FORMATTING RULES:\n" +
                "   - First row must be header row with bold text\n" +
                "   - Second row must be separator row with '----' between cells\n" +
                "   - Use **bold** for important values\n" +
                "   - Tables must have clear horizontal and vertical lines\n" +
                "   - Never merge cells or use complex formatting\n\n" +

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
                "1. Executive Summary\n" +
                "2. Company Overview\n" +
                "3. Vision & Mission Statements\n" +
                "4. Business Description\n" +
                "5. Market Analysis\n" +
                "6. Competitive Landscape\n" +
                "7. Organization & Management\n" +
                "8. Service/Product Line\n" +

                "9. Product Impact Analysis - Must include:\n" +
                "| **Main Stakeholder** | **Product Benefits** |\n" +
                "|----------------------|----------------------|\n" +
                "| Customers            | [specific benefits]  |\n" +
                "| Employees            | [specific benefits]  |\n" +
                "| Suppliers            | [specific benefits]  |\n" +
                "| Investors            | [specific benefits]  |\n" +
                "| Local Communities    | [specific benefits]  |\n" +
                "| Regulatory Bodies    | [specific benefits]  |\n\n" +

                "10. Key Performance Components\n" +
                "11. Marketing & Sales Strategy\n" +

                "12. Organizational & Marketing Tasks - Must include TWO TABLES:\n\n" +
                "TABLE 1: General Planning & Organization\n" +
                "| **Checklist Item**       | **Status** | **Priority** | **Area** | **Stage** |\n" +
                "|---------------------------|------------|--------------|----------|-----------|\n" +
                "| [specific task]          | [status]   | [priority]   | [area]   | [stage]   |\n\n" +

                "TABLE 2: Marketing Tasks\n" +
                "| **Checklist Item**       | **Status** | **Priority** | **Area** | **Stage** |\n" +
                "|---------------------------|------------|--------------|----------|-----------|\n" +
                "| [specific task]          | [status]   | [priority]   | [area]   | [stage]   |\n\n" +

                "13. Project Phases & Impacts [TABLE] - Must include these exact phases:\n" +
                "| **Phase**                           | **Description**                                      | **Timeline** |\n" +
                "|-------------------------------------|------------------------------------------------------|--------------|\n" +
                "| 1. Fundamental Establishment       | [specific to company]                                | [timeline]   |\n" +
                "| 2. Product Improvement & Market Expansion | [specific to company]                         | [timeline]   |\n" +
                "| 3. New Revenue Streams             | [specific to company]                                | [timeline]   |\n" +
                "| 4. Strategic Innovation            | [specific to company]                                | [timeline]   |\n\n" +

                "14. Market Timeline Overview\n" +
                "15. Funding Requirements\n" +

                "16. Financial Projections - Must include:\n" +
                "| **Year** | **Revenue** | **Expenses** | **Profit** | **Growth Rate** | **Investment Needed** |\n" +
                "|----------|-------------|--------------|------------|-----------------|-----------------------|\n" +
                "| 2024     | [value]     | [value]      | [value]    | [value]         | [value]               |\n" +
                "| 2025     | [value]     | [value]      | [value]    | [value]         | [value]               |\n\n" +

                "17. Financial Dashboard - Must include:\n" +
                "| **Metric**               | **Current** | **Target (1Y)** | **Target (3Y)** |\n" +
                "|---------------------------|-------------|------------------|------------------|\n" +
                "| Monthly Recurring Revenue | [value]     | [value]          | [value]          |\n" +
                "| Customer Acquisition Cost | [value]     | [value]          | [value]          |\n" +
                "| EBITDA Margin            | [value]     | [value]          | [value]          |\n\n" +

                "18. Simplified Financing Plan\n" +
                "19. Risk Analysis\n" +
                "20. Appendix\n\n" +
                "===SLIDE===\n\n" +
                "Generate ONLY professional content:\n" +
                "1. Include specific details from the company information provided\n" +
                "2. Create tables and charts where appropriate (format as markdown tables)\n" +
                "3. Each section should have 3-5 key points with detailed explanations\n" +
                "4. All tables must maintain their exact column structure as specified\n" +
                "5. Content within tables must be adapted to the specific business (%s)\n" +
                "6. Financial projections must be based on the anticipated project size (%s %s)\n" +
                "7. All phase descriptions must be specific to the company's business model\n" +
                "8. Stakeholder benefits must be realistic and tailored to the product/service";
        } else {
            promptTemplate = "Vous êtes un expert en création d'entreprise. Générez un contenu professionnel détaillé de plan d'affaires avec ces règles strictes :\n\n" +
                "RÈGLES DE STRUCTURE :\n" +
                "1. Utilisez exactement '===SLIDE===' entre les sections\n" +
                "2. Première slide : 'Présentation du Plan d'Affaires [NomEntreprise]' avec '## Table des Matières'\n" +
                "3. Chaque section commence par '[NuméroSection]. [TitreSection]' sur nouvelle slide\n" +
                "4. La Table des Matières doit CORRESPONDRE exactement aux titres\n" +
                "5. Pour les slides contenant des tableaux, ajoutez '[TABLE]' immédiatement après le titre\n" +
                "6. NE PAS inclure d'autres balises techniques\n" +
                "7. Contenu doit commencer immédiatement après le titre\n" +
                "8. Utilisez des puces (-) pour les listes et des tableaux quand approprié\n" +

                "9. RÈGLES STRICTES DE FORMATAGE DES TABLEAUX :\n" +
                "   - La première ligne doit être l'en-tête en gras\n" +
                "   - La deuxième ligne doit être une séparation avec '----'\n" +
                "   - Utilisez **gras** pour les valeurs importantes\n" +
                "   - Les tableaux doivent avoir des lignes claires\n" +
                "   - Ne fusionnez pas les cellules\n\n" +

                "10. Générer au moins 20 slides avec contenu détaillé\n\n" +
                "INFORMATIONS ENTREPRISE :\n" +
                "➤ Nom Entreprise : %s\n" +
                "➤ Description : %s\n" +
                "➤ Date Création : %s\n" +
                "➤ Pays : %s\n" +
                "➤ Langues : %s\n" +
                "➤ Taille Projet : %s %s\n\n" +
                "EXIGENCES SPÉCIFIQUES DES TABLEAUX :\n";

            sectionsInstruction = "# Présentation du Plan d'Affaires %s\n\n" +
                "## Table des Matières\n\n" +
                "1. Résumé Exécutif\n" +
                "2. Aperçu de l'Entreprise\n" +
                "3. Vision & Mission\n" +
                "4. Description de l'Activité\n" +
                "5. Analyse de Marché\n" +
                "6. Environnement Concurrentiel\n" +
                "7. Organisation & Management\n" +
                "8. Ligne de Produits/Services\n" +

                "9. Analyse d'Impact des Produits - Doit inclure :\n" +
                "| **Partie Prenante Principale** | **Avantages du Produit** |\n" +
                "|--------------------------------|--------------------------|\n" +
                "| Clients                        | [avantages spécifiques]  |\n" +
                "| Employés                       | [avantages spécifiques]  |\n" +
                "| Fournisseurs                   | [avantages spécifiques]  |\n" +
                "| Investisseurs                  | [avantages spécifiques]  |\n" +
                "| Communautés Locales             | [avantages spécifiques]  |\n" +
                "| Organismes de Réglementation    | [avantages spécifiques]  |\n\n" +

                "10. Composants Clés de Performance\n" +
                "11. Stratégie Marketing & Ventes\n" +

                "12. Tâches Organisationnelles & Marketing - Doit inclure DEUX TABLEAUX :\n\n" +
                "TABLEAU 1 : Planification & Organisation Générale\n" +
                "| **Élément de Checklist**       | **Statut** | **Priorité** | **Domaine** | **Étape** |\n" +
                "|---------------------------------|------------|--------------|-------------|-----------|\n" +
                "| [tâche spécifique]             | [statut]   | [priorité]   | [domaine]   | [étape]   |\n\n" +

                "TABLEAU 2 : Tâches Marketing\n" +
                "| **Élément de Checklist**       | **Statut** | **Priorité** | **Domaine** | **Étape** |\n" +
                "|---------------------------------|------------|--------------|-------------|-----------|\n" +
                "| [tâche spécifique]             | [statut]   | [priorité]   | [domaine]   | [étape]   |\n\n" +

                "13. Phases du Projet & Impacts - Doit inclure ces phases exactes :\n" +
                "| **Phase**                           | **Description**                                      | **Calendrier** |\n" +
                "|-------------------------------------|------------------------------------------------------|----------------|\n" +
                "| 1. Établissement Fondamental       | [spécifique à l'entreprise]                          | [calendrier]   |\n" +
                "| 2. Amélioration Produits & Expansion Marché | [spécifique à l'entreprise]               | [calendrier]   |\n" +
                "| 3. Nouvelles Sources de Revenus    | [spécifique à l'entreprise]                          | [calendrier]   |\n" +
                "| 4. Innovation Stratégique          | [spécifique à l'entreprise]                          | [calendrier]   |\n\n" +

                "14. Calendrier du Marché\n" +
                "15. Besoins de Financement\n" +

                "16. Projections Financières - Doit inclure :\n" +
                "| **Année** | **Revenus** | **Dépenses** | **Profit** | **Taux Croissance** | **Investissement Requis** |\n" +
                "|-----------|-------------|--------------|------------|---------------------|---------------------------|\n" +
                "| 2024      | [valeur]    | [valeur]     | [valeur]   | [valeur]            | [valeur]                  |\n" +
                "| 2025      | [valeur]    | [valeur]     | [valeur]   | [valeur]            | [valeur]                  |\n\n" +

                "17. Tableau de Bord Financier - Doit inclure :\n" +
                "| **Métrique**               | **Actuel** | **Objectif (1A)** | **Objectif (3A)** |\n" +
                "|----------------------------|------------|-------------------|-------------------|\n" +
                "| Revenus Mensuels Récurrents| [valeur]   | [valeur]          | [valeur]          |\n" +
                "| Coût d'Acquisition Client  | [valeur]   | [valeur]          | [valeur]          |\n" +
                "| Marge EBITDA              | [valeur]   | [valeur]          | [valeur]          |\n\n" +

                "18. Plan de Financement Simplifié\n" +
                "19. Analyse des Risques\n" +
                "20. Annexes\n\n" +
                "===SLIDE===\n\n" +
                "Contenu professionnel uniquement :\n" +
                "1. Inclure des détails spécifiques de l'entreprise\n" +
                "2. Créer des tableaux et graphiques quand approprié\n" +
                "3. Chaque section doit avoir 3-5 points clés avec explications\n" +
                "4. Tous les tableaux doivent conserver leur structure de colonnes exacte\n" +
                "5. Le contenu des tableaux doit être adapté à l'entreprise spécifique (%s)\n" +
                "6. Les projections financières doivent être basées sur la taille du projet (%s %s)\n" +
                "7. Les descriptions de phases doivent être spécifiques au modèle d'entreprise\n" +
                "8. Les avantages pour les parties prenantes doivent être réalistes et adaptés";
        }

        return String.format(promptTemplate,
            dto.getCompanyName(),
            dto.getCompanyDescription(),
            dto.getCompanyStartDate(),
            dto.getCountry(),
            dto.getLanguages(),
            dto.getAnticipatedProjectSize(),
            dto.getCurrency()
        ) + String.format(sectionsInstruction,
            dto.getCompanyName(),
            dto.getAnticipatedProjectSize(),
            dto.getCurrency(),
            dto.getCompanyName()
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
            .onStatus(
                HttpStatus.TOO_MANY_REQUESTS::equals,
                response -> {
                    log.warn("🛑 Requête refusée : Erreur 429 - Trop de requêtes. Tentative de réessai...");
                    return Mono.error(new RuntimeException("429 Too Many Requests"));
                }
            )
            .bodyToMono(String.class)
            .retryWhen(
                Retry.backoff(5, Duration.ofSeconds(1))
                    .jitter(0.5)
                    .filter(ex -> ex.getMessage().contains("429"))
                    .onRetryExhaustedThrow((retrySpec, signal) -> {
                        log.error("❌ Échec après {} tentatives. L'erreur 429 persiste.", signal.totalRetries());
                        return new RuntimeException("Échec après plusieurs tentatives : Erreur 429 persistante", signal.failure());
                    })
            );
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
