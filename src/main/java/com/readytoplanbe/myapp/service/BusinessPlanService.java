package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.service.dto.BusinessPlanDTO;
import java.util.Optional;

import com.readytoplanbe.myapp.service.dto.BusinessPlanInputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.readytoplanbe.myapp.domain.BusinessPlan}.
 */
public interface BusinessPlanService {
    /**
     * Save a businessPlan.
     *
     * @param businessPlanDTO the entity to save.
     * @return the persisted entity.
     */
    BusinessPlanDTO save(BusinessPlanDTO businessPlanDTO);

    String generatePresentation(BusinessPlanInputDTO input);

    /**
     * Updates a businessPlan.
     *
     * @param businessPlanDTO the entity to update.
     * @return the persisted entity.
     */
    BusinessPlanDTO update(BusinessPlanDTO businessPlanDTO);

    /**
     * Partially updates a businessPlan.
     *
     * @param businessPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusinessPlanDTO> partialUpdate(BusinessPlanDTO businessPlanDTO);

    /**
     * Get all the businessPlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BusinessPlanDTO> findAll(Pageable pageable);

    /**
     * Get the "id" businessPlan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusinessPlanDTO> findOne(String id);

    /**
     * Delete the "id" businessPlan.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
