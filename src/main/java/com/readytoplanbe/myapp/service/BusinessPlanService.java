package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.domain.Slide;
import com.readytoplanbe.myapp.domain.enumeration.ExportFormat;
import com.readytoplanbe.myapp.service.dto.BusinessPlanDTO;

import java.io.IOException;
import java.util.Optional;

import com.readytoplanbe.myapp.service.dto.BusinessPlanInputDTO;
import com.readytoplanbe.myapp.service.dto.BusinessPlanWithImageDTO;
import io.undertow.server.handlers.resource.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;

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

    /**
     * Generate a business plan presentation based on user input.
     *
     * @param input the input data for the business plan.
     * @return the generated presentation as a string.
     */
    String generatePresentation(BusinessPlanInputDTO input);

    /**
     * Update the generated presentation of a business plan at a specific slide index.
     *
     * @param companyName the id of the business plan.
     * @param slideIndex the index of the slide to update.
     * @param newContent the new JSON content for the slide.
     * @return the updated BusinessPlanDTO.
     */
    Optional<BusinessPlanDTO> updateGeneratedPresentation(String companyName, int slideIndex, String newContent);

    String getPresentation(String companyName);

    List<Slide> getPresentationSlides(String companyName);

    org.springframework.core.io.Resource exportPresentation(String companyName, ExportFormat format) throws IOException;
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
