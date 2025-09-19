package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.CourseEvaluation;
import com.readytoplanbe.myapp.domain.TrainingCourse;
import com.readytoplanbe.myapp.domain.User;
import com.readytoplanbe.myapp.domain.enumeration.Languages;
import com.readytoplanbe.myapp.repository.CourseEvaluationRepository;
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

    private final CourseEvaluationRepository courseEvaluationRepository;


    public TrainingCourseServiceImpl(
        TrainingCourseRepository trainingCourseRepository,
        TrainingCourseMapper trainingCourseMapper,
        AIClient aiClient,
        ChartService chartService,
        UserRepository userRepository,
        CourseEvaluationRepository courseEvaluationRepository) {
        this.trainingCourseRepository = trainingCourseRepository;
        this.trainingCourseMapper = trainingCourseMapper;
        this.aiClient = aiClient;
        this.chartService = chartService;
        this.userRepository = userRepository;
        this.courseEvaluationRepository = courseEvaluationRepository;
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

        // Adapter le contenu selon le niveau
        String levelAdaptation = "";
        switch (trainingCourse.getLevel()) {
            case BEGINNER:
                levelAdaptation = "Le contenu doit être simple, progressif et très pédagogique avec des explications détaillées. Utiliser un langage accessible et des exemples concrets.";
                break;
            case INTERMEDIATE:
                levelAdaptation = "Le contenu doit être équilibré avec des concepts avancés mais accessibles, incluant des exemples pratiques et des études de cas.";
                break;
            case ADVANCED:
                levelAdaptation = "Le contenu doit être technique, spécialisé et inclure des concepts complexes avec des analyses approfondies et des recherches récentes.";
                break;
        }

        // Adapter le contenu selon la classe/audience
        String classAdaptation = "";
        switch (trainingCourse.getStudyClass()) {
            case PRIMARY:
                classAdaptation = "Utiliser un langage simple, des exemples concrets de la vie quotidienne, beaucoup d'images et de couleurs vives. Textes courts et structurés.";
                break;
            case MIDDLE_SCHOOL:
                classAdaptation = "Contenu structuré mais accessible, avec des exemples pratiques, des activités interactives et des schémas explicatifs.";
                break;
            case HIGH_SCHOOL:
                classAdaptation = "Contenu plus technique mais pédagogique, avec des études de cas, des applications réelles et des données concrètes.";
                break;
            case BACHELOR1: case BACHELOR2:
                classAdaptation = "Contenu universitaire rigoureux avec des concepts théoriques, des applications pratiques et des références académiques.";
                break;
            case MASTER1: case MASTER2:
                classAdaptation = "Contenu spécialisé et avancé, incluant des recherches récentes, des analyses critiques et des perspectives innovantes.";
                break;
            case TRAINING:
                classAdaptation = "Contenu pratique et orienté vers les compétences professionnelles, avec des cas concrets du monde du travail et des meilleures pratiques.";
                break;
        }

        // Adapter selon le public cible
        String audienceAdaptation = "";
        switch (trainingCourse.getTargetAudience()) {
            case STUDENTS:
                audienceAdaptation = "Focus sur l'apprentissage progressif avec des exercices, des évaluations formatives et des résumés clés.";
                break;
            case TEACHERS:
                audienceAdaptation = "Inclure des méthodologies pédagogiques, des techniques d'enseignement, des ressources pour la classe et des conseils pratiques.";
                break;
            case PROFESSIONALS:
                audienceAdaptation = "Contenu orienté vers les applications professionnelles, les meilleures pratiques, les retours d'expérience et les outils concrets.";
                break;
        }

        return "Tu es un expert en pédagogie et en design de présentations modernes. " +
            "Ta tâche est de générer une présentation complète et professionnelle du cours suivant en " + language + ". " +
            levelAdaptation + " " + classAdaptation + " " + audienceAdaptation +

            "⚠️ CONTRAINTES STRICTES :" +
            " - ABSOLUMENT 18 SLIDES MINIMUM - NE PAS EN MANQUER" +
            " - Structure obligatoire : " +
            "   1. Page de garde" +
            "   2. Plan détaillé" +
            "   3. Introduction (2 slides)" +
            "   4. Historique/Contexte (2 slides)" +
            "   5. Contenu principal (8-10 slides)" +
            "   6. Avantages/Inconvénients (2 slides)" +
            "   7. Applications pratiques (2 slides)" +
            "   8. Résumé/Conclusion (2 slides)" +
            "   9. Q&A/Contact (1 slide)" +

            " - Chaque slide dans <section class='slide'>...</section>" +
            " - Page de garde avec titre, public, niveau, classe, durée" +

            " - ÉLÉMENTS VISUELS OBLIGATOIRES :" +
            "   * MINIMUM 4 GRAPHIQUES différents adaptés au titre du cours: " + safe(trainingCourse.getTitle()) +
            "   * MINIMUM 4 TABLEAUX différents avec des données structurées" +
            "   * Types de graphiques requis : camemberts, histogrammes, barres, chronologie" +
            "   * Types de tableaux requis : comparatif, technique, chronologique, synthèse" +
            "   * Chaque élément visuel doit être contextualisé et pertinent" +

            " - FORMAT DES GRAPHIQUES :" +
            "   {{GRAPH:type=bar,title=Répartition des données,categories=[Cat1,Cat2,Cat3],values=[v1,v2,v3]}}" +
            "   {{GRAPH:type=pie,title=Répartition en %,labels=[Option A,Option B],values=[60,40]}}" +
            "   {{GRAPH:type=timeline,title=Évolution chronologique,data=[{year:2020,event:Événement1}]}}" +

            " - FORMAT DES TABLEAUX (OBLIGATOIRE) :" +
            "   <div class='table-container'>" +
            "     <table class='styled-table'>" +
            "       <thead><tr><th>Colonne 1</th><th>Colonne 2</th><th>Colonne 3</th></tr></thead>" +
            "       <tbody>" +
            "         <tr><td>Donnée 1</td><td>Donnée 2</td><td>Donnée 3</td></tr>" +
            "         <tr class='highlight'><td><strong>IMPORTANT</strong></td><td><strong>VALEUR</strong></td><td><strong>SPÉCIAL</strong></td></tr>" +
            "         <tr><td>Donnée 4</td><td>Donnée 5</td><td>Donnée 6</td></tr>" +
            "       </tbody>" +
            "     </table>" +
            "   </div>" +

            " - EXEMPLES DE TABLEAUX REQUIS :" +
            "   1. Tableau comparatif (avantages/inconvénients)" +
            "   2. Tableau de spécifications techniques" +
            "   3. Tableau chronologique d'évolution" +
            "   4. Tableau de synthèse des concepts clés" +
            "   5. Tableau de données statistiques" +
            "   6. Tableau de comparaison des méthodes" +

            " - STYLE ET FORMATAGE :" +
            "   * Texte JUSTIFIÉ pour une meilleure lisibilité : style='text-align: justify;'" +
            "   * Densité de texte optimale : ni trop vide, ni trop chargé" +
            "   * Taille de texte comme dans PowerPoint :" +
            "     - Titres : 28-32px" +
            "     - Sous-titres : 24-28px" +
            "     - Corps de texte : 18-22px" +
            "     - Légendes : 14-16px" +
            "   * Marges et espacements équilibrés" +

            " - STYLE CRÉATIF OBLIGATOIRE :" +
            "   * Les titres des slides doivent avoir des couleurs vives et attractives avec dégradés" +
            "   * Alterner les couleurs de titres entre bleu, violet, vert émeraude, orange corail" +
            "   * Ajouter des ombres portées text-shadow: 2px 2px 4px rgba(0,0,0,0.3)" +
            "   * Les mots clés et points importants en <strong>gras</strong> avec couleurs accentuées" +
            "   * Les paragraphes doivent être riches mais concis, adaptés au niveau " + trainingCourse.getLevel() +

            " - STRUCTURE DES SLIDES :" +
            "   * Chaque slide doit contenir des informations correctes et complètes" +
            "   * Éviter le remplissage inutile - chaque mot doit avoir sa raison d'être" +
            "   * Équilibre entre texte et éléments visuels" +
            "   * Utiliser des listes à puces pour les points importants" +
            "   * Inclure des exemples concrets adaptés au public " + trainingCourse.getTargetAudience() +

            " - CHAQUE SLIDE DOIT CONTENIR :" +
            "   * Un titre créatif et informatif" +
            "   * 1-2 paragraphes de texte justifié" +
            "   * Des points clés en <strong>gras</strong>" +
            "   * Des éléments visuels (graphiques OU tableaux)" +
            "   * Un design cohérent et professionnel" +

            " - EXEMPLE DE SLIDE AVEC TABLEAU :" +
            "<section class='slide'>" +
            "  <h2 style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "  color: white; padding: 15px; border-radius: 10px; text-align: center; " +
            "  text-shadow: 2px 2px 4px rgba(0,0,0,0.3); margin: 20px 0; font-size: 30px;\">" +
            "  Titre du Tableau</h2>" +
            "  " +
            "  <p style=\"text-align: justify; font-size: 20px; line-height: 1.6; margin: 15px 0;\">" +
            "  Ce tableau présente une comparaison <strong>détaillée</strong> des différentes " +
            "  approches méthodologiques. Les données sont organisées de manière structurée " +
            "  pour faciliter la compréhension.</p>" +
            "  " +
            "  <div class='table-container'>" +
            "    <table class='styled-table'>" +
            "      <thead><tr><th>Méthode</th><th>Avantages</th><th>Inconvénients</th><th>Application</th></tr></thead>" +
            "      <tbody>" +
            "        <tr><td>Méthode A</td><td>Rapide</td><td>Coûteuse</td><td>Projets courts</td></tr>" +
            "        <tr class='highlight'><td><strong>Méthode B</strong></td><td><strong>Efficace</strong></td><td><strong>Complexe</strong></td><td><strong>Projets longs</strong></td></tr>" +
            "        <tr><td>Méthode C</td><td>Économique</td><td>Lente</td><td>Petits projets</td></tr>" +
            "      </tbody>" +
            "    </table>" +
            "  </div>" +
            "</section>" +

            "\n\nPage de garde EXEMPLAIRE :\n" +
            "<section class='slide cover'>\n" +
            "  <div class='cover-content' style='text-align: center; padding: 40px;'>\n" +
            "    <h1 style=\"font-size: 3em; background: linear-gradient(135deg, #ff6b6b 0%, #4ecdc4 100%); " +
            "    color: white; padding: 30px; border-radius: 15px; margin: 20px 0; " +
            "    text-shadow: 3px 3px 6px rgba(0,0,0,0.4);\">" + safe(trainingCourse.getTitle()) + "</h1>\n" +
            "    <div class='meta' style='font-size: 1.4em; line-height: 1.8;'>\n" +
            "      <p><strong style='color: #667eea;'>Public:</strong> " + safe(trainingCourse.getTargetAudience()) + "</p>\n" +
            "      <p><strong style='color: #764ba2;'>Niveau:</strong> " + safe(trainingCourse.getLevel()) + "</p>\n" +
            "      <p><strong style='color: #28a745;'>Classe:</strong> " + safe(trainingCourse.getStudyClass()) + "</p>\n" +
            "      <p><strong style='color: #fd7e14;'>Durée:</strong> " + (trainingCourse.getDuration() != null ? trainingCourse.getDuration() : "Non spécifiée") + "</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</section>\n\n" +

            "Conclusion du cours pour contexte : " + safe(trainingCourse.getSummary()) + "\n" +
            "Génère EXACTEMENT 18 SLIDES MINIMUM avec :" +
            " - 4 GRAPHIQUES minimum adaptés à \"" + safe(trainingCourse.getTitle()) + "\"" +
            " - 4 TABLEAUX minimum avec données structurées" +
            " - Texte justifié et densité optimale" +
            " - Design professionnel adapté à " + trainingCourse.getTargetAudience() +
            " de niveau " + trainingCourse.getLevel() + " et classe " + trainingCourse.getStudyClass() + ".";
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
        long satisfied = courseEvaluationRepository.countBySatisfaction(3);
        long notSatisfied = courseEvaluationRepository.countBySatisfaction(1);

        long totalCourses = trainingCourseRepository.count(); // total de tous les cours
        long notRated = totalCourses - (satisfied + notSatisfied);
        if (notRated < 0) notRated = 0; // sécurité

        Map<String, Long> stats = new HashMap<>();
        stats.put("Satisfied", satisfied);
        stats.put("NotSatisfied", notSatisfied);
        stats.put("NotRated", notRated);
        stats.put("TotalCourses", totalCourses);

        return stats;
    }


    @Override
    public List<TrainingCourseDTO> findAllByCurrentUser() {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Utilisateur non connecté"));

        User currentUser = userRepository.findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable: " + currentUserLogin));

        return trainingCourseRepository.findByCreatedByLogin(currentUserLogin)
            .stream()
            .map(course -> toDto(course, currentUser)) // ✅ au lieu du mapper simple
            .collect(Collectors.toList());
    }

    @Override
    public List<TrainingCourseDTO> findAllPublic() {
        return trainingCourseRepository.findByPublicPresentationTrue()
            .stream()
            .map(trainingCourseMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<TrainingCourseDTO> getAllCoursesWithSatisfaction(User currentUser) {
        return trainingCourseRepository.findAll()
            .stream()
            .map(course -> toDto(course, currentUser))
            .collect(Collectors.toList());
    }

    public TrainingCourseDTO toDto(TrainingCourse course, User currentUser) {
        TrainingCourseDTO dto = trainingCourseMapper.toDto(course); // utilise ton mapper existant pour les champs standards
        long satisfied = courseEvaluationRepository.countByTrainingCourseAndSatisfaction(course, 3);
        long notSatisfied = courseEvaluationRepository.countByTrainingCourseAndSatisfaction(course, 1);

        dto.setSatisfiedCount(satisfied);
        dto.setNotSatisfiedCount(notSatisfied);
        if (currentUser != null) {
            Optional<CourseEvaluation> maybe = courseEvaluationRepository.findByTrainingCourseAndUser(course, currentUser);
            maybe.ifPresent(ev -> dto.setUserSatisfaction(ev.getSatisfaction()));
        } else {
            dto.setUserSatisfaction(null);
        }

        return dto;
    }

    @Override
    public void evaluateCourse(String courseId, User currentUser, Integer satisfaction) {
        if (currentUser == null) {
            throw new IllegalStateException("User must be authenticated to evaluate");
        }

        TrainingCourse course = trainingCourseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        Optional<CourseEvaluation> existing = courseEvaluationRepository.findByTrainingCourseAndUser(course, currentUser);

        CourseEvaluation evaluation = existing.orElseGet(() -> {
            CourseEvaluation newEval = new CourseEvaluation();
            newEval.setTrainingCourse(course);
            newEval.setUser(currentUser);
            return newEval;
        });
        if (evaluation.getId() != null && Objects.equals(evaluation.getSatisfaction(), satisfaction)) {
            // annuler le vote (toggle off)
            courseEvaluationRepository.delete(evaluation);
            return;
        }
        evaluation.setSatisfaction(satisfaction);
        courseEvaluationRepository.save(evaluation);
    }

    @Override
    public TrainingCourseDTO saveWithoutPresentation(TrainingCourseDTO trainingCourseDTO) {
        log.debug("Request to save TrainingCourse WITHOUT presentation: {}", trainingCourseDTO);

        Optional<String> currentUserLoginOpt = SecurityUtils.getCurrentUserLogin();
        log.debug("Current user login from SecurityUtils: {}", currentUserLoginOpt.orElse("NOT_FOUND"));

        if (trainingCourseDTO.getId() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new IllegalStateException("User must be authenticated to create a TrainingCourse");
            }

            String currentUserLogin = authentication.getName();
            User currentUser = userRepository.findOneByLogin(currentUserLogin)
                .orElseThrow(() -> new IllegalStateException("User not found: " + currentUserLogin));

            trainingCourseDTO.setCreatedBy(currentUser.getFirstName() + " " + currentUser.getLastName());
            trainingCourseDTO.setCreatedByLogin(currentUser.getLogin());
            trainingCourseDTO.setCreatedDate(Instant.now());
        }

        TrainingCourse trainingCourse = trainingCourseMapper.toEntity(trainingCourseDTO);

        // Génération du plan du cours
        try {
            String coursePlan = generateCoursePlan(trainingCourse);
            trainingCourse.setCoursePlan(coursePlan); // Assurez-vous d'avoir ce champ dans l'entité
            log.debug("Course plan generated for course id: {}", trainingCourse.getId());
        } catch (Exception e) {
            log.error("Erreur lors de la génération du plan du cours", e);
            // Vous pouvez choisir de set un plan par défaut ou de laisser null
            trainingCourse.setCoursePlan("<p>Plan non généré</p>");
        }

        trainingCourse = trainingCourseRepository.save(trainingCourse);

        log.debug("TrainingCourse saved WITHOUT presentation but WITH course plan, id: {}", trainingCourse.getId());

        return trainingCourseMapper.toDto(trainingCourse);
    }


    public String generateCoursePlan(TrainingCourse trainingCourse) {
        String language = trainingCourse.getLanguages() == Languages.FRENCH ? "français" : "anglais";

        String prompt = "Tu es un expert en pédagogie et en conception de plans de cours. " +
            "Génère UNIQUEMENT un plan de cours STRUCTURÉ et MODERNE en " + language + " basé EXCLUSIVEMENT sur les informations fournies. " +
            "Le plan doit être composé de CHAPITRES SPÉCIFIQUES avec NUMÉROTATION, SOUS-TITRES DÉTAILLÉS et DURÉES ESTIMÉES.\n\n" +

            "FORMAT HTML STRICT ATTENDU :\n" +
            "<div class='chapter'>\n" +
            "  <h2 class='chapter-title'><span class='chapter-number'>Chapitre 1</span> Nom du chapitre spécifique</h2>\n" +
            "  <div class='chapter-duration'>⏱️ Durée estimée: X heures</div>\n" +
            "  <div class='subtopics'>\n" +
            "    <div class='subtopic'>• Sous-titre détaillé 1</div>\n" +
            "    <div class='subtopic'>• Sous-titre détaillé 2</div>\n" +
            "    <div class='subtopic'>• Sous-titre détaillé 3</div>\n" +
            "  </div>\n" +
            "</div>\n\n" +

            "INFORMATIONS DU COURS FOURNIES PAR L'UTILISATEUR :\n" +
            "- Titre : " + safe(trainingCourse.getTitle()) + "\n" +
            "- Résumé : " + safe(trainingCourse.getSummary()) + "\n" +
            "- Public cible : " + safe(trainingCourse.getTargetAudience()) + "\n" +
            "- Niveau : " + safe(trainingCourse.getLevel()) + "\n" +
            "- Classe : " + safe(trainingCourse.getStudyClass()) + "\n" +
            "- Enseignant : " + safe(trainingCourse.getInstructor()) + "\n" +
            "- Durée totale : " + (trainingCourse.getDuration() != null ? trainingCourse.getDuration() : "Non spécifiée") + "\n" +
            "- Type de lieu : " + safe(trainingCourse.getLocationType()) + "\n\n" +

            "CONTRAINTES STRICTES :\n" +
            "1. Génère 5-8 CHAPITRES SPÉCIFIQUES basés sur le titre et le résumé\n" +
            "2. NUMÉROTE chaque chapitre (Chapitre 1, Chapitre 2, etc.)\n" +
            "3. Chaque chapitre doit avoir 3-5 SOUS-TITRES DÉTAILLÉS pertinents\n" +
            "4. CALCULE AUTOMATIQUEMENT une DURÉE ESTIMÉE réaliste pour chaque chapitre\n" +
            "5. CALCULE et AFFICHE la DURÉE TOTALE automatiquement en additionnant toutes les durées des chapitres\n" +
            "6. Adapte la complexité au niveau : " + safe(trainingCourse.getLevel()) + "\n" +
            "7. Structure cohérente avec le public : " + safe(trainingCourse.getTargetAudience()) + "\n" +
            "8. Format HTML MODERNE avec STYLE ATTRACTIF\n" +
            "9. Texte JUSTIFIÉ et bien structuré\n\n" +

            "STRUCTURE OBLIGATOIRE :\n" +
            "- En-tête avec titre du cours\n" +
            "- 5-8 chapitres principaux NUMÉROTÉS\n" +
            "- Sous-titres détaillés pour chaque chapitre\n" +
            "- Durées estimées réalistes CALCULÉES AUTOMATIQUEMENT\n" +
            "- Total des heures CALCULÉ AUTOMATIQUEMENT\n\n" +

            "STYLE OBLIGATOIRE POUR LE FRONTEND :\n" +
            "- Design épuré sans arrière-plan coloré pour le titre\n" +
            "- Texte aligné à GAUCHE (pas centré)\n" +
            "- Style de texte cohérent pour tout le plan\n" +
            "- Chapitres alignés à GAUCHE\n" +
            "- Sous-titres sous chaque chapitre\n" +
            "- Design responsive et attractif\n" +
            "- Icônes pour les durées (⏱️)\n" +
            "- Bordures arrondies et effets d'ombre\n" +
            "- Police moderne: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif\n\n" +

            "EXEMPLE DE FORMAT ATTENDU :\n" +
            "<div class='modern-course-plan'>\n" +
            "  <div class='plan-header'>\n" +
            "    <h1>📋 PLAN DU COURS: " + safe(trainingCourse.getTitle()) + "</h1>\n" +
            "    <p class='total-duration'>⏱️ Durée totale calculée: X heures</p>\n" +
            "  </div>\n" +
            "  \n" +
            "  <div class='chapters-container'>\n" +
            "    <div class='chapter'>\n" +
            "      <h2 class='chapter-title'><span class='chapter-number'>Chapitre 1</span> Introduction aux concepts de base</h2>\n" +
            "      <div class='chapter-duration'>⏱️ Durée estimée: 2 heures</div>\n" +
            "      <div class='subtopics'>\n" +
            "        <div class='subtopic'>• Présentation des objectifs du cours</div>\n" +
            "        <div class='subtopic'>• Définitions fondamentales</div>\n" +
            "        <div class='subtopic'>• Importance dans le domaine</div>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>\n\n" +

            "IMPORTANT :\n" +
            "- CALCULE AUTOMATIQUEMENT les durées de chaque chapitre\n" +
            "- ADDITIONNE toutes les durées pour obtenir le TOTAL\n" +
            "- AFFICHE le TOTAL dans l'en-tête\n" +
            "- ALIGNE TOUT À GAUCHE (pas de centrage)\n" +
            "- STYLE ÉPURÉ sans arrière-plan coloré pour le titre\n" +
            "- Sois SPÉCIFIQUE au cours : " + safe(trainingCourse.getTitle()) + "\n" +
            "- Base-toi sur le résumé : " + safe(trainingCourse.getSummary()) + "\n" +
            "- Durées REALISTES et COHÉRENTES\n" +
            "- Sous-titres DÉTAILLÉS et PERTINENTS\n" +
            "- MINIMUM 5 chapitres, MAXIMUM 8 chapitres\n" +
            "- Retourne UNIQUEMENT du HTML sans commentaires";

        try {
            String rawPlan = aiClient.generatePresentation(prompt);
            return enhanceCoursePlanStyle(rawPlan, trainingCourse.getTitle());
        } catch (Exception e) {
            log.error("Erreur génération plan du cours", e);
            return createGenericCoursePlan(trainingCourse);
        }
    }

    private String enhanceCoursePlanStyle(String rawHtml, String courseTitle) {
        if (rawHtml == null || rawHtml.trim().isEmpty()) {
            return createGenericCoursePlan(null);
        }

        String cleaned = rawHtml.replaceAll("```html", "").replaceAll("```", "").trim();

        // Ajouter le conteneur principal si absent
        if (!cleaned.contains("modern-course-plan")) {
            cleaned = "<div class='modern-course-plan'>" + cleaned + "</div>";
        }

        // Supprimer les arrière-plans colorés et aligner à gauche
        cleaned = cleaned.replaceAll(
            "style=['\"][^'\"]*background:[^;]*;[^'\"]*['\"]",
            "style=''"
        );

        // Améliorer le style des chapitres (alignement à gauche)
        cleaned = cleaned.replaceAll(
            "<div class='chapter'",
            "<div class='chapter' style='" +
                "padding: 20px; border-radius: 8px; margin-bottom: 20px; " +
                "background: #f8f9fa; border-left: 4px solid #4facfe; " +
                "box-shadow: 0 2px 8px rgba(0,0,0,0.1); transition: transform 0.3s ease;' " +
                "onmouseover=\"this.style.transform='translateY(-2px)'\" " +
                "onmouseout=\"this.style.transform='none'\""
        );

        // Améliorer les titres de chapitres (alignement à gauche)
        cleaned = cleaned.replaceAll(
            "<h2 class='chapter-title'",
            "<h2 class='chapter-title' style='" +
                "margin: 0 0 10px 0; font-size: 20px; " +
                "font-weight: 700; color: #2c3e50; display: flex; " +
                "align-items: center; gap: 10px;'"
        );

        // Améliorer la numérotation des chapitres
        cleaned = cleaned.replaceAll(
            "<span class='chapter-number'",
            "<span class='chapter-number' style='" +
                "background: #4facfe; color: white; " +
                "padding: 4px 10px; border-radius: 15px; font-weight: bold; font-size: 12px; " +
                "min-width: 70px; text-align: center;'"
        );

        // Améliorer les durées
        cleaned = cleaned.replaceAll(
            "<div class='chapter-duration'",
            "<div class='chapter-duration' style='" +
                "font-size: 14px; margin-bottom: 15px; color: #666; " +
                "background: #e3f2fd; padding: 6px 12px; border-radius: 15px; " +
                "display: inline-block; font-weight: 500;'"
        );

        // Améliorer les sous-titres
        cleaned = cleaned.replaceAll(
            "<div class='subtopic'",
            "<div class='subtopic' style='" +
                "margin: 6px 0; padding: 4px 8px; color: #555; " +
                "background: #ffffff; border-radius: 4px; font-size: 14px; " +
                "border-left: 2px solid #4facfe;'"
        );

        // Ajouter l'en-tête épuré si absent
        if (!cleaned.contains("plan-header")) {
            String header = "<div class='plan-header' style='" +
                "margin-bottom: 30px; padding: 20px 0; border-bottom: 2px solid #e9ecef;'>" +
                "<h1 style='margin: 0 0 10px 0; font-size: 28px; font-weight: 700; " +
                "color: #2c3e50; text-align: left;'>" +
                "📋 PLAN DU COURS: " + "</h1>" +
                "<p class='total-duration' style='margin: 0; font-size: 16px; color: #666; " +
                "font-weight: 500; text-align: left;'>" +
                "⏱️ Durée totale calculée automatiquement</p>" +
                "</div>";

            cleaned = cleaned.replace("<div class='modern-course-plan'>",
                "<div class='modern-course-plan'>" + header);
        }

        return cleaned;
    }

    private String createGenericCoursePlan(TrainingCourse trainingCourse) {
        String title = trainingCourse != null ? safe(trainingCourse.getTitle()) : "Cours";

        return "<div class='modern-course-plan' style='font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px;'>" +
            "<div class='plan-header' style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
            "color: white; padding: 25px; border-radius: 12px; text-align: center; margin-bottom: 30px; " +
            "box-shadow: 0 4px 15px rgba(0,0,0,0.2);'>" +
            "<h1 style='margin: 0; font-size: 32px; font-weight: bold; text-shadow: 2px 2px 4px rgba(0,0,0,0.3);'>" +
            "📋 PLAN DU COURS</h1>" +
            "<p style='margin: 10px 0 0 0; font-size: 18px; opacity: 0.9;'>" + title + "</p>" +
            "</div>" +

            "<div class='plan-items'>" +

            "<div class='plan-item' style='background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); " +
            "padding: 15px 20px; border-radius: 8px; margin-bottom: 12px; color: white; " +
            "box-shadow: 0 2px 6px rgba(0,0,0,0.1); transition: transform 0.2s ease; cursor: pointer;' " +
            "onmouseover=\"this.style.transform='translateY(-2px)'\" " +
            "onmouseout=\"this.style.transform='none'\">" +
            "<h2 style='margin: 0; font-size: 20px; font-weight: 600; text-shadow: 1px 1px 2px rgba(0,0,0,0.2);'>Présentation du cours</h2>" +
            "</div>" +

            "</div>" +
            "</div>";
    }


    private String cleanAndStructureCoursePlan(String rawHtml) {
        if (rawHtml == null || rawHtml.trim().isEmpty()) {
            return "<div class='course-plan'><h3>Plan du Cours</h3><p>Plan non disponible.</p></div>";
        }

        String cleaned = rawHtml.replaceAll("```html", "").replaceAll("```", "").trim();

        if (!cleaned.contains("<div") && !cleaned.contains("<h")) {
            cleaned = "<div class='course-plan'>" + cleaned + "</div>";
        }

        return cleaned;
    }


}
