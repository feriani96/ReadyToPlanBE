package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.service.dto.ProductOrServiceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.readytoplanbe.myapp.domain.ProductOrService}.
 */
public interface ProductOrServiceService {
    /**
     * Save a productOrService.
     *
     * @param productOrServiceDTO the entity to save.
     * @return the persisted entity.
     */
    ProductOrServiceDTO save(ProductOrServiceDTO productOrServiceDTO);

    /**
     * Updates a productOrService.
     *
     * @param productOrServiceDTO the entity to update.
     * @return the persisted entity.
     */
    ProductOrServiceDTO update(ProductOrServiceDTO productOrServiceDTO);

    /**
     * Partially updates a productOrService.
     *
     * @param productOrServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductOrServiceDTO> partialUpdate(ProductOrServiceDTO productOrServiceDTO);

    /**
     * Get all the productOrServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductOrServiceDTO> findAll(Pageable pageable);

    /**
     * Get all the productOrServices with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductOrServiceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" productOrService.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductOrServiceDTO> findOne(String id);

    /**
     * Delete the "id" productOrService.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
