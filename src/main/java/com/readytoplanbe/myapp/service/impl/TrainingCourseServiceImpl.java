package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.TrainingCourse;
import com.readytoplanbe.myapp.domain.User;
import com.readytoplanbe.myapp.domain.enumeration.Languages;
import com.readytoplanbe.myapp.repository.TrainingCourseRepository;
import com.readytoplanbe.myapp.repository.UserRepository;
import com.readytoplanbe.myapp.security.SecurityUtils;
import com.readytoplanbe.myapp.service.TrainingCourseService;
import com.readytoplanbe.myapp.service.ai.AIClient;
import com.readytoplanbe.myapp.service.chart.ChartService;
import com.readytoplanbe.myapp.service.dto.TrainingCourseDTO;
import com.readytoplanbe.myapp.service.mapper.TrainingCourseMapper;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link TrainingCourse}.
 */
@Service
public class TrainingCourseServiceImpl implements TrainingCourseService {

    private final Logger log = LoggerFactory.getLogger(TrainingCourseServiceImpl.class);

    private final TrainingCourseRepository trainingCourseRepository;

    private final TrainingCourseMapper trainingCourseMapper;

    private final AIClient aiClient;

    private final ChartService chartService;

    private final UserRepository userRepository;


    public TrainingCourseServiceImpl(
        TrainingCourseRepository trainingCourseRepository,
        TrainingCourseMapper trainingCourseMapper,
        AIClient aiClient,
        ChartService chartService,
        UserRepository userRepository) {
        this.trainingCourseRepository = trainingCourseRepository;
        this.trainingCourseMapper = trainingCourseMapper;
        this.aiClient = aiClient;
        this.chartService = chartService;
        this.userRepository = userRepository;
    }

    @Override
    public TrainingCourseDTO save(TrainingCourseDTO trainingCourseDTO) {
        log.debug("Request to save TrainingCourse : {}", trainingCourseDTO);

        Optional<String> currentUserLoginOpt = SecurityUtils.getCurrentUserLogin();
        log.debug("Current user login from SecurityUtils: {}", currentUserLoginOpt.orElse("NOT_FOUND"));

        if (trainingCourseDTO.getId() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new IllegalStateException("User must be authenticated to create a TrainingCourse");
            }

            String currentUserLogin = authentication.getName(); // login réel
            User currentUser = userRepository.findOneByLogin(currentUserLogin)
                .orElseThrow(() -> new IllegalStateException("User not found: " + currentUserLogin));

            trainingCourseDTO.setCreatedBy(currentUser.getFirstName() + " " + currentUser.getLastName());
            trainingCourseDTO.setCreatedByLogin(currentUser.getLogin());
            trainingCourseDTO.setCreatedDate(Instant.now());
        }

        TrainingCourse trainingCourse = trainingCourseMapper.toEntity(trainingCourseDTO);
        trainingCourse = trainingCourseRepository.save(trainingCourse);

        // Génération de la présentation via AI
        try {
            String presentationHtml = generatePresentation(trainingCourse.getId());
            trainingCourse.setPresentation(presentationHtml);

            // Re-sauvegarde avec la présentation générée
            trainingCourse = trainingCourseRepository.save(trainingCourse);
            log.debug("Presentation generated and saved for course id: {}", trainingCourse.getId());
        } catch (Exception e) {
            log.error("Erreur lors de la génération de la présentation AI", e);
        }

        // Retourner le DTO
        return trainingCourseMapper.toDto(trainingCourse);
    }


    @Override
    public TrainingCourseDTO update(TrainingCourseDTO trainingCourseDTO) {
        log.debug("Request to update TrainingCourse : {}", trainingCourseDTO);
        TrainingCourse trainingCourse = trainingCourseMapper.toEntity(trainingCourseDTO);
        trainingCourse = trainingCourseRepository.save(trainingCourse);
        return trainingCourseMapper.toDto(trainingCourse);
    }

    @Override
    public Optional<TrainingCourseDTO> partialUpdate(TrainingCourseDTO trainingCourseDTO) {
        log.debug("Request to partially update TrainingCourse : {}", trainingCourseDTO);

        return trainingCourseRepository
            .findById(trainingCourseDTO.getId())
            .map(existingTrainingCourse -> {
                trainingCourseMapper.partialUpdate(existingTrainingCourse, trainingCourseDTO);

                return existingTrainingCourse;
            })
            .map(trainingCourseRepository::save)
            .map(trainingCourseMapper::toDto);
    }

    @Override
    public List<TrainingCourseDTO> findAll() {
        log.debug("Request to get all TrainingCourses");
        return trainingCourseRepository
            .findAll()
            .stream()
            .map(trainingCourseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<TrainingCourseDTO> findOne(String id) {
        log.debug("Request to get TrainingCourse : {}", id);
        return trainingCourseRepository.findById(id).map(trainingCourseMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete TrainingCourse : {}", id);
        trainingCourseRepository.deleteById(id);
    }

    /**
     * Génère la présentation HTML via AI, puis remplace les placeholders de graphiques
     * par des images base64.
     */
    public String generatePresentation(String trainingCourseId) {
        TrainingCourse trainingCourse = trainingCourseRepository.findById(trainingCourseId)
            .orElseThrow(() -> new RuntimeException("TrainingCourse introuvable"));

        String prompt = buildPrompt(trainingCourse);

        try {
            String rawHtml = aiClient.generatePresentation(prompt);

            String structuredHtml = structurePresentation(rawHtml);

            Pattern pattern = Pattern.compile("\\{\\{GRAPH:(.*?)\\}\\}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(rawHtml);
            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {
                if (matcher.groupCount() >= 1) {
                    String chartPrompt = matcher.group(1).trim();
                    try {
                        String chartJsonData = aiClient.generateChartData(chartPrompt);

                        // Valider les données avant de créer le graphique
                        if (chartService.isValidChartData(chartJsonData)) {
                            byte[] chartImageBytes = chartService.createChart(chartJsonData);
                            String base64 = Base64.getEncoder().encodeToString(chartImageBytes);
                            String imgTag = "<div class='chart'><img src=\"data:image/png;base64," + base64 + "\" style=\"max-width:100%;border-radius:8px;\"/></div>";
                            matcher.appendReplacement(sb, Matcher.quoteReplacement(imgTag));
                        } else {
                            throw new IllegalArgumentException("Données de graphique invalides");
                        }
                    } catch (Exception imgEx) {
                        log.error("Erreur génération image pour prompt: {}", chartPrompt, imgEx);
                        String fallback = "<div class='chart-placeholder'>[Graphique: " +
                            chartPrompt.substring(0, Math.min(chartPrompt.length(), 50)) + "...]</div>";
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(fallback));
                    }
                }
            }
            matcher.appendTail(sb);
            String finalHtml = injectGlobalStylesIfMissing(sb.toString());

            return finalHtml;
        } catch (Exception e) {
            log.error("Erreur lors de l'appel AI pour la présentation", e);
            return "<div><p>Erreur lors de la génération de la présentation.</p></div>";
        }
    }

    private String structurePresentation(String html) {
        String regex = "(?i)<section class='slide'>(.*?)(\\{\\{GRAPH:.*?\\}\\})(.*?)</section>";

        return html.replaceAll(regex,
            "<section class='slide intro-slide'>$1</section>" +
                "<section class='slide visual-slide'><h3>Titre du Graphique</h3>$2</section>");
    }

    private String injectGlobalStylesIfMissing(String html) {
        if (html.contains("<style") || html.contains("class='slide'") || html.contains("class=\"slide\"")) {
            if (!html.contains("<style id=\"global-presentation-style\">")) {
                String globalStyle = "<style id=\"global-presentation-style\">"
                    + "body{font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#FFFFFF; color:#333;}"
                    + ".slide{padding:40px;box-sizing:border-box;min-height:400px;} .cover{display:flex;align-items:center;justify-content:center;}"
                    + ".chart{margin-top:20px;text-align:center;} .chart img{max-width:100%;height:auto;border-radius:8px;}"

                    // STYLES RENFORCÉS POUR LES TABLEAUX
                    + ".table-container {overflow-x: auto; margin: 25px 0; border-radius: 10px; box-shadow: 0 0 15px rgba(0, 0, 0, 0.08) !important;}"
                    + ".styled-table {border-collapse: collapse !important; width: 100% !important; font-size: 0.95em !important; min-width: 600px; margin: 1.5rem 0 !important; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1) !important; border-radius: 8px !important; overflow: hidden !important;}"
                    + ".styled-table thead tr {background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%) !important; color: #ffffff !important; text-align: left !important;}"
                    + ".styled-table th, .styled-table td {padding: 12px 15px !important; border: 1px solid #ddd !important; text-align: left !important;}"
                    + ".styled-table th {background: #3498DB !important; color: white !important; font-weight: 600 !important; font-size: 1.05em !important;}"
                    + ".styled-table td {background: #ECF0F1 !important; color: #333 !important;}"
                    + ".styled-table tbody tr {border-bottom: 1px solid #ddd !important;}"
                    + ".styled-table tbody tr:last-child td {border-bottom: none !important;}"
                    + ".styled-table tbody tr.highlight td {background: #e3f2fd !important; font-weight: 600 !important; color: #1976d2 !important;}"
                    + ".styled-table tbody tr:hover td {background: #f1f8ff !important;}"

                    + ".timeline {margin: 20px 0; position: relative;}"
                    + ".timeline:before {content: ''; position: absolute; left: 20px; top: 0; bottom: 0; width: 4px; background: #3498DB;}"
                    + ".timeline-item {position: relative; margin-bottom: 20px; padding-left: 40px;}"
                    + ".timeline-item:before {content: ''; position: absolute; left: 16px; top: 8px; width: 12px; height: 12px; border-radius: 50%; background: #3498DB; border: 3px solid #fff; box-shadow: 0 0 0 2px #3498DB;}"
                    + ".timeline-year {font-weight: bold; color: #3498DB; margin-bottom: 5px;}"
                    + ".timeline-content {background: #f8f9fa; padding: 15px; border-radius: 8px; border-left: 3px solid #3498DB;}"
                    + "</style>";
                if (html.contains("<head>")) {
                    html = html.replaceFirst("<head>", "<head>" + globalStyle);
                } else {
                    html = globalStyle + html;
                }
            }
        } else {
            String wrapper = "<html><head><meta charset='utf-8'>"
                + "<style id=\"global-presentation-style\">"
                + "body{font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#FFFFFF; color:#333;} "
                + ".slide{padding:40px;min-height:400px;} "
                + ".chart{margin-top:20px;text-align:center;} "
                + ".chart img{max-width:100%;border-radius:8px;} "

                // Styles améliorés pour les tableaux
                + ".table-container {overflow-x: auto; margin: 25px 0; border-radius: 10px; box-shadow: 0 0 15px rgba(0, 0, 0, 0.08);}"
                + ".styled-table {border-collapse: collapse; width: 100%; font-size: 0.95em; min-width: 600px;}"
                + ".styled-table thead tr {background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%); color: #ffffff; text-align: left;}"
                + ".styled-table th, .styled-table td {padding: 14px 16px; border-right: 1px solid #e1e5eb;}"
                + ".styled-table th:last-child, .styled-table td:last-child {border-right: none;}"
                + ".styled-table th {font-weight: 700; font-size: 1.05em;}"
                + ".styled-table tbody tr {border-bottom: 1px solid #e1e5eb; transition: background-color 0.2s;}"
                + ".styled-table tbody tr:nth-of-type(even) {background-color: #f8f9fa;}"
                + ".styled-table tbody tr:nth-of-type(odd) {background-color: #ffffff;}"
                + ".styled-table tbody tr:last-of-type {border-bottom: 3px solid #4b6cb7;}"
                + ".styled-table tbody tr.highlight {background-color: #e3f2fd !important; font-weight: 600; color: #1976d2;}"
                + ".styled-table tbody tr:hover {background-color: #f1f8ff; cursor: default;}"

                + ".timeline {margin: 20px 0; position: relative;}"
                + ".timeline:before {content: ''; position: absolute; left: 20px; top: 0; bottom: 0; width: 4px; background: #3498DB;}"
                + ".timeline-item {position: relative; margin-bottom: 20px; padding-left: 40px;}"
                + ".timeline-item:before {content: ''; position: absolute; left: 16px; top: 8px; width: 12px; height: 12px; border-radius: 50%; background: #3498DB; border: 3px solid #fff; box-shadow: 0 0 0 2px #3498DB;}"
                + ".timeline-year {font-weight: bold; color: #3498DB; margin-bottom: 5px;}"
                + ".timeline-content {background: #f8f9fa; padding: 15px; border-radius: 8px; border-left: 3px solid #3498DB;}"
                + "</style>"
                + "</head><body>" + html + "</body></html>";
            html = wrapper;
        }
        return html;
    }

    private String buildPrompt(TrainingCourse trainingCourse) {
        String language = trainingCourse.getLanguages() == Languages.FRENCH ? "français" : "anglais";

        return "Tu es un expert en pédagogie et en design de présentations modernes. " +
            "Ta tâche est de générer une présentation complète et professionnelle du cours suivant en " + language + ". " +
            "⚠️ CONTRAINTES STRICTES :" +
            " - MINIMUM 18 SLIDES avec AU MOINS 4 TABLEAUX DIFFÉRENTS" +
            " - Structure : Page de garde, Plan, Introduction, Historique, 5+ slides de contenu, Avantages/Inconvénients, Applications, Résumé" +
            " - Chaque slide dans <section class='slide'>...</section>" +
            " - Page de garde avec titre, public, niveau, classe, durée" +
            " - Le niveau du cours est " + trainingCourse.getLevel() + ", adapte la profondeur du contenu en conséquence." +

            " - NOUVELLE STRUCTURE OBLIGATOIRE :" +
            "   * AVANT CHAQUE GRAPHIQUE, HISTOGRAMME OU TABLEAU :" +
            "     - Créer un slide d'introduction avec un titre clair" +
            "     - Ajouter un paragraphe ou des points expliquant ce qui sera présenté" +
            "     - Ce slide doit préparer le contenu visuel suivant" +
            "   * LE SLIDE SUIVANT contient uniquement :" +
            "     - Un titre descriptif" +
            "     - Le graphique/histogramme/tableau (centré et bien mis en valeur)" +
            "     - Aucun autre texte pour éviter la surcharge" +

            " - POUR LES TABLEAUX : UTILISER OBLIGATOIREMENT CE FORMAT EXACT :" +
            "   <div class='table-container'>" +
            "     <table class='styled-table'>" +
            "       <thead><tr><th>Colonne 1</th><th>Colonne 2</th><th>Colonne 3</th></tr></thead>" +
            "       <tbody>" +
            "         <tr><td>Donnée 1</td><td>Donnée 2</td><td>Donnée 3</td></tr>" +
            "         <tr class='highlight'><td>IMPORTANT</td><td>VALEUR</td><td>SPÉCIAL</td></tr>" +
            "         <tr><td>Donnée 4</td><td>Donnée 5</td><td>Donnée 6</td></tr>" +
            "       </tbody>" +
            "     </table>" +
            "   </div>" +
            " - Types de tableaux OBLIGATOIRES :" +
            "   1. Tableau comparatif (avantages/inconvénients)" +
            "   2. Tableau de spécifications techniques" +
            "   3. Tableau chronologique" +
            "   4. Tableau de synthèse" +

            " - Chaque slide de contenu doit comporter au moins deux paragraphes explicatifs." +
            " - Intègre dans les slides avec des points explicatifs: des graphiques (camemberts, histogrammes, barres), et une chronologie adaptée." +
            " - Pour les graphiques, insère un placeholder explicite au format : {{GRAPH:type=bar,title=Répartition,...}} " +
            "   que je remplacerai ensuite par une image." +
            " - Pour graphiques : {{GRAPH:type=bar,title=Titre,categories=[...],values=[...]}}" +
            " - AUCUN code <script> ou markdown" +
            " - HTML propre et prêt à afficher" +
            " - NE PAS utiliser de backticks ``` autour du HTML" +
            " - Retourner DIRECTEMENT le HTML sans commentaires" +

            "\n\nEXEMPLE DE STRUCTURE CORRECTE :" +
            "<!-- Slide d'introduction avant un élément visuel -->" +
            "<section class='slide'>" +
            "  <h2>Titre contextuel</h2>" +
            "  <p>Description et explication de ce qui sera présenté dans le slide suivant...</p>" +
            "  <ul>" +
            "    <li>Point clé 1 à observer</li>" +
            "    <li>Point clé 2 à retenir</li>" +
            "    <li>Contexte nécessaire</li>" +
            "  </ul>" +
            "</section>" +

            "<!-- Slide avec uniquement l'élément visuel -->" +
            "<section class='slide'>" +
            "  <h3>Titre descriptif de l'élément visuel</h3>" +
            "  {{GRAPH:type=bar,title=Titre significatif,categories=[Cat1,Cat2,Cat3],values=[v1,v2,v3]}}" +
            "</section>" +

            "\n\nPage de garde EXEMPLAIRE :\n" +
            "<section class='slide cover'>\n" +
            "  <div class='cover-content'>\n" +
            "    <h1>" + safe(trainingCourse.getTitle()) + "</h1>\n" +
            "    <div class='meta'>\n" +
            "      <p><strong>Public:</strong> " + safe(trainingCourse.getTargetAudience()) + "</p>\n" +
            "      <p><strong>Niveau:</strong> " + safe(trainingCourse.getLevel()) + "</p>\n" +
            "      <p><strong>Classe:</strong> " + safe(trainingCourse.getStudyClass()) + "</p>\n" +
            "      <p><strong>Durée:</strong> " + (trainingCourse.getDuration() != null ? trainingCourse.getDuration() : "Non spécifiée") + "</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</section>\n\n" +

            "Conclusion du cours pour contexte : " + safe(trainingCourse.getSummary()) + "\n" +
            "Génère une présentation RICHE avec MULTIPLES TABLEAUX DÉTAILLÉS.";
    }

    private String safe(Object obj) {
        return obj == null ? "Non spécifié" : obj.toString();
    }

    public TrainingCourseDTO evaluatePresentation(String courseId, Integer satisfaction) {
        TrainingCourse course = trainingCourseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setSatisfaction(satisfaction);
        trainingCourseRepository.save(course);
        return trainingCourseMapper.toDto(course);
    }

    public TrainingCourseDTO setPublicPresentation(String courseId, Boolean isPublic) {
        TrainingCourse course = trainingCourseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setPublicPresentation(isPublic);
        trainingCourseRepository.save(course);
        return trainingCourseMapper.toDto(course);
    }

    public Map<String, Long> getSatisfactionStats() {
        List<TrainingCourse> courses = trainingCourseRepository.findAll();

        long satisfied = courses.stream()
            .filter(c -> c.getSatisfaction() != null && c.getSatisfaction() == 3)
            .count();

        long notSatisfied = courses.stream()
            .filter(c -> c.getSatisfaction() != null && c.getSatisfaction() == 1)
            .count();

        long total = courses.size();
        long notRated = total - (satisfied + notSatisfied);

        Map<String, Long> stats = new HashMap<>();
        stats.put("Satisfied", satisfied);
        stats.put("NotSatisfied", notSatisfied);
        stats.put("NotRated", notRated);
        stats.put("Total", total);

        return stats;
    }

    @Override
    public List<TrainingCourseDTO> findAllByCurrentUser() {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Utilisateur non connecté"));

        return trainingCourseRepository.findByCreatedByLogin(currentUserLogin)
            .stream()
            .map(trainingCourseMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<TrainingCourseDTO> findAllPublic() {
        return trainingCourseRepository.findByPublicPresentationTrue()
            .stream()
            .map(trainingCourseMapper::toDto)
            .collect(Collectors.toList());
    }

}
