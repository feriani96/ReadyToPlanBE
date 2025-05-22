package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.domain.Enterprise;
import com.readytoplanbe.myapp.repository.EnterpriseRepository;
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
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.Enterprise}.
 */
@RestController
@RequestMapping("/api")
public class EnterpriseResource {

    private final Logger log = LoggerFactory.getLogger(EnterpriseResource.class);

    private static final String ENTITY_NAME = "enterprise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseResource(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    /**
     * {@code POST  /enterprises} : Create a new enterprise.
     *
     * @param enterprise the enterprise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enterprise, or with status {@code 400 (Bad Request)} if the enterprise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enterprises")
    public ResponseEntity<Enterprise> createEnterprise(@Valid @RequestBody Enterprise enterprise) throws URISyntaxException {
        log.debug("REST request to save Enterprise : {}", enterprise);
        if (enterprise.getId() != null) {
            throw new BadRequestAlertException("A new enterprise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Enterprise result = enterpriseRepository.save(enterprise);
        return ResponseEntity
            .created(new URI("/api/enterprises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /enterprises/:id} : Updates an existing enterprise.
     *
     * @param id the id of the enterprise to save.
     * @param enterprise the enterprise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enterprise,
     * or with status {@code 400 (Bad Request)} if the enterprise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enterprise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enterprises/{id}")
    public ResponseEntity<Enterprise> updateEnterprise(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Enterprise enterprise
    ) throws URISyntaxException {
        log.debug("REST request to update Enterprise : {}, {}", id, enterprise);
        if (enterprise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enterprise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enterpriseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Enterprise result = enterpriseRepository.save(enterprise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enterprise.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /enterprises/:id} : Partial updates given fields of an existing enterprise, field will ignore if it is null
     *
     * @param id the id of the enterprise to save.
     * @param enterprise the enterprise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enterprise,
     * or with status {@code 400 (Bad Request)} if the enterprise is not valid,
     * or with status {@code 404 (Not Found)} if the enterprise is not found,
     * or with status {@code 500 (Internal Server Error)} if the enterprise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/enterprises/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Enterprise> partialUpdateEnterprise(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Enterprise enterprise
    ) throws URISyntaxException {
        log.debug("REST request to partial update Enterprise partially : {}, {}", id, enterprise);
        if (enterprise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enterprise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enterpriseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Enterprise> result = enterpriseRepository
            .findById(enterprise.getId())
            .map(existingEnterprise -> {
                if (enterprise.getEnterpriseName() != null) {
                    existingEnterprise.setEnterpriseName(enterprise.getEnterpriseName());
                }
                if (enterprise.getCountry() != null) {
                    existingEnterprise.setCountry(enterprise.getCountry());
                }
                if (enterprise.getPhoneNumber() != null) {
                    existingEnterprise.setPhoneNumber(enterprise.getPhoneNumber());
                }
                if (enterprise.getDescription() != null) {
                    existingEnterprise.setDescription(enterprise.getDescription());
                }
                if (enterprise.getAmount() != null) {
                    existingEnterprise.setAmount(enterprise.getAmount());
                }
                if (enterprise.getCurrency() != null) {
                    existingEnterprise.setCurrency(enterprise.getCurrency());
                }

                return existingEnterprise;
            })
            .map(enterpriseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enterprise.getId())
        );
    }

    /**
     * {@code GET  /enterprises} : get all the enterprises.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enterprises in body.
     */
    @GetMapping("/enterprises")
    public ResponseEntity<List<Enterprise>> getAllEnterprises(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Enterprises");
        Page<Enterprise> page = enterpriseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /enterprises/:id} : get the "id" enterprise.
     *
     * @param id the id of the enterprise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enterprise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enterprises/{id}")
    public ResponseEntity<Enterprise> getEnterprise(@PathVariable String id) {
        log.debug("REST request to get Enterprise : {}", id);
        Optional<Enterprise> enterprise = enterpriseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(enterprise);
    }

    /**
     * {@code DELETE  /enterprises/:id} : delete the "id" enterprise.
     *
     * @param id the id of the enterprise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enterprises/{id}")
    public ResponseEntity<Void> deleteEnterprise(@PathVariable String id) {
        log.debug("REST request to delete Enterprise : {}", id);
        enterpriseRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
