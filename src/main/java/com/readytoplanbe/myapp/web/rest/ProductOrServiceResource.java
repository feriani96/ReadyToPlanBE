package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.domain.ProductOrService;
import com.readytoplanbe.myapp.repository.ProductOrServiceRepository;
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
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.ProductOrService}.
 */
@RestController
@RequestMapping("/api")
public class ProductOrServiceResource {

    private final Logger log = LoggerFactory.getLogger(ProductOrServiceResource.class);

    private static final String ENTITY_NAME = "productOrService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductOrServiceRepository productOrServiceRepository;

    public ProductOrServiceResource(ProductOrServiceRepository productOrServiceRepository) {
        this.productOrServiceRepository = productOrServiceRepository;
    }

    /**
     * {@code POST  /product-or-services} : Create a new productOrService.
     *
     * @param productOrService the productOrService to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productOrService, or with status {@code 400 (Bad Request)} if the productOrService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-or-services")
    public ResponseEntity<ProductOrService> createProductOrService(@Valid @RequestBody ProductOrService productOrService)
        throws URISyntaxException {
        log.debug("REST request to save ProductOrService : {}", productOrService);
        if (productOrService.getId() != null) {
            throw new BadRequestAlertException("A new productOrService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductOrService result = productOrServiceRepository.save(productOrService);
        return ResponseEntity
            .created(new URI("/api/product-or-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /product-or-services/:id} : Updates an existing productOrService.
     *
     * @param id the id of the productOrService to save.
     * @param productOrService the productOrService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOrService,
     * or with status {@code 400 (Bad Request)} if the productOrService is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productOrService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-or-services/{id}")
    public ResponseEntity<ProductOrService> updateProductOrService(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ProductOrService productOrService
    ) throws URISyntaxException {
        log.debug("REST request to update ProductOrService : {}, {}", id, productOrService);
        if (productOrService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOrService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOrServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductOrService result = productOrServiceRepository.save(productOrService);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productOrService.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-or-services/:id} : Partial updates given fields of an existing productOrService, field will ignore if it is null
     *
     * @param id the id of the productOrService to save.
     * @param productOrService the productOrService to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOrService,
     * or with status {@code 400 (Bad Request)} if the productOrService is not valid,
     * or with status {@code 404 (Not Found)} if the productOrService is not found,
     * or with status {@code 500 (Internal Server Error)} if the productOrService couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-or-services/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductOrService> partialUpdateProductOrService(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ProductOrService productOrService
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductOrService partially : {}, {}", id, productOrService);
        if (productOrService.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productOrService.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productOrServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductOrService> result = productOrServiceRepository
            .findById(productOrService.getId())
            .map(existingProductOrService -> {
                if (productOrService.getNameProductOrService() != null) {
                    existingProductOrService.setNameProductOrService(productOrService.getNameProductOrService());
                }
                if (productOrService.getProductDescription() != null) {
                    existingProductOrService.setProductDescription(productOrService.getProductDescription());
                }
                if (productOrService.getUnitPrice() != null) {
                    existingProductOrService.setUnitPrice(productOrService.getUnitPrice());
                }
                if (productOrService.getEstimatedMonthlySales() != null) {
                    existingProductOrService.setEstimatedMonthlySales(productOrService.getEstimatedMonthlySales());
                }
                if (productOrService.getDurationInMonths() != null) {
                    existingProductOrService.setDurationInMonths(productOrService.getDurationInMonths());
                }

                return existingProductOrService;
            })
            .map(productOrServiceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productOrService.getId())
        );
    }

    /**
     * {@code GET  /product-or-services} : get all the productOrServices.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productOrServices in body.
     */
    @GetMapping("/product-or-services")
    public ResponseEntity<List<ProductOrService>> getAllProductOrServices(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of ProductOrServices");
        Page<ProductOrService> page;
        if (eagerload) {
            page = productOrServiceRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = productOrServiceRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-or-services/:id} : get the "id" productOrService.
     *
     * @param id the id of the productOrService to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productOrService, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-or-services/{id}")
    public ResponseEntity<ProductOrService> getProductOrService(@PathVariable String id) {
        log.debug("REST request to get ProductOrService : {}", id);
        Optional<ProductOrService> productOrService = productOrServiceRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(productOrService);
    }

    /**
     * {@code DELETE  /product-or-services/:id} : delete the "id" productOrService.
     *
     * @param id the id of the productOrService to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-or-services/{id}")
    public ResponseEntity<Void> deleteProductOrService(@PathVariable String id) {
        log.debug("REST request to delete ProductOrService : {}", id);
        productOrServiceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
