package com.readytoplanbe.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.readytoplanbe.myapp.IntegrationTest;
import com.readytoplanbe.myapp.domain.TrainingCourse;
import com.readytoplanbe.myapp.domain.enumeration.Languages;
import com.readytoplanbe.myapp.domain.enumeration.Level;
import com.readytoplanbe.myapp.domain.enumeration.LocationType;
import com.readytoplanbe.myapp.domain.enumeration.StudyClass;
import com.readytoplanbe.myapp.domain.enumeration.TargetAudience;
import com.readytoplanbe.myapp.repository.TrainingCourseRepository;
import com.readytoplanbe.myapp.service.dto.TrainingCourseDTO;
import com.readytoplanbe.myapp.service.mapper.TrainingCourseMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link TrainingCourseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainingCourseResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final TargetAudience DEFAULT_TARGET_AUDIENCE = TargetAudience.STUDENTS;
    private static final TargetAudience UPDATED_TARGET_AUDIENCE = TargetAudience.TEACHERS;

    private static final String DEFAULT_INSTRUCTOR = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTOR = "BBBBBBBBBB";

    private static final StudyClass DEFAULT_STUDY_CLASS = StudyClass.PRIMARY;
    private static final StudyClass UPDATED_STUDY_CLASS = StudyClass.HIGH_SCHOOL;

    private static final Level DEFAULT_LEVEL = Level.BEGINNER;
    private static final Level UPDATED_LEVEL = Level.INTERMEDIATE;

    private static final LocationType DEFAULT_LOCATION_TYPE = LocationType.SITE;
    private static final LocationType UPDATED_LOCATION_TYPE = LocationType.UNIVERSITY;

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final Languages DEFAULT_LANGUAGES = Languages.ENGLISH;
    private static final Languages UPDATED_LANGUAGES = Languages.FRENCH;

    private static final String ENTITY_API_URL = "/api/training-courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private TrainingCourseRepository trainingCourseRepository;

    @Autowired
    private TrainingCourseMapper trainingCourseMapper;

    @Autowired
    private MockMvc restTrainingCourseMockMvc;

    private TrainingCourse trainingCourse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingCourse createEntity() {
        TrainingCourse trainingCourse = new TrainingCourse()
            .title(DEFAULT_TITLE)
            .summary(DEFAULT_SUMMARY)
            .targetAudience(DEFAULT_TARGET_AUDIENCE)
            .instructor(DEFAULT_INSTRUCTOR)
            .studyClass(DEFAULT_STUDY_CLASS)
            .level(DEFAULT_LEVEL)
            .locationType(DEFAULT_LOCATION_TYPE)
            .duration(DEFAULT_DURATION)
            .languages(DEFAULT_LANGUAGES);
        return trainingCourse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingCourse createUpdatedEntity() {
        TrainingCourse trainingCourse = new TrainingCourse()
            .title(UPDATED_TITLE)
            .summary(UPDATED_SUMMARY)
            .targetAudience(UPDATED_TARGET_AUDIENCE)
            .instructor(UPDATED_INSTRUCTOR)
            .studyClass(UPDATED_STUDY_CLASS)
            .level(UPDATED_LEVEL)
            .locationType(UPDATED_LOCATION_TYPE)
            .duration(UPDATED_DURATION)
            .languages(UPDATED_LANGUAGES);
        return trainingCourse;
    }

    @BeforeEach
    public void initTest() {
        trainingCourseRepository.deleteAll();
        trainingCourse = createEntity();
    }

    @Test
    void createTrainingCourse() throws Exception {
        int databaseSizeBeforeCreate = trainingCourseRepository.findAll().size();
        // Create the TrainingCourse
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);
        restTrainingCourseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeCreate + 1);
        TrainingCourse testTrainingCourse = trainingCourseList.get(trainingCourseList.size() - 1);
        assertThat(testTrainingCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTrainingCourse.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testTrainingCourse.getTargetAudience()).isEqualTo(DEFAULT_TARGET_AUDIENCE);
        assertThat(testTrainingCourse.getInstructor()).isEqualTo(DEFAULT_INSTRUCTOR);
        assertThat(testTrainingCourse.getStudyClass()).isEqualTo(DEFAULT_STUDY_CLASS);
        assertThat(testTrainingCourse.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testTrainingCourse.getLocationType()).isEqualTo(DEFAULT_LOCATION_TYPE);
        assertThat(testTrainingCourse.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testTrainingCourse.getLanguages()).isEqualTo(DEFAULT_LANGUAGES);
    }

    @Test
    void createTrainingCourseWithExistingId() throws Exception {
        // Create the TrainingCourse with an existing ID
        trainingCourse.setId("existing_id");
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        int databaseSizeBeforeCreate = trainingCourseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingCourseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingCourseRepository.findAll().size();
        // set the field null
        trainingCourse.setTitle(null);

        // Create the TrainingCourse, which fails.
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        restTrainingCourseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTargetAudienceIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingCourseRepository.findAll().size();
        // set the field null
        trainingCourse.setTargetAudience(null);

        // Create the TrainingCourse, which fails.
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        restTrainingCourseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStudyClassIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingCourseRepository.findAll().size();
        // set the field null
        trainingCourse.setStudyClass(null);

        // Create the TrainingCourse, which fails.
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        restTrainingCourseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingCourseRepository.findAll().size();
        // set the field null
        trainingCourse.setLevel(null);

        // Create the TrainingCourse, which fails.
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        restTrainingCourseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLocationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingCourseRepository.findAll().size();
        // set the field null
        trainingCourse.setLocationType(null);

        // Create the TrainingCourse, which fails.
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        restTrainingCourseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLanguagesIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingCourseRepository.findAll().size();
        // set the field null
        trainingCourse.setLanguages(null);

        // Create the TrainingCourse, which fails.
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        restTrainingCourseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTrainingCourses() throws Exception {
        // Initialize the database
        trainingCourseRepository.save(trainingCourse);

        // Get all the trainingCourseList
        restTrainingCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingCourse.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].targetAudience").value(hasItem(DEFAULT_TARGET_AUDIENCE.toString())))
            .andExpect(jsonPath("$.[*].instructor").value(hasItem(DEFAULT_INSTRUCTOR)))
            .andExpect(jsonPath("$.[*].studyClass").value(hasItem(DEFAULT_STUDY_CLASS.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].locationType").value(hasItem(DEFAULT_LOCATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].languages").value(hasItem(DEFAULT_LANGUAGES.toString())));
    }

    @Test
    void getTrainingCourse() throws Exception {
        // Initialize the database
        trainingCourseRepository.save(trainingCourse);

        // Get the trainingCourse
        restTrainingCourseMockMvc
            .perform(get(ENTITY_API_URL_ID, trainingCourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainingCourse.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.targetAudience").value(DEFAULT_TARGET_AUDIENCE.toString()))
            .andExpect(jsonPath("$.instructor").value(DEFAULT_INSTRUCTOR))
            .andExpect(jsonPath("$.studyClass").value(DEFAULT_STUDY_CLASS.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.locationType").value(DEFAULT_LOCATION_TYPE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.languages").value(DEFAULT_LANGUAGES.toString()));
    }

    @Test
    void getNonExistingTrainingCourse() throws Exception {
        // Get the trainingCourse
        restTrainingCourseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingTrainingCourse() throws Exception {
        // Initialize the database
        trainingCourseRepository.save(trainingCourse);

        int databaseSizeBeforeUpdate = trainingCourseRepository.findAll().size();

        // Update the trainingCourse
        TrainingCourse updatedTrainingCourse = trainingCourseRepository.findById(trainingCourse.getId()).get();
        updatedTrainingCourse
            .title(UPDATED_TITLE)
            .summary(UPDATED_SUMMARY)
            .targetAudience(UPDATED_TARGET_AUDIENCE)
            .instructor(UPDATED_INSTRUCTOR)
            .studyClass(UPDATED_STUDY_CLASS)
            .level(UPDATED_LEVEL)
            .locationType(UPDATED_LOCATION_TYPE)
            .duration(UPDATED_DURATION)
            .languages(UPDATED_LANGUAGES);
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(updatedTrainingCourse);

        restTrainingCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingCourseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeUpdate);
        TrainingCourse testTrainingCourse = trainingCourseList.get(trainingCourseList.size() - 1);
        assertThat(testTrainingCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTrainingCourse.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testTrainingCourse.getTargetAudience()).isEqualTo(UPDATED_TARGET_AUDIENCE);
        assertThat(testTrainingCourse.getInstructor()).isEqualTo(UPDATED_INSTRUCTOR);
        assertThat(testTrainingCourse.getStudyClass()).isEqualTo(UPDATED_STUDY_CLASS);
        assertThat(testTrainingCourse.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testTrainingCourse.getLocationType()).isEqualTo(UPDATED_LOCATION_TYPE);
        assertThat(testTrainingCourse.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testTrainingCourse.getLanguages()).isEqualTo(UPDATED_LANGUAGES);
    }

    @Test
    void putNonExistingTrainingCourse() throws Exception {
        int databaseSizeBeforeUpdate = trainingCourseRepository.findAll().size();
        trainingCourse.setId(UUID.randomUUID().toString());

        // Create the TrainingCourse
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingCourseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTrainingCourse() throws Exception {
        int databaseSizeBeforeUpdate = trainingCourseRepository.findAll().size();
        trainingCourse.setId(UUID.randomUUID().toString());

        // Create the TrainingCourse
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTrainingCourse() throws Exception {
        int databaseSizeBeforeUpdate = trainingCourseRepository.findAll().size();
        trainingCourse.setId(UUID.randomUUID().toString());

        // Create the TrainingCourse
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingCourseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTrainingCourseWithPatch() throws Exception {
        // Initialize the database
        trainingCourseRepository.save(trainingCourse);

        int databaseSizeBeforeUpdate = trainingCourseRepository.findAll().size();

        // Update the trainingCourse using partial update
        TrainingCourse partialUpdatedTrainingCourse = new TrainingCourse();
        partialUpdatedTrainingCourse.setId(trainingCourse.getId());

        partialUpdatedTrainingCourse
            .targetAudience(UPDATED_TARGET_AUDIENCE)
            .instructor(UPDATED_INSTRUCTOR)
            .studyClass(UPDATED_STUDY_CLASS)
            .level(UPDATED_LEVEL)
            .duration(UPDATED_DURATION);

        restTrainingCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingCourse))
            )
            .andExpect(status().isOk());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeUpdate);
        TrainingCourse testTrainingCourse = trainingCourseList.get(trainingCourseList.size() - 1);
        assertThat(testTrainingCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTrainingCourse.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testTrainingCourse.getTargetAudience()).isEqualTo(UPDATED_TARGET_AUDIENCE);
        assertThat(testTrainingCourse.getInstructor()).isEqualTo(UPDATED_INSTRUCTOR);
        assertThat(testTrainingCourse.getStudyClass()).isEqualTo(UPDATED_STUDY_CLASS);
        assertThat(testTrainingCourse.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testTrainingCourse.getLocationType()).isEqualTo(DEFAULT_LOCATION_TYPE);
        assertThat(testTrainingCourse.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testTrainingCourse.getLanguages()).isEqualTo(DEFAULT_LANGUAGES);
    }

    @Test
    void fullUpdateTrainingCourseWithPatch() throws Exception {
        // Initialize the database
        trainingCourseRepository.save(trainingCourse);

        int databaseSizeBeforeUpdate = trainingCourseRepository.findAll().size();

        // Update the trainingCourse using partial update
        TrainingCourse partialUpdatedTrainingCourse = new TrainingCourse();
        partialUpdatedTrainingCourse.setId(trainingCourse.getId());

        partialUpdatedTrainingCourse
            .title(UPDATED_TITLE)
            .summary(UPDATED_SUMMARY)
            .targetAudience(UPDATED_TARGET_AUDIENCE)
            .instructor(UPDATED_INSTRUCTOR)
            .studyClass(UPDATED_STUDY_CLASS)
            .level(UPDATED_LEVEL)
            .locationType(UPDATED_LOCATION_TYPE)
            .duration(UPDATED_DURATION)
            .languages(UPDATED_LANGUAGES);

        restTrainingCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingCourse))
            )
            .andExpect(status().isOk());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeUpdate);
        TrainingCourse testTrainingCourse = trainingCourseList.get(trainingCourseList.size() - 1);
        assertThat(testTrainingCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTrainingCourse.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testTrainingCourse.getTargetAudience()).isEqualTo(UPDATED_TARGET_AUDIENCE);
        assertThat(testTrainingCourse.getInstructor()).isEqualTo(UPDATED_INSTRUCTOR);
        assertThat(testTrainingCourse.getStudyClass()).isEqualTo(UPDATED_STUDY_CLASS);
        assertThat(testTrainingCourse.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testTrainingCourse.getLocationType()).isEqualTo(UPDATED_LOCATION_TYPE);
        assertThat(testTrainingCourse.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testTrainingCourse.getLanguages()).isEqualTo(UPDATED_LANGUAGES);
    }

    @Test
    void patchNonExistingTrainingCourse() throws Exception {
        int databaseSizeBeforeUpdate = trainingCourseRepository.findAll().size();
        trainingCourse.setId(UUID.randomUUID().toString());

        // Create the TrainingCourse
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingCourseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTrainingCourse() throws Exception {
        int databaseSizeBeforeUpdate = trainingCourseRepository.findAll().size();
        trainingCourse.setId(UUID.randomUUID().toString());

        // Create the TrainingCourse
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTrainingCourse() throws Exception {
        int databaseSizeBeforeUpdate = trainingCourseRepository.findAll().size();
        trainingCourse.setId(UUID.randomUUID().toString());

        // Create the TrainingCourse
        TrainingCourseDTO trainingCourseDTO = trainingCourseMapper.toDto(trainingCourse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingCourseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingCourseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingCourse in the database
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTrainingCourse() throws Exception {
        // Initialize the database
        trainingCourseRepository.save(trainingCourse);

        int databaseSizeBeforeDelete = trainingCourseRepository.findAll().size();

        // Delete the trainingCourse
        restTrainingCourseMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainingCourse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrainingCourse> trainingCourseList = trainingCourseRepository.findAll();
        assertThat(trainingCourseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
