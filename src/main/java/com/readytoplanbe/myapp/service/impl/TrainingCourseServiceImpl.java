package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.TrainingCourse;
import com.readytoplanbe.myapp.domain.enumeration.Languages;
import com.readytoplanbe.myapp.domain.enumeration.Level;
import com.readytoplanbe.myapp.repository.TrainingCourseRepository;
import com.readytoplanbe.myapp.service.TrainingCourseService;
import com.readytoplanbe.myapp.service.ai.AIClient;
import com.readytoplanbe.myapp.service.chart.ChartService;
import com.readytoplanbe.myapp.service.dto.TrainingCourseDTO;
import com.readytoplanbe.myapp.service.mapper.TrainingCourseMapper;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public TrainingCourseServiceImpl(TrainingCourseRepository trainingCourseRepository, TrainingCourseMapper trainingCourseMapper, AIClient aiClient, ChartService chartService) {
        this.trainingCourseRepository = trainingCourseRepository;
        this.trainingCourseMapper = trainingCourseMapper;
        this.aiClient = aiClient;
        this.chartService = chartService;
    }

    @Override
    public TrainingCourseDTO save(TrainingCourseDTO trainingCourseDTO) {
        log.debug("Request to save TrainingCourse : {}", trainingCourseDTO);
        TrainingCourse trainingCourse = trainingCourseMapper.toEntity(trainingCourseDTO);
        trainingCourse = trainingCourseRepository.save(trainingCourse);

        try {
            String presentationHtml = generatePresentation(trainingCourse.getId());
            trainingCourse.setPresentation(presentationHtml);
            trainingCourse = trainingCourseRepository.save(trainingCourse);
        } catch (Exception e) {
            log.error("Erreur lors de la génération de la présentation AI", e);
        }

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

            Pattern pattern = Pattern.compile("\\{\\{GRAPH:(.*?)\\}\\}", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(rawHtml);
            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {
                if (matcher.groupCount() >= 1) {
                    String chartPrompt = matcher.group(1).trim();
                    try {
                        String chartJsonData = aiClient.generateChartData(chartPrompt);
                        byte[] chartImageBytes = chartService.createChart(chartJsonData); // Changé ici: createChart au lieu de createBarChart
                        String base64 = Base64.getEncoder().encodeToString(chartImageBytes);
                        String imgTag = "<div class='chart'><img src=\"data:image/png;base64," + base64 + "\" style=\"max-width:100%;border-radius:8px;\"/></div>";
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(imgTag));
                    } catch (Exception imgEx) {
                        log.error("Erreur génération image pour prompt: {}", chartPrompt, imgEx);
                        String fallback = "<div class='chart-placeholder'>[Graphique indisponible: " + chartPrompt + "]</div>";
                        matcher.appendReplacement(sb, Matcher.quoteReplacement(fallback));
                    }
                } else {
                    log.warn("AI returned a malformed chart placeholder without a group. Skipping.");
                    matcher.appendReplacement(sb, Matcher.quoteReplacement(""));
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

    private String injectGlobalStylesIfMissing(String html) {
        if (html.contains("<style") || html.contains("class='slide'") || html.contains("class=\"slide\"")) {
            if (!html.contains("<style id=\"global-presentation-style\">")) {
                String globalStyle = "<style id=\"global-presentation-style\">"
                    + "body{font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#FFFFFF; color:#333;}"
                    + ".slide{padding:40px;box-sizing:border-box;min-height:400px;} .cover{display:flex;align-items:center;justify-content:center;}"
                    + ".chart{margin-top:20px;text-align:center;} .chart img{max-width:100%;height:auto;border-radius:8px;}"
                    + "table{border-collapse:collapse;width:100%;font-size:14px;margin-top:20px;border-radius:8px;overflow:hidden;}"
                    + "th{background:#3498DB;color:#fff;padding:12px;text-align:left;} td{background:#ECF0F1;padding:10px;border-bottom:1px solid #ddd;}"
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
                + "table{border-collapse:collapse;width:100%;font-size:14px;margin-top:20px;} "
                + "th{background:#3498DB;color:#fff;padding:12px;text-align:left;} "
                + "td{background:#ECF0F1;padding:10px;border-bottom:1px solid #ddd;} "
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
            "⚠️ Contraintes strictes :" +
            " - Minimum 15 slides obligatoires." +
            " - Structure logique : Page de garde, Introduction générale, Historique et évolution, Plan du cours, " +
            "   Contenu détaillé (plusieurs slides selon le besoin), Avantages et Inconvénients, Applications, Résumé final." +
            " - Chaque slide doit être dans une balise <section class='slide'>...</section>." +
            " - La page de garde doit toujours être la première slide avec le titre, le public cible, le niveau, la durée et la classe." +
            " - Le niveau du cours est " + trainingCourse.getLevel() + ", adapte la profondeur du contenu en conséquence." +
            " - Chaque slide de contenu doit comporter au moins deux paragraphes explicatifs." +
            " - Intègre dans les slides : des tableaux (concepts, exemples, comparatifs), des graphiques (camemberts, histogrammes, barres), et une chronologie adaptée." +
            " - Pour les graphiques, insère un placeholder explicite au format : {{GRAPH:type=bar,title=Répartition,...}} " +
            "   que je remplacerai ensuite par une image." +
            " - Interdis tout code <script> ou markdown." +
            " - Génère du HTML clair et prêt à être affiché, sans afficher les balises comme texte." +

            "\n\nPage de garde :\n" +
            "<section class='slide cover'>\n" +
            "  <div class='cover-content'>\n" +
            "    <h1>" + trainingCourse.getTitle() + "</h1>\n" +
            "    <div class='meta'>\n" +
            "      <p><strong>Public:</strong> " + safe(trainingCourse.getTargetAudience()) + "</p>\n" +
            "      <p><strong>Niveau:</strong> " + safe(trainingCourse.getLevel()) + "</p>\n" +
            "      <p><strong>Classe:</strong> " + safe(trainingCourse.getStudyClass()) + "</p>\n" +
            "      <p><strong>Durée:</strong> " + (trainingCourse.getDuration() != null ? trainingCourse.getDuration() : "Non spécifiée") + "</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</section>\n\n" +

            "Résumé du cours : " + safe(trainingCourse.getSummary()) + "\n" +
            "Fin.";
    }

    private String safe(Object obj) {
        return obj == null ? "Non spécifié" : obj.toString();
    }
}
