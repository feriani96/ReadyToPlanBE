package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.domain.AIGeneratedResponse;
import com.readytoplanbe.myapp.domain.enumeration.EntityType;
import com.readytoplanbe.myapp.repository.AIGeneratedResponseRepository;
import com.readytoplanbe.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.AIGeneratedResponse}.
 */
@RestController
@RequestMapping("/api")
public class AIGeneratedResponseResource {

    private final Logger log = LoggerFactory.getLogger(AIGeneratedResponseResource.class);

    private static final String ENTITY_NAME = "aIGeneratedResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AIGeneratedResponseRepository aIGeneratedResponseRepository;

    public AIGeneratedResponseResource(AIGeneratedResponseRepository aIGeneratedResponseRepository) {
        this.aIGeneratedResponseRepository = aIGeneratedResponseRepository;
    }

    /**
     * {@code POST  /ai-generated-responses} : Create a new aIGeneratedResponse.
     *
     * @param aIGeneratedResponse the aIGeneratedResponse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aIGeneratedResponse, or with status {@code 400 (Bad Request)} if the aIGeneratedResponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ai-generated-responses")
    public ResponseEntity<AIGeneratedResponse> createAIGeneratedResponse(@Valid @RequestBody AIGeneratedResponse aIGeneratedResponse)
        throws URISyntaxException {
        log.debug("REST request to save AIGeneratedResponse : {}", aIGeneratedResponse);
        if (aIGeneratedResponse.getId() != null) {
            throw new BadRequestAlertException("A new aIGeneratedResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AIGeneratedResponse result = aIGeneratedResponseRepository.save(aIGeneratedResponse);
        return ResponseEntity
            .created(new URI("/api/ai-generated-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /ai-generated-responses/:id} : Updates an existing aIGeneratedResponse.
     *
     * @param id the id of the aIGeneratedResponse to save.
     * @param aIGeneratedResponse the aIGeneratedResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIGeneratedResponse,
     * or with status {@code 400 (Bad Request)} if the aIGeneratedResponse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aIGeneratedResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ai-generated-responses/{id}")
    public ResponseEntity<AIGeneratedResponse> updateAIGeneratedResponse(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody AIGeneratedResponse aIGeneratedResponse
    ) throws URISyntaxException {
        log.debug("REST request to update AIGeneratedResponse : {}, {}", id, aIGeneratedResponse);
        if (aIGeneratedResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aIGeneratedResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aIGeneratedResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AIGeneratedResponse result = aIGeneratedResponseRepository.save(aIGeneratedResponse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aIGeneratedResponse.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /ai-generated-responses/:id} : Partial updates given fields of an existing aIGeneratedResponse, field will ignore if it is null
     *
     * @param id the id of the aIGeneratedResponse to save.
     * @param aIGeneratedResponse the aIGeneratedResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIGeneratedResponse,
     * or with status {@code 400 (Bad Request)} if the aIGeneratedResponse is not valid,
     * or with status {@code 404 (Not Found)} if the aIGeneratedResponse is not found,
     * or with status {@code 500 (Internal Server Error)} if the aIGeneratedResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ai-generated-responses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AIGeneratedResponse> partialUpdateAIGeneratedResponse(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody AIGeneratedResponse aIGeneratedResponse
    ) throws URISyntaxException {
        log.debug("REST request to partial update AIGeneratedResponse partially : {}, {}", id, aIGeneratedResponse);
        if (aIGeneratedResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aIGeneratedResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aIGeneratedResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AIGeneratedResponse> result = aIGeneratedResponseRepository
            .findById(aIGeneratedResponse.getId())
            .map(existingAIGeneratedResponse -> {
                if (aIGeneratedResponse.getEntityId() != null) {
                    existingAIGeneratedResponse.setEntityId(aIGeneratedResponse.getEntityId());
                }
                if (aIGeneratedResponse.getEntityType() != null) {
                    existingAIGeneratedResponse.setEntityType(aIGeneratedResponse.getEntityType());
                }
                if (aIGeneratedResponse.getPrompt() != null) {
                    existingAIGeneratedResponse.setPrompt(aIGeneratedResponse.getPrompt());
                }
                if (aIGeneratedResponse.getAiResponse() != null) {
                    existingAIGeneratedResponse.setAiResponse(aIGeneratedResponse.getAiResponse());
                }
                if (aIGeneratedResponse.getBusinessPlanId() != null) {
                    existingAIGeneratedResponse.setBusinessPlanId(aIGeneratedResponse.getBusinessPlanId());
                }

                return existingAIGeneratedResponse;
            })
            .map(aIGeneratedResponseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aIGeneratedResponse.getId())
        );
    }

    /**
     * {@code GET  /ai-generated-responses} : get all the aIGeneratedResponses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aIGeneratedResponses in body.
     */
    @GetMapping("/ai-responses/{entityType}/{entityId}")
    public ResponseEntity<AIGeneratedResponse> getByEntityTypeAndEntityId(
        @PathVariable EntityType entityType,
        @PathVariable String entityId
    ) {
        log.debug("REST request to get AIGeneratedResponse by entityType {} and entityId {}", entityType, entityId);
        Optional<AIGeneratedResponse> response = aIGeneratedResponseRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return ResponseUtil.wrapOrNotFound(response);
    }

    /**
     * {@code GET  /ai-generated-responses/:id} : get the "id" aIGeneratedResponse.
     *
     * @param id the id of the aIGeneratedResponse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aIGeneratedResponse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ai-generated-responses/{id}")
    public ResponseEntity<AIGeneratedResponse> getAIGeneratedResponse(@PathVariable String id) {
        log.debug("REST request to get AIGeneratedResponse : {}", id);
        Optional<AIGeneratedResponse> aIGeneratedResponse = aIGeneratedResponseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(aIGeneratedResponse);
    }

    /**
     * {@code DELETE  /ai-generated-responses/:id} : delete the "id" aIGeneratedResponse.
     *
     * @param id the id of the aIGeneratedResponse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ai-generated-responses/{id}")
    public ResponseEntity<Void> deleteAIGeneratedResponse(@PathVariable String id) {
        log.debug("REST request to delete AIGeneratedResponse : {}", id);
        aIGeneratedResponseRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
