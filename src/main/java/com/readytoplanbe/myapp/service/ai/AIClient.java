package com.readytoplanbe.myapp.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AIClient {

    // IMPORTANT: Make this a property in application.yml for security
    private static final String GEMINI_API_KEY = "AIzaSyBcw3NMOd2EXFdYvAgImXSr1ufe82bOzGM";
    private static final String API_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-002:generateContent?key=" + GEMINI_API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Génère un texte (HTML) via l'API Gemini.
     * @param prompt Le prompt pour l'IA.
     * @return Le contenu généré en tant que chaîne de caractères.
     * @throws Exception en cas d'erreur de communication avec l'API.
     */
    public String generatePresentation(String prompt) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Échappement correct du prompt
        String escapedPrompt = prompt.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");

        String body = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + escapedPrompt + "\" }] }] }";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode candidate = root.at("/candidates/0/content/parts/0/text");

                if (!candidate.isMissingNode()) {
                    String rawResponse = candidate.asText();

                    // Nettoyer la réponse des backticks et markdown
                    String cleanedResponse = rawResponse
                        .replaceAll("(?s)```html", "")
                        .replaceAll("(?s)```", "")
                        .replaceAll("(?s)HTML:", "")
                        .trim();

                    return cleanedResponse;
                } else {
                    StringBuilder sb = new StringBuilder();
                    JsonNode contents = root.at("/candidates/0/content/parts");
                    if (contents.isArray()) {
                        for (JsonNode part : contents) {
                            sb.append(part.path("text").asText()).append("\n");
                        }
                    }
                    return sb.toString().trim();
                }
            } else {
                throw new RuntimeException("Erreur API Gemini : " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Erreur HTTP Gemini : " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        }
    }

    /**
     * Génère des données de graphique au format JSON via l'API Gemini.
     * @param prompt Le prompt décrivant le graphique souhaité.
     * @return Une chaîne de caractères contenant les données JSON du graphique.
     * @throws Exception en cas d'erreur de communication ou de parsing.
     */
    public String generateChartData(String prompt) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String newPrompt = "Génère des données JSON pour un graphique basé sur la description suivante. " +
            "Formats acceptés :\n" +
            "- Barres: {\"type\": \"bar\", \"title\": \"Titre\", \"categories\": [\"Cat1\", \"Cat2\"], \"values\": [10, 20]}\n" +
            "- Circulaire: {\"type\": \"pie\", \"title\": \"Titre\", \"labels\": [\"Label1\", \"Label2\"], \"values\": [30, 70]}\n" +
            "- Chronologie: {\"type\": \"timeline\", \"title\": \"Titre\", \"data\": [{\"year\": 1990, \"event\": \"Description\"}]}\n" +
            "- Diagramme: {\"type\": \"diagram\", \"title\": \"Titre\", \"elements\": [{\"name\": \"Élément1\"}, {\"name\": \"Élément2\"}]} OU {\"type\": \"diagram\", \"title\": \"Titre\", \"content\": \"Description\"}\n" +
            "⚠️ IMPORTANT : Retourne UNIQUEMENT le JSON brut, sans ```json, sans backticks, sans explications.\n" +
            "Description: " + prompt;

        String escapedPrompt = newPrompt.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
        String body = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + escapedPrompt + "\" }] }] }";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                String raw = root.at("/candidates/0/content/parts/0/text").asText();

                // Nettoyer la réponse
                String cleaned = raw.replaceAll("(?s)```json", "")
                    .replaceAll("(?s)```", "")
                    .replaceAll("(?s)JSON:", "")
                    .replaceAll("(?s)json:", "")
                    .trim();

                // Valider que c'est du JSON valide
                try {
                    objectMapper.readTree(cleaned);
                    return cleaned;
                } catch (Exception e) {
                    // Si ce n'est pas du JSON valide, créer un format par défaut
                    return createDefaultChartData(prompt);
                }
            } else {
                return createDefaultChartData(prompt);
            }
        } catch (HttpClientErrorException e) {
            return createDefaultChartData(prompt);
        }
    }

    private String createDefaultChartData(String prompt) {
        // Créer des données par défaut en cas d'erreur
        return "{" +
            "\"type\": \"bar\"," +
            "\"title\": \"Graphique par défaut\"," +
            "\"categories\": [\"Catégorie 1\", \"Catégorie 2\", \"Catégorie 3\"]," +
            "\"values\": [25, 40, 35]" +
            "}";
    }
}
