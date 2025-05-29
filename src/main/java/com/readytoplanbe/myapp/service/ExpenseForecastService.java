package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.service.dto.ExpenseForecastDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.readytoplanbe.myapp.domain.ExpenseForecast}.
 */
public interface ExpenseForecastService {
    /**
     * Save a expenseForecast.
     *
     * @param expenseForecastDTO the entity to save.
     * @return the persisted entity.
     */
    ExpenseForecastDTO save(ExpenseForecastDTO expenseForecastDTO);

    /**
     * Updates a expenseForecast.
     *
     * @param expenseForecastDTO the entity to update.
     * @return the persisted entity.
     */
    ExpenseForecastDTO update(ExpenseForecastDTO expenseForecastDTO);

    /**
     * Partially updates a expenseForecast.
     *
     * @param expenseForecastDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExpenseForecastDTO> partialUpdate(ExpenseForecastDTO expenseForecastDTO);

    /**
     * Get all the expenseForecasts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExpenseForecastDTO> findAll(Pageable pageable);

    /**
     * Get all the expenseForecasts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ExpenseForecastDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" expenseForecast.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExpenseForecastDTO> findOne(String id);

    /**
     * Delete the "id" expenseForecast.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
