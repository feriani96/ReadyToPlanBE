package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.service.dto.RevenueForecastDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.readytoplanbe.myapp.domain.RevenueForecast}.
 */
public interface RevenueForecastService {
    /**
     * Save a revenueForecast.
     *
     * @param revenueForecastDTO the entity to save.
     * @return the persisted entity.
     */
    RevenueForecastDTO save(RevenueForecastDTO revenueForecastDTO);

    /**
     * Updates a revenueForecast.
     *
     * @param revenueForecastDTO the entity to update.
     * @return the persisted entity.
     */
    RevenueForecastDTO update(RevenueForecastDTO revenueForecastDTO);

    /**
     * Partially updates a revenueForecast.
     *
     * @param revenueForecastDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RevenueForecastDTO> partialUpdate(RevenueForecastDTO revenueForecastDTO);

    /**
     * Get all the revenueForecasts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RevenueForecastDTO> findAll(Pageable pageable);

    /**
     * Get all the revenueForecasts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RevenueForecastDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" revenueForecast.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RevenueForecastDTO> findOne(String id);

    /**
     * Delete the "id" revenueForecast.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
