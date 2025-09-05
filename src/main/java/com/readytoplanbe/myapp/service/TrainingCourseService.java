package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.domain.TrainingCourse;
import com.readytoplanbe.myapp.service.dto.TrainingCourseDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.readytoplanbe.myapp.domain.TrainingCourse}.
 */
public interface TrainingCourseService {
    /**
     * Save a trainingCourse.
     *
     * @param trainingCourseDTO the entity to save.
     * @return the persisted entity.
     */
    TrainingCourseDTO save(TrainingCourseDTO trainingCourseDTO);

    /**
     * Updates a trainingCourse.
     *
     * @param trainingCourseDTO the entity to update.
     * @return the persisted entity.
     */
    TrainingCourseDTO update(TrainingCourseDTO trainingCourseDTO);

    /**
     * Partially updates a trainingCourse.
     *
     * @param trainingCourseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrainingCourseDTO> partialUpdate(TrainingCourseDTO trainingCourseDTO);

    /**
     * Get all the trainingCourses.
     *
     * @return the list of entities.
     */
    List<TrainingCourseDTO> findAll();

    /**
     * Get the "id" trainingCourse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrainingCourseDTO> findOne(String id);

    /**
     * Delete the "id" trainingCourse.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    TrainingCourseDTO evaluatePresentation(String courseId, Integer satisfaction);

    TrainingCourseDTO setPublicPresentation(String courseId, Boolean isPublic);

    Map<String, Long> getSatisfactionStats();

    List<TrainingCourseDTO> findAllByCurrentUser();

    List<TrainingCourseDTO> findAllPublic();
}

