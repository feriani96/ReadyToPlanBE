package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.repository.RevenueForecastRepository;
import com.readytoplanbe.myapp.service.RevenueForecastService;
import com.readytoplanbe.myapp.service.dto.RevenueForecastDTO;
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

    private final RevenueForecastService revenueForecastService;

    private final RevenueForecastRepository revenueForecastRepository;

    public RevenueForecastResource(RevenueForecastService revenueForecastService, RevenueForecastRepository revenueForecastRepository) {
        this.revenueForecastService = revenueForecastService;
        this.revenueForecastRepository = revenueForecastRepository;
    }

    /**
     * {@code POST  /revenue-forecasts} : Create a new revenueForecast.
     *
     * @param revenueForecastDTO the revenueForecastDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new revenueForecastDTO, or with status {@code 400 (Bad Request)} if the revenueForecast has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/revenue-forecasts")
    public ResponseEntity<RevenueForecastDTO> createRevenueForecast(@Valid @RequestBody RevenueForecastDTO revenueForecastDTO)
        throws URISyntaxException {
        log.debug("REST request to save RevenueForecast : {}", revenueForecastDTO);
        if (revenueForecastDTO.getId() != null) {
            throw new BadRequestAlertException("A new revenueForecast cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RevenueForecastDTO result = revenueForecastService.save(revenueForecastDTO);
        return ResponseEntity
            .created(new URI("/api/revenue-forecasts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /revenue-forecasts/:id} : Updates an existing revenueForecast.
     *
     * @param id the id of the revenueForecastDTO to save.
     * @param revenueForecastDTO the revenueForecastDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revenueForecastDTO,
     * or with status {@code 400 (Bad Request)} if the revenueForecastDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the revenueForecastDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/revenue-forecasts/{id}")
    public ResponseEntity<RevenueForecastDTO> updateRevenueForecast(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody RevenueForecastDTO revenueForecastDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RevenueForecast : {}, {}", id, revenueForecastDTO);
        if (revenueForecastDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, revenueForecastDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!revenueForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RevenueForecastDTO result = revenueForecastService.update(revenueForecastDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, revenueForecastDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /revenue-forecasts/:id} : Partial updates given fields of an existing revenueForecast, field will ignore if it is null
     *
     * @param id the id of the revenueForecastDTO to save.
     * @param revenueForecastDTO the revenueForecastDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revenueForecastDTO,
     * or with status {@code 400 (Bad Request)} if the revenueForecastDTO is not valid,
     * or with status {@code 404 (Not Found)} if the revenueForecastDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the revenueForecastDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/revenue-forecasts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RevenueForecastDTO> partialUpdateRevenueForecast(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody RevenueForecastDTO revenueForecastDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RevenueForecast partially : {}, {}", id, revenueForecastDTO);
        if (revenueForecastDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, revenueForecastDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!revenueForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RevenueForecastDTO> result = revenueForecastService.partialUpdate(revenueForecastDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, revenueForecastDTO.getId())
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
    public ResponseEntity<List<RevenueForecastDTO>> getAllRevenueForecasts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of RevenueForecasts");
        Page<RevenueForecastDTO> page;
        if (eagerload) {
            page = revenueForecastService.findAllWithEagerRelationships(pageable);
        } else {
            page = revenueForecastService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /revenue-forecasts/:id} : get the "id" revenueForecast.
     *
     * @param id the id of the revenueForecastDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the revenueForecastDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/revenue-forecasts/{id}")
    public ResponseEntity<RevenueForecastDTO> getRevenueForecast(@PathVariable String id) {
        log.debug("REST request to get RevenueForecast : {}", id);
        Optional<RevenueForecastDTO> revenueForecastDTO = revenueForecastService.findOne(id);
        return ResponseUtil.wrapOrNotFound(revenueForecastDTO);
    }

    /**
     * {@code DELETE  /revenue-forecasts/:id} : delete the "id" revenueForecast.
     *
     * @param id the id of the revenueForecastDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/revenue-forecasts/{id}")
    public ResponseEntity<Void> deleteRevenueForecast(@PathVariable String id) {
        log.debug("REST request to delete RevenueForecast : {}", id);
        revenueForecastService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
