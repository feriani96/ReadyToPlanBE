package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.TrainingCourse;
import com.readytoplanbe.myapp.repository.TrainingCourseRepository;
import com.readytoplanbe.myapp.service.TrainingCourseService;
import com.readytoplanbe.myapp.service.ai.AIClient;
import com.readytoplanbe.myapp.service.dto.TrainingCourseDTO;
import com.readytoplanbe.myapp.service.mapper.TrainingCourseMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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


    public TrainingCourseServiceImpl(TrainingCourseRepository trainingCourseRepository, TrainingCourseMapper trainingCourseMapper, AIClient aiClient) {
        this.trainingCourseRepository = trainingCourseRepository;
        this.trainingCourseMapper = trainingCourseMapper;
        this.aiClient = aiClient;
    }

    @Override
    public TrainingCourseDTO save(TrainingCourseDTO trainingCourseDTO) {
        log.debug("Request to save TrainingCourse : {}", trainingCourseDTO);
        TrainingCourse trainingCourse = trainingCourseMapper.toEntity(trainingCourseDTO);
        trainingCourse = trainingCourseRepository.save(trainingCourse);

        // Générer la présentation via AI
        String presentation = "";
        try {
            presentation = generatePresentation(trainingCourse.getId());
            trainingCourse.setPresentation(presentation);
            trainingCourse = trainingCourseRepository.save(trainingCourse);  // sauvegarder la présentation
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

    public String generatePresentation(String trainingCourseId) {
        TrainingCourse trainingCourse = trainingCourseRepository.findById(trainingCourseId)
            .orElseThrow(() -> new RuntimeException("TrainingCourse introuvable"));

        String prompt = buildPrompt(trainingCourse);

        try {
            return aiClient.generatePresentation(prompt);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la génération de la présentation.";
        }
    }

    private String buildPrompt(TrainingCourse trainingCourse) {
        return "Tu es un expert en création de contenu pédagogique. "
            + "Crée une présentation professionnelle et engageante pour le cours suivant :\n\n"
            + "**Titre du cours** : " + trainingCourse.getTitle() + "\n"
            + "**Public cible** : " + trainingCourse.getTargetAudience() + "\n"
            + "**Niveau** : " + trainingCourse.getLevel() + "\n"
            + "**Description** : " + (trainingCourse.getSummary() != null ? trainingCourse.getSummary() : "N/A") + "\n\n"
            + "La présentation doit :\n"
            + "- Être concise (environ 200 mots)\n"
            + "- Mettre en valeur les points clés du cours\n"
            + "- Inclure une introduction accrocheuse\n"
            + "- Mentionner les compétences que les participants acquerront\n"
            + "- Utiliser un ton professionnel mais accessible";
    }
}
