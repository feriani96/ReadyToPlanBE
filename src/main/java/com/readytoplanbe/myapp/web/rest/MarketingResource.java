package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.repository.MarketingRepository;
import com.readytoplanbe.myapp.service.MarketingService;
import com.readytoplanbe.myapp.service.dto.MarketingDTO;
import com.readytoplanbe.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;

/**
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.Marketing}.
 */
@RestController
@RequestMapping("/api")
public class MarketingResource {

    private final Logger log = LoggerFactory.getLogger(MarketingResource.class);

    private static final String ENTITY_NAME = "marketing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarketingService marketingService;

    private final MarketingRepository marketingRepository;

    public MarketingResource(MarketingService marketingService, MarketingRepository marketingRepository) {
        this.marketingService = marketingService;
        this.marketingRepository = marketingRepository;
    }

    @PostMapping("/marketings")
    public ResponseEntity<MarketingDTO> createMarketing(
        @Valid @RequestBody MarketingDTO dto,
        @RequestParam String companyId) throws URISyntaxException {

        log.debug("REST request to save ProductOrService : {}", dto);
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new productOrService cannot already have an ID", ENTITY_NAME, "idexists");
        }

        MarketingDTO result = marketingService.save(dto, companyId);

        return ResponseEntity
            .created(new URI("/api/marketings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }
    @GetMapping("/marketings/by-company/{companyId}")
    public List<MarketingDTO> getMarketingsByCompany(@PathVariable String companyId) {
        log.debug("REST request to get all Marketings for Company : {}", companyId);
        return marketingService.findByCompanyId(companyId);
    }

    /**
     * {@code PUT  /marketings/:id} : Updates an existing marketing.
     *
     * @param id the id of the marketingDTO to save.
     * @param marketingDTO the marketingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketingDTO,
     * or with status {@code 400 (Bad Request)} if the marketingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marketingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marketings/{id}")
    public ResponseEntity<MarketingDTO> updateMarketing(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody MarketingDTO marketingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Marketing : {}, {}", id, marketingDTO);
        if (marketingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MarketingDTO result = marketingService.update(marketingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marketingDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /marketings/:id} : Partial updates given fields of an existing marketing, field will ignore if it is null
     *
     * @param id the id of the marketingDTO to save.
     * @param marketingDTO the marketingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marketingDTO,
     * or with status {@code 400 (Bad Request)} if the marketingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the marketingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the marketingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/marketings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MarketingDTO> partialUpdateMarketing(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody MarketingDTO marketingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Marketing partially : {}, {}", id, marketingDTO);
        if (marketingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marketingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marketingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarketingDTO> result = marketingService.partialUpdate(marketingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, marketingDTO.getId())
        );
    }

    /**
     * {@code GET  /marketings} : get all the marketings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marketings in body.
     */
    @GetMapping("/marketings")
    public List<MarketingDTO> getAllMarketings() {
        log.debug("REST request to get all Marketings");
        return marketingService.findAll();
    }

    /**
     * {@code GET  /marketings/:id} : get the "id" marketing.
     *
     * @param id the id of the marketingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marketingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marketings/{id}")
    public ResponseEntity<MarketingDTO> getMarketing(@PathVariable String id) {
        log.debug("REST request to get Marketing : {}", id);
        Optional<MarketingDTO> marketingDTO = marketingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(marketingDTO);
    }

    /**
     * {@code DELETE  /marketings/:id} : delete the "id" marketing.
     *
     * @param id the id of the marketingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marketings/{id}")
    public ResponseEntity<Void> deleteMarketing(@PathVariable String id) {
        log.debug("REST request to delete Marketing : {}", id);
        marketingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
