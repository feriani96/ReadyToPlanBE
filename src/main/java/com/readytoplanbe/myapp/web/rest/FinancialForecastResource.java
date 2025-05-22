package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.domain.FinancialForecast;
import com.readytoplanbe.myapp.repository.FinancialForecastRepository;
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
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.FinancialForecast}.
 */
@RestController
@RequestMapping("/api")
public class FinancialForecastResource {

    private final Logger log = LoggerFactory.getLogger(FinancialForecastResource.class);

    private static final String ENTITY_NAME = "financialForecast";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinancialForecastRepository financialForecastRepository;

    public FinancialForecastResource(FinancialForecastRepository financialForecastRepository) {
        this.financialForecastRepository = financialForecastRepository;
    }

    /**
     * {@code POST  /financial-forecasts} : Create a new financialForecast.
     *
     * @param financialForecast the financialForecast to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financialForecast, or with status {@code 400 (Bad Request)} if the financialForecast has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/financial-forecasts")
    public ResponseEntity<FinancialForecast> createFinancialForecast(@Valid @RequestBody FinancialForecast financialForecast)
        throws URISyntaxException {
        log.debug("REST request to save FinancialForecast : {}", financialForecast);
        if (financialForecast.getId() != null) {
            throw new BadRequestAlertException("A new financialForecast cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinancialForecast result = financialForecastRepository.save(financialForecast);
        return ResponseEntity
            .created(new URI("/api/financial-forecasts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /financial-forecasts/:id} : Updates an existing financialForecast.
     *
     * @param id the id of the financialForecast to save.
     * @param financialForecast the financialForecast to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialForecast,
     * or with status {@code 400 (Bad Request)} if the financialForecast is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financialForecast couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/financial-forecasts/{id}")
    public ResponseEntity<FinancialForecast> updateFinancialForecast(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FinancialForecast financialForecast
    ) throws URISyntaxException {
        log.debug("REST request to update FinancialForecast : {}, {}", id, financialForecast);
        if (financialForecast.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialForecast.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FinancialForecast result = financialForecastRepository.save(financialForecast);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialForecast.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /financial-forecasts/:id} : Partial updates given fields of an existing financialForecast, field will ignore if it is null
     *
     * @param id the id of the financialForecast to save.
     * @param financialForecast the financialForecast to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialForecast,
     * or with status {@code 400 (Bad Request)} if the financialForecast is not valid,
     * or with status {@code 404 (Not Found)} if the financialForecast is not found,
     * or with status {@code 500 (Internal Server Error)} if the financialForecast couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/financial-forecasts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinancialForecast> partialUpdateFinancialForecast(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FinancialForecast financialForecast
    ) throws URISyntaxException {
        log.debug("REST request to partial update FinancialForecast partially : {}, {}", id, financialForecast);
        if (financialForecast.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialForecast.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinancialForecast> result = financialForecastRepository
            .findById(financialForecast.getId())
            .map(existingFinancialForecast -> {
                if (financialForecast.getStartDate() != null) {
                    existingFinancialForecast.setStartDate(financialForecast.getStartDate());
                }
                if (financialForecast.getDurationInMonths() != null) {
                    existingFinancialForecast.setDurationInMonths(financialForecast.getDurationInMonths());
                }

                return existingFinancialForecast;
            })
            .map(financialForecastRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialForecast.getId())
        );
    }

    /**
     * {@code GET  /financial-forecasts} : get all the financialForecasts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financialForecasts in body.
     */
    @GetMapping("/financial-forecasts")
    public ResponseEntity<List<FinancialForecast>> getAllFinancialForecasts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of FinancialForecasts");
        Page<FinancialForecast> page;
        if (eagerload) {
            page = financialForecastRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = financialForecastRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /financial-forecasts/:id} : get the "id" financialForecast.
     *
     * @param id the id of the financialForecast to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financialForecast, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/financial-forecasts/{id}")
    public ResponseEntity<FinancialForecast> getFinancialForecast(@PathVariable String id) {
        log.debug("REST request to get FinancialForecast : {}", id);
        Optional<FinancialForecast> financialForecast = financialForecastRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(financialForecast);
    }

    /**
     * {@code DELETE  /financial-forecasts/:id} : delete the "id" financialForecast.
     *
     * @param id the id of the financialForecast to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/financial-forecasts/{id}")
    public ResponseEntity<Void> deleteFinancialForecast(@PathVariable String id) {
        log.debug("REST request to delete FinancialForecast : {}", id);
        financialForecastRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
