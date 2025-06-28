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
                "2. First slide must be '# [CompanyName] Business Plan Presentation' followed by '## Table of Contents' with ACTUAL section titles\n" +
                "3. Each main section starts with '# [SectionNumber]. [SectionTitle]' on a new slide\n" +
                "4. Table of Contents must EXACTLY match these section titles\n" +
                "5. For slides containing tables, add '[TABLE]' immediately after the section title\n" +
                "6. DO NOT include any other technical markup labels\n" +
                "7. Content should begin immediately after the title without title repetition\n" +
                "8. Use bullet points (-) for lists, **bold** for emphasis\n" +
                "9. ALWAYS include tables for these specific sections (mark with [TABLE]):\n" +
                "   - Organizational & Marketing Tasks [TABLE]\n" +
                "   - Financial Projections [TABLE]\n" +
                "   - Product Impact Analysis [TABLE]\n" +
                "   - Project Phases & Impacts [TABLE]\n" +
                "10. Tables must be formatted EXACTLY like this example:\n" +
                "```\n" +
                "| Header 1       | Header 2       | Header 3       |\n" +
                "|----------------|----------------|----------------|\n" +
                "| **Cell 1**     | Cell 2         | Cell 3         |\n" +
                "| Cell 4         | **Cell 5**     | Cell 6         |\n" +
                "```\n" +
                "   - First row must be header row with bold text\n" +
                "   - Second row must be separator row with '----' between cells\n" +
                "   - Use **bold** for important values\n" +
                "   - Tables must have clear horizontal and vertical lines\n" +
                "   - Never merge cells or use complex formatting\n" +
                "11. Include financial charts and tables for projections\n" +
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
                "9. Product Impact Analysis [TABLE]\n" +
                "10. Key Performance Components\n" +
                "11. Marketing & Sales Strategy\n" +
                "12. Organizational & Marketing Tasks [TABLE]\n" +
                "13. Project Phases & Impacts [TABLE]\n" +
                "14. Market Timeline Overview\n" +
                "15. Funding Requirements\n" +
                "16. Financial Projections [TABLE]\n" +
                "17. Financial Dashboard [TABLE]\n" +
                "18. Simplified Financing Plan\n" +
                "19. Risk Analysis\n" +
                "20. Appendix\n\n" +
                "===SLIDE===\n\n" +
                "Generate ONLY professional content:\n" +
                "1. Include specific details from the company information provided\n" +
                "2. Create tables and charts where appropriate (format as markdown tables)\n" +
                "3. Each section should have 3-5 key points with detailed explanations\n" +
                "4. Financial sections must include numeric projections based on the anticipated project size (%s %s)\n" +
                "5. Product Impact Analysis must include a stakeholder table\n" +
                "6. Financial Dashboard should summarize key financial metrics\n" +
                "7. Project Phases should include timeline visualization\n" +
                "8. Make all content specific to %s";
        } else {
            promptTemplate = "Vous êtes un expert en création d'entreprise. Générez un contenu professionnel détaillé de plan d'affaires avec ces règles strictes :\n\n" +
                "RÈGLES DE STRUCTURE :\n" +
                "1. Utilisez exactement '===SLIDE===' entre les sections\n" +
                "2. Première slide : '# Présentation du Plan d'Affaires [NomEntreprise]' avec '## Table des Matières'\n" +
                "3. Chaque section commence par '# [NuméroSection]. [TitreSection]' sur nouvelle slide\n" +
                "4. La Table des Matières doit CORRESPONDRE exactement aux titres\n" +
                "5. Pour les slides contenant des tableaux, ajoutez '[TABLE]' immédiatement après le titre\n" +
                "6. NE PAS inclure d'autres balises techniques\n" +
                "7. Contenu doit commencer immédiatement après le titre\n" +
                "8. Utilisez des puces (-) pour les listes et des tableaux quand approprié\n" +
                "9. TOUJOURS inclure des tableaux pour ces sections spécifiques (marquées avec [TABLE]):\n" +
                "   - Tâches Organisationnelles & Marketing [TABLE]\n" +
                "   - Projections Financières [TABLE]\n" +
                "   - Analyse d'Impact des Produits [TABLE]\n" +
                "   - Phases du Projet & Impacts [TABLE]\n" +
                "10. Les tableaux doivent être formatés EXACTEMENT comme cet exemple :\n" +
                "```\n" +
                "| En-tête 1      | En-tête 2      | En-tête 3      |\n" +
                "|----------------|----------------|----------------|\n" +
                "| **Cellule 1**  | Cellule 2      | Cellule 3      |\n" +
                "| Cellule 4      | **Cellule 5**  | Cellule 6      |\n" +
                "```\n" +
                "   - La première ligne doit être l'en-tête en gras\n" +
                "   - La deuxième ligne doit être une séparation avec '----'\n" +
                "   - Utilisez **gras** pour les valeurs importantes\n" +
                "   - Les tableaux doivent avoir des lignes claires\n" +
                "   - Ne fusionnez pas les cellules\n" +
                "11. Générer au moins 20 slides avec contenu détaillé\n\n" +
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
                "1. Résumé Exécutif\n" +
                "2. Aperçu de l'Entreprise\n" +
                "3. Vision & Mission\n" +
                "4. Description de l'Activité\n" +
                "5. Analyse de Marché\n" +
                "6. Environnement Concurrentiel\n" +
                "7. Organisation & Management\n" +
                "8. Ligne de Produits/Services\n" +
                "9. Analyse d'Impact des Produits [TABLE]\n" +
                "10. Composants Clés de Performance\n" +
                "11. Stratégie Marketing & Ventes\n" +
                "12. Tâches Organisationnelles & Marketing [TABLE]\n" +
                "13. Phases du Projet & Impacts [TABLE]\n" +
                "14. Calendrier du Marché\n" +
                "15. Besoins de Financement\n" +
                "16. Projections Financières [TABLE]\n" +
                "17. Tableau de Bord Financier [TABLE]\n" +
                "18. Plan de Financement Simplifié\n" +
                "19. Analyse des Risques\n" +
                "20. Annexes\n\n" +
                "===SLIDE===\n\n" +
                "Contenu professionnel uniquement :\n" +
                "1. Inclure des détails spécifiques de l'entreprise\n" +
                "2. Créer des tableaux et graphiques quand approprié\n" +
                "3. Chaque section doit avoir 3-5 points clés avec explications\n" +
                "4. Sections financières doivent inclure projections basées sur la taille du projet (%s %s)\n" +
                "5. Analyse d'Impact doit inclure tableau des parties prenantes\n" +
                "6. Tableau de Bord doit résumer les indicateurs financiers clés\n" +
                "7. Phases du Projet doivent inclure visualisation temporelle\n" +
                "8. Rendre tout le contenu spécifique à %s";
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
