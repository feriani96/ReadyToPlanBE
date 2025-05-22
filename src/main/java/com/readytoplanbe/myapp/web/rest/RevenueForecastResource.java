package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.domain.RevenueForecast;
import com.readytoplanbe.myapp.repository.RevenueForecastRepository;
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
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.RevenueForecast}.
 */
@RestController
@RequestMapping("/api")
public class RevenueForecastResource {

    private final Logger log = LoggerFactory.getLogger(RevenueForecastResource.class);

    private static final String ENTITY_NAME = "revenueForecast";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RevenueForecastRepository revenueForecastRepository;

    public RevenueForecastResource(RevenueForecastRepository revenueForecastRepository) {
        this.revenueForecastRepository = revenueForecastRepository;
    }

    /**
     * {@code POST  /revenue-forecasts} : Create a new revenueForecast.
     *
     * @param revenueForecast the revenueForecast to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new revenueForecast, or with status {@code 400 (Bad Request)} if the revenueForecast has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/revenue-forecasts")
    public ResponseEntity<RevenueForecast> createRevenueForecast(@Valid @RequestBody RevenueForecast revenueForecast)
        throws URISyntaxException {
        log.debug("REST request to save RevenueForecast : {}", revenueForecast);
        if (revenueForecast.getId() != null) {
            throw new BadRequestAlertException("A new revenueForecast cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RevenueForecast result = revenueForecastRepository.save(revenueForecast);
        return ResponseEntity
            .created(new URI("/api/revenue-forecasts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /revenue-forecasts/:id} : Updates an existing revenueForecast.
     *
     * @param id the id of the revenueForecast to save.
     * @param revenueForecast the revenueForecast to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revenueForecast,
     * or with status {@code 400 (Bad Request)} if the revenueForecast is not valid,
     * or with status {@code 500 (Internal Server Error)} if the revenueForecast couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/revenue-forecasts/{id}")
    public ResponseEntity<RevenueForecast> updateRevenueForecast(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody RevenueForecast revenueForecast
    ) throws URISyntaxException {
        log.debug("REST request to update RevenueForecast : {}, {}", id, revenueForecast);
        if (revenueForecast.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, revenueForecast.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!revenueForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RevenueForecast result = revenueForecastRepository.save(revenueForecast);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, revenueForecast.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /revenue-forecasts/:id} : Partial updates given fields of an existing revenueForecast, field will ignore if it is null
     *
     * @param id the id of the revenueForecast to save.
     * @param revenueForecast the revenueForecast to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revenueForecast,
     * or with status {@code 400 (Bad Request)} if the revenueForecast is not valid,
     * or with status {@code 404 (Not Found)} if the revenueForecast is not found,
     * or with status {@code 500 (Internal Server Error)} if the revenueForecast couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/revenue-forecasts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RevenueForecast> partialUpdateRevenueForecast(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody RevenueForecast revenueForecast
    ) throws URISyntaxException {
        log.debug("REST request to partial update RevenueForecast partially : {}, {}", id, revenueForecast);
        if (revenueForecast.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, revenueForecast.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!revenueForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RevenueForecast> result = revenueForecastRepository
            .findById(revenueForecast.getId())
            .map(existingRevenueForecast -> {
                if (revenueForecast.getMonth() != null) {
                    existingRevenueForecast.setMonth(revenueForecast.getMonth());
                }
                if (revenueForecast.getYear() != null) {
                    existingRevenueForecast.setYear(revenueForecast.getYear());
                }
                if (revenueForecast.getUnitsSold() != null) {
                    existingRevenueForecast.setUnitsSold(revenueForecast.getUnitsSold());
                }
                if (revenueForecast.getTotalRevenue() != null) {
                    existingRevenueForecast.setTotalRevenue(revenueForecast.getTotalRevenue());
                }

                return existingRevenueForecast;
            })
            .map(revenueForecastRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, revenueForecast.getId())
        );
    }

    /**
     * {@code GET  /revenue-forecasts} : get all the revenueForecasts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of revenueForecasts in body.
     */
    @GetMapping("/revenue-forecasts")
    public ResponseEntity<List<RevenueForecast>> getAllRevenueForecasts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of RevenueForecasts");
        Page<RevenueForecast> page;
        if (eagerload) {
            page = revenueForecastRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = revenueForecastRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /revenue-forecasts/:id} : get the "id" revenueForecast.
     *
     * @param id the id of the revenueForecast to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the revenueForecast, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/revenue-forecasts/{id}")
    public ResponseEntity<RevenueForecast> getRevenueForecast(@PathVariable String id) {
        log.debug("REST request to get RevenueForecast : {}", id);
        Optional<RevenueForecast> revenueForecast = revenueForecastRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(revenueForecast);
    }

    /**
     * {@code DELETE  /revenue-forecasts/:id} : delete the "id" revenueForecast.
     *
     * @param id the id of the revenueForecast to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/revenue-forecasts/{id}")
    public ResponseEntity<Void> deleteRevenueForecast(@PathVariable String id) {
        log.debug("REST request to delete RevenueForecast : {}", id);
        revenueForecastRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
