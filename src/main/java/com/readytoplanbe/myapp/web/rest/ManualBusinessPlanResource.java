package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.repository.ManualBusinessPlanRepository;
import com.readytoplanbe.myapp.service.ManualBusinessPlanService;
import com.readytoplanbe.myapp.service.dto.ManualBusinessPlanDTO;
import com.readytoplanbe.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.ManualBusinessPlan}.
 */
@RestController
@RequestMapping("/api")
public class ManualBusinessPlanResource {

    private final Logger log = LoggerFactory.getLogger(ManualBusinessPlanResource.class);

    private static final String ENTITY_NAME = "manualBusinessPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManualBusinessPlanService manualBusinessPlanService;

    private final ManualBusinessPlanRepository manualBusinessPlanRepository;

    public ManualBusinessPlanResource(
        ManualBusinessPlanService manualBusinessPlanService,
        ManualBusinessPlanRepository manualBusinessPlanRepository
    ) {
        this.manualBusinessPlanService = manualBusinessPlanService;
        this.manualBusinessPlanRepository = manualBusinessPlanRepository;
    }

    /**
     * {@code POST  /manual-business-plans} : Create a new manualBusinessPlan.
     *
     * @param manualBusinessPlanDTO the manualBusinessPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manualBusinessPlanDTO, or with status {@code 400 (Bad Request)} if the manualBusinessPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manual-business-plans")
    public ResponseEntity<ManualBusinessPlanDTO> createManualBusinessPlan(@Valid @RequestBody ManualBusinessPlanDTO manualBusinessPlanDTO)
        throws URISyntaxException {
        log.debug("REST request to save ManualBusinessPlan : {}", manualBusinessPlanDTO);
        if (manualBusinessPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new manualBusinessPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManualBusinessPlanDTO result = manualBusinessPlanService.save(manualBusinessPlanDTO);
        return ResponseEntity
            .created(new URI("/api/manual-business-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /manual-business-plans/:id} : Updates an existing manualBusinessPlan.
     *
     * @param id the id of the manualBusinessPlanDTO to save.
     * @param manualBusinessPlanDTO the manualBusinessPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualBusinessPlanDTO,
     * or with status {@code 400 (Bad Request)} if the manualBusinessPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manualBusinessPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manual-business-plans/{id}")
    public ResponseEntity<ManualBusinessPlanDTO> updateManualBusinessPlan(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ManualBusinessPlanDTO manualBusinessPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ManualBusinessPlan : {}, {}", id, manualBusinessPlanDTO);
        if (manualBusinessPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualBusinessPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualBusinessPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ManualBusinessPlanDTO result = manualBusinessPlanService.update(manualBusinessPlanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualBusinessPlanDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /manual-business-plans/:id} : Partial updates given fields of an existing manualBusinessPlan, field will ignore if it is null
     *
     * @param id the id of the manualBusinessPlanDTO to save.
     * @param manualBusinessPlanDTO the manualBusinessPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualBusinessPlanDTO,
     * or with status {@code 400 (Bad Request)} if the manualBusinessPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the manualBusinessPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the manualBusinessPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/manual-business-plans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManualBusinessPlanDTO> partialUpdateManualBusinessPlan(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ManualBusinessPlanDTO manualBusinessPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ManualBusinessPlan partially : {}, {}", id, manualBusinessPlanDTO);
        if (manualBusinessPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualBusinessPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualBusinessPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManualBusinessPlanDTO> result = manualBusinessPlanService.partialUpdate(manualBusinessPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualBusinessPlanDTO.getId())
        );
    }

    /**
     * {@code GET  /manual-business-plans} : get all the manualBusinessPlans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manualBusinessPlans in body.
     */
    @GetMapping("/manual-business-plans")
    public ResponseEntity<List<ManualBusinessPlanDTO>> getAllManualBusinessPlans(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ManualBusinessPlans");
        Page<ManualBusinessPlanDTO> page = manualBusinessPlanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manual-business-plans/:id} : get the "id" manualBusinessPlan.
     *
     * @param id the id of the manualBusinessPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manualBusinessPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manual-business-plans/{id}")
    public ResponseEntity<ManualBusinessPlanDTO> getManualBusinessPlan(@PathVariable String id) {
        log.debug("REST request to get ManualBusinessPlan : {}", id);
        Optional<ManualBusinessPlanDTO> manualBusinessPlanDTO = manualBusinessPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(manualBusinessPlanDTO);
    }

    /**
     * {@code DELETE  /manual-business-plans/:id} : delete the "id" manualBusinessPlan.
     *
     * @param id the id of the manualBusinessPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manual-business-plans/{id}")
    public ResponseEntity<Void> deleteManualBusinessPlan(@PathVariable String id) {
        log.debug("REST request to delete ManualBusinessPlan : {}", id);
        manualBusinessPlanService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
