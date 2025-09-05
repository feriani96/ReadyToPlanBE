package com.readytoplanbe.myapp.web.rest;

import com.readytoplanbe.myapp.repository.TrainingCourseRepository;
import com.readytoplanbe.myapp.service.TrainingCourseService;
import com.readytoplanbe.myapp.service.dto.TrainingCourseDTO;
import com.readytoplanbe.myapp.service.impl.TrainingCourseServiceImpl;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.readytoplanbe.myapp.domain.TrainingCourse}.
 */
@RestController
@RequestMapping("/api")
public class TrainingCourseResource {

    private final Logger log = LoggerFactory.getLogger(TrainingCourseResource.class);

    private static final String ENTITY_NAME = "trainingCourse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainingCourseService trainingCourseService;

    private final TrainingCourseRepository trainingCourseRepository;

    private final TrainingCourseServiceImpl trainingCourseServiceImpl;



    public TrainingCourseResource(TrainingCourseService trainingCourseService, TrainingCourseRepository trainingCourseRepository, TrainingCourseServiceImpl trainingCourseServiceImpl) {
        this.trainingCourseService = trainingCourseService;
        this.trainingCourseRepository = trainingCourseRepository;
        this.trainingCourseServiceImpl = trainingCourseServiceImpl;

    }

    /**
     * {@code POST  /training-courses} : Create a new trainingCourse.
     *
     * @param trainingCourseDTO the trainingCourseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingCourseDTO, or with status {@code 400 (Bad Request)} if the trainingCourse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/training-courses")
    public ResponseEntity<TrainingCourseDTO> createTrainingCourse(@Valid @RequestBody TrainingCourseDTO trainingCourseDTO)
        throws URISyntaxException {
        log.debug("REST request to save TrainingCourse : {}", trainingCourseDTO);
        if (trainingCourseDTO.getId() != null) {
            throw new BadRequestAlertException("A new trainingCourse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrainingCourseDTO result = trainingCourseService.save(trainingCourseDTO);
        return ResponseEntity
            .created(new URI("/api/training-courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /training-courses/:id} : Updates an existing trainingCourse.
     *
     * @param id the id of the trainingCourseDTO to save.
     * @param trainingCourseDTO the trainingCourseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingCourseDTO,
     * or with status {@code 400 (Bad Request)} if the trainingCourseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingCourseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/training-courses/{id}")
    public ResponseEntity<TrainingCourseDTO> updateTrainingCourse(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody TrainingCourseDTO trainingCourseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TrainingCourse : {}, {}", id, trainingCourseDTO);
        if (trainingCourseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingCourseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingCourseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrainingCourseDTO result = trainingCourseService.update(trainingCourseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trainingCourseDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /training-courses/:id} : Partial updates given fields of an existing trainingCourse, field will ignore if it is null
     *
     * @param id the id of the trainingCourseDTO to save.
     * @param trainingCourseDTO the trainingCourseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingCourseDTO,
     * or with status {@code 400 (Bad Request)} if the trainingCourseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trainingCourseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trainingCourseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/training-courses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrainingCourseDTO> partialUpdateTrainingCourse(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody TrainingCourseDTO trainingCourseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrainingCourse partially : {}, {}", id, trainingCourseDTO);
        if (trainingCourseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingCourseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingCourseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrainingCourseDTO> result = trainingCourseService.partialUpdate(trainingCourseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trainingCourseDTO.getId())
        );
    }

    /**
     * {@code GET  /training-courses} : get all the trainingCourses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainingCourses in body.
     */
    @GetMapping("/training-courses")
    public List<TrainingCourseDTO> getAllTrainingCourses() {
        log.debug("REST request to get all TrainingCourses");
        return trainingCourseService.findAll();
    }

    /**
     * {@code GET  /training-courses/:id} : get the "id" trainingCourse.
     *
     * @param id the id of the trainingCourseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingCourseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/training-courses/{id}")
    public ResponseEntity<TrainingCourseDTO> getTrainingCourse(@PathVariable String id) {
        log.debug("REST request to get TrainingCourse : {}", id);
        Optional<TrainingCourseDTO> trainingCourseDTO = trainingCourseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trainingCourseDTO);
    }

    /**
     * {@code DELETE  /training-courses/:id} : delete the "id" trainingCourse.
     *
     * @param id the id of the trainingCourseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/training-courses/{id}")
    public ResponseEntity<Void> deleteTrainingCourse(@PathVariable String id) {
        log.debug("REST request to delete TrainingCourse : {}", id);
        trainingCourseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    @GetMapping("/training-courses/{id}/presentation")
    public ResponseEntity<String> getPresentation(@PathVariable String id) {
        // Récupérer le cours
        Optional<TrainingCourseDTO> trainingCourseOpt = trainingCourseService.findOne(id);

        if (trainingCourseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TrainingCourseDTO trainingCourse = trainingCourseOpt.get();

        // ✅ Vérifier si la présentation existe déjà
        if (trainingCourse.getPresentation() != null && !trainingCourse.getPresentation().isEmpty()) {
            return ResponseEntity.ok(trainingCourse.getPresentation());
        }

        // Sinon, la générer une seule fois
        String presentation = trainingCourseServiceImpl.generatePresentation(id);

        trainingCourse.setPresentation(presentation);
        trainingCourseService.update(trainingCourse); // sauvegarder avec la présentation

        return ResponseEntity.ok(presentation);
    }


    @PostMapping("/training-courses/{id}/regenerate-presentation")
    public ResponseEntity<String> regeneratePresentation(@PathVariable String id) {
        Optional<TrainingCourseDTO> trainingCourseOpt = trainingCourseService.findOne(id);

        if (trainingCourseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TrainingCourseDTO trainingCourse = trainingCourseOpt.get();

        String newPresentation = trainingCourseServiceImpl.generatePresentation(id);

        return ResponseEntity.ok(newPresentation);
    }

    @PostMapping("/training-courses/{id}/save-presentation")
    public ResponseEntity<TrainingCourseDTO> savePresentation(
        @PathVariable String id,
        @RequestBody String newPresentation) {

        Optional<TrainingCourseDTO> trainingCourseOpt = trainingCourseService.findOne(id);

        if (trainingCourseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TrainingCourseDTO trainingCourse = trainingCourseOpt.get();
        trainingCourse.setPresentation(newPresentation);
        TrainingCourseDTO updated = trainingCourseService.update(trainingCourse);

        return ResponseEntity.ok(updated);
    }


}
