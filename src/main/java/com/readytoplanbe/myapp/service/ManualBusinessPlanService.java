package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.service.dto.ManualBusinessPlanDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.readytoplanbe.myapp.domain.ManualBusinessPlan}.
 */
public interface ManualBusinessPlanService {
    /**
     * Save a manualBusinessPlan.
     *
     * @param manualBusinessPlanDTO the entity to save.
     * @return the persisted entity.
     */
    ManualBusinessPlanDTO save(ManualBusinessPlanDTO manualBusinessPlanDTO);

    /**
     * Updates a manualBusinessPlan.
     *
     * @param manualBusinessPlanDTO the entity to update.
     * @return the persisted entity.
     */
    ManualBusinessPlanDTO update(ManualBusinessPlanDTO manualBusinessPlanDTO);

    /**
     * Partially updates a manualBusinessPlan.
     *
     * @param manualBusinessPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ManualBusinessPlanDTO> partialUpdate(ManualBusinessPlanDTO manualBusinessPlanDTO);

    /**
     * Get all the manualBusinessPlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ManualBusinessPlanDTO> findAll(Pageable pageable);

    /**
     * Get the "id" manualBusinessPlan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManualBusinessPlanDTO> findOne(String id);

    /**
     * Delete the "id" manualBusinessPlan.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
