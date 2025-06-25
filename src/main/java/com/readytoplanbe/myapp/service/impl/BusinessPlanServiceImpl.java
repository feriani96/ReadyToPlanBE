package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.BusinessPlan;
import com.readytoplanbe.myapp.domain.Slide;
import com.readytoplanbe.myapp.domain.enumeration.ExportFormat;
import com.readytoplanbe.myapp.repository.BusinessPlanRepository;
import com.readytoplanbe.myapp.service.BusinessPlanGeneratorService;
import com.readytoplanbe.myapp.service.BusinessPlanService;
import com.readytoplanbe.myapp.service.PresentationExportService;
import com.readytoplanbe.myapp.service.dto.BusinessPlanDTO;
import com.readytoplanbe.myapp.service.dto.BusinessPlanInputDTO;
import com.readytoplanbe.myapp.service.mapper.BusinessPlanMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.core.io.Resource;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessPlanServiceImpl implements BusinessPlanService {

    private final Logger log = LoggerFactory.getLogger(BusinessPlanServiceImpl.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final BusinessPlanRepository businessPlanRepository;

    private final BusinessPlanMapper businessPlanMapper;

    private final BusinessPlanGeneratorService generatorService;

    private final PresentationExportService exportService;
    @Value("${gemini.api.key}")
    private String apikey;

    public BusinessPlanServiceImpl(
        BusinessPlanRepository businessPlanRepository,
        BusinessPlanMapper businessPlanMapper,
        BusinessPlanGeneratorService generatorService,
        PresentationExportService exportService) {
        this.businessPlanRepository = businessPlanRepository;
        this.businessPlanMapper = businessPlanMapper;
        this.generatorService = generatorService;
        this.exportService = exportService;
    }

    /**
     * Génère une présentation pour une entreprise donnée en utilisant l'API Gemini.
     * Si une présentation éditée existe déjà, la génération est annulée.
     */
    @Override
    public String generatePresentation(BusinessPlanInputDTO input) {
        log.info("Génération de la présentation pour l'entreprise : {}", input.getCompanyName());

        businessPlanRepository.findByCompanyName(input.getCompanyName())
            .ifPresent(businessPlan -> {
                if (businessPlan.isEdited()) {
                    log.warn("Présentation déjà éditée pour l'entreprise {}. Génération annulée.", input.getCompanyName());
                    throw new RuntimeException("Présentation modifiée manuellement, génération impossible.");
                }
            });

        String presentation = generatorService.generatePresentation(input, apikey);
        log.info("Présentation générée avec succès pour l'entreprise : {}", input.getCompanyName());
        return presentation;
    }

    /**
     * Récupère la présentation pour une entreprise donnée.
     * Retourne la version éditée si elle existe, sinon la version générée.
     * Si aucune version n'est valide, une nouvelle présentation est générée.
     */
    @Override
    public String getPresentation(String companyName) {
        List<Slide> slides = getPresentationSlides(companyName);

        StringBuilder builder = new StringBuilder();
        for (Slide slide : slides) {
            if (builder.length() > 0) {
                builder.append("\n\n===SLIDE===\n\n");
            }
            builder.append(slide.getContent().trim());
        }

        return builder.toString();
    }

    @Override
    public List<Slide> getPresentationSlides(String companyName) {
        BusinessPlan businessPlan = businessPlanRepository.findByCompanyName(companyName)
            .orElseThrow(() -> new EntityNotFoundException("Aucun BusinessPlan trouvé pour : " + companyName));

        String presentationContent = chooseBestPresentation(businessPlan);

        if (presentationContent == null) {
            // Générer nouvelle présentation
            BusinessPlanInputDTO input = new BusinessPlanInputDTO(mapToDTO(businessPlan));
            presentationContent = generatorService.generatePresentation(input, apikey);

            // Convertir immédiatement en JSON pour stockage
            List<Slide> slides = parseMarkdownToSlides(presentationContent);
            try {
                presentationContent = objectMapper.writeValueAsString(slides);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur de conversion en JSON", e);
            }

            businessPlan.setGeneratedPresentation(presentationContent);
            businessPlanRepository.save(businessPlan);
            return slides;
        }

        // Le contenu stocké est toujours du JSON maintenant
        try {
            return objectMapper.readValue(presentationContent, new TypeReference<List<Slide>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Erreur de parsing JSON", e);
        }
    }

    private List<Slide> parseMarkdownToSlides(String markdownContent) {
        List<Slide> slides = new ArrayList<>();
        if (markdownContent == null || markdownContent.trim().isEmpty()) {
            return slides;
        }

        String normalizedContent = markdownContent.replace("\r\n", "\n").trim();

        // Détection du séparateur ===SLIDE===
        if (normalizedContent.contains("===SLIDE===")) {
            String[] slideContents = normalizedContent.split("(?m)^===SLIDE===\\s*$");
            for (String content : slideContents) {
                if (!content.trim().isEmpty()) {
                    Slide slide = new Slide();
                    slide.setContent(content.trim());
                    slides.add(slide);
                }
            }
            return slides;
        }

        // Détection des slides par titres principaux (h1)
        String[] h1Sections = normalizedContent.split("(?m)^#\\s+(?![#])(?=\\S)");
        if (h1Sections.length > 1) {
            for (int i = 0; i < h1Sections.length; i++) {
                String content = h1Sections[i].trim();
                if (!content.isEmpty()) {
                    Slide slide = new Slide();
                    // Pour le premier élément après split, on n'ajoute pas de #
                    slide.setContent(i == 0 ? content : "# " + content);
                    slides.add(slide);
                }
            }
            return slides;
        }

        // Fallback - tout dans une slide
        Slide fallbackSlide = new Slide();
        fallbackSlide.setContent(normalizedContent);
        slides.add(fallbackSlide);
        return slides;
    }

    /**
     * Met à jour le contenu d'un slide spécifique dans la présentation générée.
     */
    @Override
    public Optional<BusinessPlanDTO> updateGeneratedPresentation(String id, int slideIndex, String newContent) {
        log.info("Mise à jour de la présentation générée pour le BusinessPlan id: {}, slideIndex: {}", id, slideIndex);

        BusinessPlan businessPlan = businessPlanRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BusinessPlan non trouvé avec l'id: " + id));

        String updatedJson = updateSlideContent(businessPlan.getGeneratedPresentation(), slideIndex, newContent);
        businessPlan.setGeneratedPresentation(updatedJson);

        BusinessPlan updated = businessPlanRepository.save(businessPlan);
        log.info("Présentation mise à jour avec succès pour l'id: {}", id);
        return Optional.of(businessPlanMapper.toDto(updated));
    }

    /**
     * Sauvegarde un nouveau BusinessPlan.
     */
    @Override
    public BusinessPlanDTO save(BusinessPlanDTO businessPlanDTO) {
        log.debug("Sauvegarde du BusinessPlan : {}", businessPlanDTO);
        BusinessPlan businessPlan = businessPlanMapper.toEntity(businessPlanDTO);

        businessPlan = businessPlanRepository.save(businessPlan);
        log.info("BusinessPlan sauvegardé avec l'id: {}", businessPlan.getId());
        return businessPlanMapper.toDto(businessPlan);
    }

    /**
     * Met à jour un BusinessPlan existant et régénère la présentation.
     */
    @Override
    public BusinessPlanDTO update(BusinessPlanDTO businessPlanDTO) {
        log.debug("Mise à jour du BusinessPlan : {}", businessPlanDTO);
        BusinessPlan businessPlan = businessPlanMapper.toEntity(businessPlanDTO);

        BusinessPlanInputDTO input = new BusinessPlanInputDTO(businessPlanDTO);
        String presentation = generatorService.generatePresentation(input, apikey);
        businessPlan.setGeneratedPresentation(presentation);

        businessPlan = businessPlanRepository.save(businessPlan);
        log.info("BusinessPlan mis à jour avec l'id: {}", businessPlan.getId());
        return businessPlanMapper.toDto(businessPlan);
    }

    /**
     * Mise à jour partielle d'un BusinessPlan.
     */
    @Override
    public Optional<BusinessPlanDTO> partialUpdate(BusinessPlanDTO businessPlanDTO) {
        log.debug("Mise à jour partielle du BusinessPlan : {}", businessPlanDTO);

        return businessPlanRepository
            .findById(businessPlanDTO.getId())
            .map(existingBusinessPlan -> {
                businessPlanMapper.partialUpdate(existingBusinessPlan, businessPlanDTO);
                log.info("Mise à jour partielle effectuée pour le BusinessPlan id: {}", existingBusinessPlan.getId());
                return existingBusinessPlan;
            })
            .map(businessPlanRepository::save)
            .map(businessPlanMapper::toDto);
    }

    /**
     * Récupère tous les BusinessPlans avec pagination.
     */
    @Override
    public Page<BusinessPlanDTO> findAll(Pageable pageable) {
        log.debug("Récupération de tous les BusinessPlans - page: {}", pageable.getPageNumber());
        return businessPlanRepository.findAll(pageable).map(businessPlanMapper::toDto);
    }

    /**
     * Récupère un BusinessPlan par son identifiant.
     */
    @Override
    public Optional<BusinessPlanDTO> findOne(String id) {
        log.debug("Récupération du BusinessPlan par id: {}", id);
        return businessPlanRepository.findById(id).map(businessPlanMapper::toDto);
    }

    /**
     * Supprime un BusinessPlan par son identifiant.
     */
    @Override
    public void delete(String id) {
        log.info("Suppression du BusinessPlan avec l'id: {}", id);
        businessPlanRepository.deleteById(id);
    }

    // Méthodes utilitaires privées

    /**
     * Choisit la meilleure présentation disponible : éditée si existante, sinon générée.
     */
    private String chooseBestPresentation(BusinessPlan businessPlan) {
        // Vérifie d'abord la présentation éditée
        if (isPresentationValid(businessPlan.getEditedPresentation())) {
            log.debug("Utilisation de la présentation éditée pour l'entreprise : {}", businessPlan.getCompanyName());
            return businessPlan.getEditedPresentation();
        }

        // Sinon vérifie la présentation générée
        if (isPresentationValid(businessPlan.getGeneratedPresentation())) {
            log.debug("Utilisation de la présentation générée pour l'entreprise : {}", businessPlan.getCompanyName());
            return businessPlan.getGeneratedPresentation();
        }

        // Aucune présentation valide trouvée
        log.warn("Aucune présentation valide trouvée pour l'entreprise : {}", businessPlan.getCompanyName());
        return null;
    }

    /**
     * Régénère la présentation pour un BusinessPlan donné.
     */
    private String regeneratePresentation(BusinessPlan businessPlan) {
        BusinessPlanInputDTO input = new BusinessPlanInputDTO(mapToDTO(businessPlan));
        String generatedText = generatorService.generatePresentation(input, apikey);

        List<String> slides = Arrays.stream(generatedText.split("(?=\\d+\\.\\s)"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());

        try {
            String json = objectMapper.writeValueAsString(slides);
            businessPlan.setGeneratedPresentation(json);
            businessPlanRepository.save(businessPlan);
            log.info("Présentation régénérée et sauvegardée pour l'entreprise : {}", businessPlan.getCompanyName());
            return json;
        } catch (JsonProcessingException e) {
            log.error("Erreur lors de la conversion de la présentation en JSON", e);
            throw new RuntimeException("Erreur JSON lors de la régénération de la présentation", e);
        }
    }

    /**
     * Met à jour le contenu d'un slide spécifique dans la présentation JSON.
     */
    /**
     * Met à jour le contenu d'un slide spécifique dans la présentation JSON.
     */
    private String updateSlideContent(String presentationJson, int index, String content) {
        if (presentationJson == null || presentationJson.isBlank()) {
            // Si la présentation est vide, crée un nouveau tableau JSON avec le contenu
            try {
                ArrayNode newArray = objectMapper.createArrayNode();
                newArray.add(content);
                return objectMapper.writeValueAsString(newArray);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur lors de la création d'une nouvelle présentation JSON", e);
            }
        }

        try {
            // Essayer de parser comme JSON
            JsonNode node = objectMapper.readTree(presentationJson);

            if (node.isArray()) {
                ArrayNode slidesArray = (ArrayNode) node;
                // Si l'index est hors limites, étendre le tableau
                while (index >= slidesArray.size()) {
                    slidesArray.add("");
                }
                slidesArray.set(index, new TextNode(content));
                return objectMapper.writeValueAsString(slidesArray);
            } else if (node.isTextual()) {
                // Si c'est du texte brut, le convertir en tableau JSON
                ArrayNode newArray = objectMapper.createArrayNode();
                newArray.add(content);
                return objectMapper.writeValueAsString(newArray);
            }
        } catch (IOException e) {
            log.warn("Le contenu de la présentation n'est pas du JSON valide, traitement comme texte brut");
        }

        // Fallback pour le contenu non-JSON (markdown avec séparateurs)
        try {
            String[] slides = presentationJson.split("===SLIDE===");
            // Étendre le tableau si nécessaire
            if (index >= slides.length) {
                slides = Arrays.copyOf(slides, index + 1);
            }
            slides[index] = content;
            return String.join("===SLIDE===", slides);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du traitement du contenu Markdown", e);
        }
    }

    /**
     * Vérifie si une présentation est valide (non nulle et non vide).
     */
    private boolean isPresentationValid(String presentation) {
        return presentation != null && !presentation.trim().isEmpty();
    }

    /**
     * Vérifie si une chaîne JSON représente une présentation valide.
     */
    private boolean isJsonValid(String json) {
        if (!isPresentationValid(json)) return false;

        try {
            JsonNode node = objectMapper.readTree(json);
            return node.isArray() || node.isTextual(); // accepte tableau ou simple texte
        } catch (IOException e) {
            // Ce n'est pas du JSON, mais on peut quand même l'accepter comme du markdown
            log.warn("Contenu non JSON détecté, mais accepté comme texte/markdown.");
            return true;
        }
    }

    @Override
    public org.springframework.core.io.Resource exportPresentation(String companyName, ExportFormat format) throws IOException {
        BusinessPlan businessPlan = businessPlanRepository.findByCompanyName(companyName)
            .orElseThrow(() -> new EntityNotFoundException("Aucun BusinessPlan trouvé pour : " + companyName));

        String presentationContent = chooseBestPresentation(businessPlan);
        if (presentationContent == null) {
            throw new IllegalStateException("Aucun contenu de présentation disponible");
        }

        File exportFile;
        switch (format) {
            case PDF:
                exportFile = exportService.exportToPdf(companyName, presentationContent);
                break;
            case PPTX:
                exportFile = exportService.exportToPptx(companyName, presentationContent);
                break;
            default:
                throw new IllegalArgumentException("Format non supporté: " + format);
        }

        return new FileSystemResource(exportFile) {
            private static final long serialVersionUID = 1L;

            @Override
            public String getFilename() {
                return String.format("BusinessPlan-%s-%s.%s",
                    companyName,
                    LocalDate.now().toString(),
                    format.toString().toLowerCase());
            }
        };
    }

    /**
     * Convertit une entité BusinessPlan en DTO.
     */
    private BusinessPlanDTO mapToDTO(BusinessPlan businessPlan) {
        BusinessPlanDTO dto = new BusinessPlanDTO();
        dto.setCompanyName(businessPlan.getCompanyName());
        dto.setCompanyStartDate(businessPlan.getCompanyStartDate());
        dto.setCountry(businessPlan.getCountry());
        dto.setLanguages(businessPlan.getLanguages());
        dto.setCompanyDescription(businessPlan.getCompanyDescription());
        dto.setAnticipatedProjectSize(businessPlan.getAnticipatedProjectSize());
        dto.setCurrency(businessPlan.getCurrency());
        return dto;
    }

    private String convertSlidesToText(List<Slide> slides) {
        return slides.stream()
            .map(Slide::getContent)
            .collect(Collectors.joining("\n\n===SLIDE===\n\n"));
    }

    private void checkForExistingBusinessPlan(String companyName) {
        businessPlanRepository.findByCompanyName(companyName)
            .ifPresent(bp -> {
                log.warn("Business plan already exists for company: {}", companyName);
                throw new BusinessPlanAlreadyExistsException("Business plan already exists");
            });
    }

    private String generatePresentationContent(BusinessPlanInputDTO input) {
        try {
            return generatorService.generatePresentation(input, apikey);
        } catch (Exception e) {
            log.error("Failed to generate presentation", e);
            throw new PresentationGenerationException("Presentation generation failed");
        }
    }

    public class BusinessPlanAlreadyExistsException extends RuntimeException {
        public BusinessPlanAlreadyExistsException(String message) {
            super(message);
        }
    }

    public class PresentationGenerationException extends RuntimeException {
        public PresentationGenerationException(String message) {
            super(message);
        }
    }


}


