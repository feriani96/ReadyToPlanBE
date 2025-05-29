package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.service.dto.FinancialForecastDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.readytoplanbe.myapp.domain.FinancialForecast}.
 */
public interface FinancialForecastService {
    /**
     * Save a financialForecast.
     *
     * @param financialForecastDTO the entity to save.
     * @return the persisted entity.
     */
    FinancialForecastDTO save(FinancialForecastDTO financialForecastDTO);

    /**
     * Updates a financialForecast.
     *
     * @param financialForecastDTO the entity to update.
     * @return the persisted entity.
     */
    FinancialForecastDTO update(FinancialForecastDTO financialForecastDTO);

    /**
     * Partially updates a financialForecast.
     *
     * @param financialForecastDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FinancialForecastDTO> partialUpdate(FinancialForecastDTO financialForecastDTO);

    /**
     * Get all the financialForecasts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FinancialForecastDTO> findAll(Pageable pageable);

    /**
     * Get all the financialForecasts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FinancialForecastDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" financialForecast.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FinancialForecastDTO> findOne(String id);

    /**
     * Delete the "id" financialForecast.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
