package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.domain.ManualBusinessPlan;
import com.readytoplanbe.myapp.repository.ManualBusinessPlanRepository;
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
import org.springframework.http.HttpStatus;
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

    private final ManualBusinessPlanRepository manualBusinessPlanRepository;

    public ManualBusinessPlanResource(ManualBusinessPlanRepository manualBusinessPlanRepository) {
        this.manualBusinessPlanRepository = manualBusinessPlanRepository;
    }

    /**
     * {@code POST  /manual-business-plans} : Create a new manualBusinessPlan.
     *
     * @param manualBusinessPlan the manualBusinessPlan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manualBusinessPlan, or with status {@code 400 (Bad Request)} if the manualBusinessPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manual-business-plans")
    public ResponseEntity<ManualBusinessPlan> createManualBusinessPlan(@Valid @RequestBody ManualBusinessPlan manualBusinessPlan)
        throws URISyntaxException {
        log.debug("REST request to save ManualBusinessPlan : {}", manualBusinessPlan);
        if (manualBusinessPlan.getId() != null) {
            throw new BadRequestAlertException("A new manualBusinessPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManualBusinessPlan result = manualBusinessPlanRepository.save(manualBusinessPlan);
        return ResponseEntity
            .created(new URI("/api/manual-business-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /manual-business-plans/:id} : Updates an existing manualBusinessPlan.
     *
     * @param id the id of the manualBusinessPlan to save.
     * @param manualBusinessPlan the manualBusinessPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualBusinessPlan,
     * or with status {@code 400 (Bad Request)} if the manualBusinessPlan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manualBusinessPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manual-business-plans/{id}")
    public ResponseEntity<ManualBusinessPlan> updateManualBusinessPlan(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ManualBusinessPlan manualBusinessPlan
    ) throws URISyntaxException {
        log.debug("REST request to update ManualBusinessPlan : {}, {}", id, manualBusinessPlan);
        if (manualBusinessPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualBusinessPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualBusinessPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ManualBusinessPlan result = manualBusinessPlanRepository.save(manualBusinessPlan);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualBusinessPlan.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /manual-business-plans/:id} : Partial updates given fields of an existing manualBusinessPlan, field will ignore if it is null
     *
     * @param id the id of the manualBusinessPlan to save.
     * @param manualBusinessPlan the manualBusinessPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manualBusinessPlan,
     * or with status {@code 400 (Bad Request)} if the manualBusinessPlan is not valid,
     * or with status {@code 404 (Not Found)} if the manualBusinessPlan is not found,
     * or with status {@code 500 (Internal Server Error)} if the manualBusinessPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/manual-business-plans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManualBusinessPlan> partialUpdateManualBusinessPlan(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ManualBusinessPlan manualBusinessPlan
    ) throws URISyntaxException {
        log.debug("REST request to partial update ManualBusinessPlan partially : {}, {}", id, manualBusinessPlan);
        if (manualBusinessPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manualBusinessPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manualBusinessPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManualBusinessPlan> result = manualBusinessPlanRepository
            .findById(manualBusinessPlan.getId())
            .map(existingManualBusinessPlan -> {
                if (manualBusinessPlan.getName() != null) {
                    existingManualBusinessPlan.setName(manualBusinessPlan.getName());
                }
                if (manualBusinessPlan.getDescription() != null) {
                    existingManualBusinessPlan.setDescription(manualBusinessPlan.getDescription());
                }
                if (manualBusinessPlan.getCreationDate() != null) {
                    existingManualBusinessPlan.setCreationDate(manualBusinessPlan.getCreationDate());
                }
                if (manualBusinessPlan.getEntrepreneurName() != null) {
                    existingManualBusinessPlan.setEntrepreneurName(manualBusinessPlan.getEntrepreneurName());
                }

                return existingManualBusinessPlan;
            })
            .map(manualBusinessPlanRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manualBusinessPlan.getId())
        );
    }

    /**
     * {@code GET  /manual-business-plans} : get all the manualBusinessPlans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manualBusinessPlans in body.
     */
    @GetMapping("/manual-business-plans")
    public ResponseEntity<List<ManualBusinessPlan>> getAllManualBusinessPlans(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ManualBusinessPlans");
        Page<ManualBusinessPlan> page = manualBusinessPlanRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /manual-business-plans/:id} : get the "id" manualBusinessPlan.
     *
     * @param id the id of the manualBusinessPlan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manualBusinessPlan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manual-business-plans/{id}")
    public ResponseEntity<ManualBusinessPlan> getManualBusinessPlan(@PathVariable String id) {
        log.debug("REST request to get ManualBusinessPlan : {}", id);
        Optional<ManualBusinessPlan> manualBusinessPlan = manualBusinessPlanRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(manualBusinessPlan);
    }

    /**
     * {@code DELETE  /manual-business-plans/:id} : delete the "id" manualBusinessPlan.
     *
     * @param id the id of the manualBusinessPlan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manual-business-plans/{id}")
    public ResponseEntity<Void> deleteManualBusinessPlan(@PathVariable String id) {
        log.debug("REST request to delete ManualBusinessPlan : {}", id);
        manualBusinessPlanRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
