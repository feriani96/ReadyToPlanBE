package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.domain.ExpenseForecast;
import com.readytoplanbe.myapp.repository.ExpenseForecastRepository;
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
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.ExpenseForecast}.
 */
@RestController
@RequestMapping("/api")
public class ExpenseForecastResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseForecastResource.class);

    private static final String ENTITY_NAME = "expenseForecast";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpenseForecastRepository expenseForecastRepository;

    public ExpenseForecastResource(ExpenseForecastRepository expenseForecastRepository) {
        this.expenseForecastRepository = expenseForecastRepository;
    }

    /**
     * {@code POST  /expense-forecasts} : Create a new expenseForecast.
     *
     * @param expenseForecast the expenseForecast to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expenseForecast, or with status {@code 400 (Bad Request)} if the expenseForecast has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expense-forecasts")
    public ResponseEntity<ExpenseForecast> createExpenseForecast(@Valid @RequestBody ExpenseForecast expenseForecast)
        throws URISyntaxException {
        log.debug("REST request to save ExpenseForecast : {}", expenseForecast);
        if (expenseForecast.getId() != null) {
            throw new BadRequestAlertException("A new expenseForecast cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpenseForecast result = expenseForecastRepository.save(expenseForecast);
        return ResponseEntity
            .created(new URI("/api/expense-forecasts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /expense-forecasts/:id} : Updates an existing expenseForecast.
     *
     * @param id the id of the expenseForecast to save.
     * @param expenseForecast the expenseForecast to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseForecast,
     * or with status {@code 400 (Bad Request)} if the expenseForecast is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expenseForecast couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expense-forecasts/{id}")
    public ResponseEntity<ExpenseForecast> updateExpenseForecast(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ExpenseForecast expenseForecast
    ) throws URISyntaxException {
        log.debug("REST request to update ExpenseForecast : {}, {}", id, expenseForecast);
        if (expenseForecast.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseForecast.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExpenseForecast result = expenseForecastRepository.save(expenseForecast);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expenseForecast.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /expense-forecasts/:id} : Partial updates given fields of an existing expenseForecast, field will ignore if it is null
     *
     * @param id the id of the expenseForecast to save.
     * @param expenseForecast the expenseForecast to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expenseForecast,
     * or with status {@code 400 (Bad Request)} if the expenseForecast is not valid,
     * or with status {@code 404 (Not Found)} if the expenseForecast is not found,
     * or with status {@code 500 (Internal Server Error)} if the expenseForecast couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/expense-forecasts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpenseForecast> partialUpdateExpenseForecast(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ExpenseForecast expenseForecast
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExpenseForecast partially : {}, {}", id, expenseForecast);
        if (expenseForecast.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expenseForecast.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expenseForecastRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpenseForecast> result = expenseForecastRepository
            .findById(expenseForecast.getId())
            .map(existingExpenseForecast -> {
                if (expenseForecast.getLabel() != null) {
                    existingExpenseForecast.setLabel(expenseForecast.getLabel());
                }
                if (expenseForecast.getMonthlyAmount() != null) {
                    existingExpenseForecast.setMonthlyAmount(expenseForecast.getMonthlyAmount());
                }

                return existingExpenseForecast;
            })
            .map(expenseForecastRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expenseForecast.getId())
        );
    }

    /**
     * {@code GET  /expense-forecasts} : get all the expenseForecasts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expenseForecasts in body.
     */
    @GetMapping("/expense-forecasts")
    public ResponseEntity<List<ExpenseForecast>> getAllExpenseForecasts(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of ExpenseForecasts");
        Page<ExpenseForecast> page;
        if (eagerload) {
            page = expenseForecastRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = expenseForecastRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expense-forecasts/:id} : get the "id" expenseForecast.
     *
     * @param id the id of the expenseForecast to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expenseForecast, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expense-forecasts/{id}")
    public ResponseEntity<ExpenseForecast> getExpenseForecast(@PathVariable String id) {
        log.debug("REST request to get ExpenseForecast : {}", id);
        Optional<ExpenseForecast> expenseForecast = expenseForecastRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(expenseForecast);
    }

    /**
     * {@code DELETE  /expense-forecasts/:id} : delete the "id" expenseForecast.
     *
     * @param id the id of the expenseForecast to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expense-forecasts/{id}")
    public ResponseEntity<Void> deleteExpenseForecast(@PathVariable String id) {
        log.debug("REST request to delete ExpenseForecast : {}", id);
        expenseForecastRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
