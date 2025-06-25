package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.domain.Slide;
import com.readytoplanbe.myapp.repository.BusinessPlanRepository;
import com.readytoplanbe.myapp.service.BusinessPlanService;
import com.readytoplanbe.myapp.service.dto.BusinessPlanDTO;
import com.readytoplanbe.myapp.service.dto.BusinessPlanInputDTO;
import com.readytoplanbe.myapp.service.dto.BusinessPlanWithImageDTO;
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

import com.readytoplanbe.myapp.web.rest.vm.SlideUpdateRequest;


/**
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.BusinessPlan}.
 */
@RestController
@RequestMapping("/api")
public class BusinessPlanResource {

    private final Logger log = LoggerFactory.getLogger(BusinessPlanResource.class);

    private static final String ENTITY_NAME = "businessPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessPlanService businessPlanService;

    private final BusinessPlanRepository businessPlanRepository;

    public BusinessPlanResource(BusinessPlanService businessPlanService, BusinessPlanRepository businessPlanRepository) {
        this.businessPlanService = businessPlanService;
        this.businessPlanRepository = businessPlanRepository;
    }

    /**
     * {@code POST  /business-plans} : Create a new businessPlan.
     *
     * @param businessPlanDTO the businessPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessPlanDTO, or with status {@code 400 (Bad Request)} if the businessPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-plans")
    public ResponseEntity<BusinessPlanDTO> createBusinessPlan(@RequestBody BusinessPlanDTO businessPlanDTO) throws URISyntaxException {
        log.debug("REST request to save BusinessPlan : {}", businessPlanDTO);

        BusinessPlanDTO result = businessPlanService.save(businessPlanDTO);
        return ResponseEntity
            .created(new URI("/api/business-plans/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /business-plans/:id} : Updates an existing businessPlan.
     *
     * @param id the id of the businessPlanDTO to save.
     * @param businessPlanDTO the businessPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessPlanDTO,
     * or with status {@code 400 (Bad Request)} if the businessPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-plans/{id}")
    public ResponseEntity<BusinessPlanDTO> updateBusinessPlan(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody BusinessPlanDTO businessPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessPlan : {}, {}", id, businessPlanDTO);
        if (businessPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessPlanDTO result = businessPlanService.update(businessPlanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessPlanDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-plans/:id} : Partial updates given fields of an existing businessPlan, field will ignore if it is null
     *
     * @param id the id of the businessPlanDTO to save.
     * @param businessPlanDTO the businessPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessPlanDTO,
     * or with status {@code 400 (Bad Request)} if the businessPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/business-plans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessPlanDTO> partialUpdateBusinessPlan(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody BusinessPlanDTO businessPlanDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessPlan partially : {}, {}", id, businessPlanDTO);
        if (businessPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessPlanDTO> result = businessPlanService.partialUpdate(businessPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessPlanDTO.getId())
        );
    }

    /**
     * {@code GET  /business-plans} : get all the businessPlans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessPlans in body.
     */
    @GetMapping("/business-plans")
    public ResponseEntity<List<BusinessPlanDTO>> getAllBusinessPlans(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of BusinessPlans");
        Page<BusinessPlanDTO> page = businessPlanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-plans/:id} : get the "id" businessPlan.
     *
     * @param id the id of the businessPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-plans/{id}")
    public ResponseEntity<BusinessPlanDTO> getBusinessPlan(@PathVariable String id) {
        log.debug("REST request to get BusinessPlan : {}", id);
        Optional<BusinessPlanDTO> businessPlanDTO = businessPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessPlanDTO);
    }

    @PostMapping("/business-plans/generate")
    public ResponseEntity<String> generateBusinessPlan(@Valid @RequestBody BusinessPlanDTO businessPlanDTO) {
        BusinessPlanInputDTO input = new BusinessPlanInputDTO(businessPlanDTO);
        String generatedPresentation = businessPlanService.generatePresentation(input);
        return ResponseEntity.ok(generatedPresentation);
    }

    @PostMapping("/business-plans/{id}/regenerate")
    public ResponseEntity<BusinessPlanDTO> regeneratePresentation(@PathVariable String id) {
        log.debug("REST request to regenerate presentation for BusinessPlan : {}", id);
        BusinessPlanDTO businessPlanDTO = businessPlanService.findOne(id)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));

        BusinessPlanDTO updated = businessPlanService.update(businessPlanDTO);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/business-plans/{companyName}/presentation")
    public ResponseEntity<List<Slide>> getPresentation(@PathVariable String companyName) {
        log.debug("Enter: getPresentation() with argument[s] = [{}]", companyName);
        List<Slide> slides = businessPlanService.getPresentationSlides(companyName);
        return ResponseEntity.ok(slides);
    }

    @GetMapping("/business-plans/{companyName}/presentation/text")
    public ResponseEntity<String> getPresentationText(@PathVariable String companyName) {
        log.debug("REST request to get presentation text for company: {}", companyName);
        String presentation = businessPlanService.getPresentation(companyName);
        return ResponseEntity.ok(presentation);
    }



    /**
     * {@code PATCH /business-plans/{id}/presentation/slide/{slideIndex}} : Update the content of a specific slide in the generated presentation.
     *
     * @param id the id of the business plan.
     * @param slideIndex the index of the slide to update.
     * @param slideUpdateRequest the request body containing the new content.
     * @return the updated BusinessPlanDTO.
     */
    @PatchMapping("/business-plans/{id}/presentation/slide/{slideIndex}")
    public ResponseEntity<BusinessPlanDTO> updateGeneratedPresentation(
        @PathVariable String id,
        @PathVariable int slideIndex,
        @RequestBody SlideUpdateRequest slideUpdateRequest
    ) {
        log.debug("REST request to update slide {} of generatedPresentation for BusinessPlan : {}", slideIndex, id);

        Optional<BusinessPlanDTO> updatedOptional = businessPlanService.updateGeneratedPresentation(id, slideIndex, slideUpdateRequest.getNewContent());

        return updatedOptional
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /business-plans/:id} : delete the "id" businessPlan.
     *
     * @param id the id of the businessPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-plans/{id}")
    public ResponseEntity<Void> deleteBusinessPlan(@PathVariable String id) {
        log.debug("REST request to delete BusinessPlan : {}", id);
        businessPlanService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
