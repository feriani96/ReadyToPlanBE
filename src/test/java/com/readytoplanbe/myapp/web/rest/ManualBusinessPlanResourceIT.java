package com.readytoplanbe.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.readytoplanbe.myapp.IntegrationTest;
import com.readytoplanbe.myapp.domain.ManualBusinessPlan;
import com.readytoplanbe.myapp.repository.ManualBusinessPlanRepository;
import com.readytoplanbe.myapp.service.dto.ManualBusinessPlanDTO;
import com.readytoplanbe.myapp.service.mapper.ManualBusinessPlanMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ManualBusinessPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManualBusinessPlanResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ENTREPRENEUR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTREPRENEUR_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/manual-business-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ManualBusinessPlanRepository manualBusinessPlanRepository;

    @Autowired
    private ManualBusinessPlanMapper manualBusinessPlanMapper;

    @Autowired
    private MockMvc restManualBusinessPlanMockMvc;

    private ManualBusinessPlan manualBusinessPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualBusinessPlan createEntity() {
        ManualBusinessPlan manualBusinessPlan = new ManualBusinessPlan()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .entrepreneurName(DEFAULT_ENTREPRENEUR_NAME);
        return manualBusinessPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualBusinessPlan createUpdatedEntity() {
        ManualBusinessPlan manualBusinessPlan = new ManualBusinessPlan()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .entrepreneurName(UPDATED_ENTREPRENEUR_NAME);
        return manualBusinessPlan;
    }

    @BeforeEach
    public void initTest() {
        manualBusinessPlanRepository.deleteAll();
        manualBusinessPlan = createEntity();
    }

    @Test
    void createManualBusinessPlan() throws Exception {
        int databaseSizeBeforeCreate = manualBusinessPlanRepository.findAll().size();
        // Create the ManualBusinessPlan
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);
        restManualBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeCreate + 1);
        ManualBusinessPlan testManualBusinessPlan = manualBusinessPlanList.get(manualBusinessPlanList.size() - 1);
        assertThat(testManualBusinessPlan.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testManualBusinessPlan.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testManualBusinessPlan.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testManualBusinessPlan.getEntrepreneurName()).isEqualTo(DEFAULT_ENTREPRENEUR_NAME);
    }

    @Test
    void createManualBusinessPlanWithExistingId() throws Exception {
        // Create the ManualBusinessPlan with an existing ID
        manualBusinessPlan.setId("existing_id");
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        int databaseSizeBeforeCreate = manualBusinessPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManualBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualBusinessPlanRepository.findAll().size();
        // set the field null
        manualBusinessPlan.setName(null);

        // Create the ManualBusinessPlan, which fails.
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        restManualBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualBusinessPlanRepository.findAll().size();
        // set the field null
        manualBusinessPlan.setDescription(null);

        // Create the ManualBusinessPlan, which fails.
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        restManualBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualBusinessPlanRepository.findAll().size();
        // set the field null
        manualBusinessPlan.setCreationDate(null);

        // Create the ManualBusinessPlan, which fails.
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        restManualBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEntrepreneurNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualBusinessPlanRepository.findAll().size();
        // set the field null
        manualBusinessPlan.setEntrepreneurName(null);

        // Create the ManualBusinessPlan, which fails.
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        restManualBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllManualBusinessPlans() throws Exception {
        // Initialize the database
        manualBusinessPlanRepository.save(manualBusinessPlan);

        // Get all the manualBusinessPlanList
        restManualBusinessPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manualBusinessPlan.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].entrepreneurName").value(hasItem(DEFAULT_ENTREPRENEUR_NAME)));
    }

    @Test
    void getManualBusinessPlan() throws Exception {
        // Initialize the database
        manualBusinessPlanRepository.save(manualBusinessPlan);

        // Get the manualBusinessPlan
        restManualBusinessPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, manualBusinessPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manualBusinessPlan.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.entrepreneurName").value(DEFAULT_ENTREPRENEUR_NAME));
    }

    @Test
    void getNonExistingManualBusinessPlan() throws Exception {
        // Get the manualBusinessPlan
        restManualBusinessPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingManualBusinessPlan() throws Exception {
        // Initialize the database
        manualBusinessPlanRepository.save(manualBusinessPlan);

        int databaseSizeBeforeUpdate = manualBusinessPlanRepository.findAll().size();

        // Update the manualBusinessPlan
        ManualBusinessPlan updatedManualBusinessPlan = manualBusinessPlanRepository.findById(manualBusinessPlan.getId()).get();
        updatedManualBusinessPlan
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .entrepreneurName(UPDATED_ENTREPRENEUR_NAME);
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(updatedManualBusinessPlan);

        restManualBusinessPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualBusinessPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeUpdate);
        ManualBusinessPlan testManualBusinessPlan = manualBusinessPlanList.get(manualBusinessPlanList.size() - 1);
        assertThat(testManualBusinessPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManualBusinessPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testManualBusinessPlan.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testManualBusinessPlan.getEntrepreneurName()).isEqualTo(UPDATED_ENTREPRENEUR_NAME);
    }

    @Test
    void putNonExistingManualBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = manualBusinessPlanRepository.findAll().size();
        manualBusinessPlan.setId(UUID.randomUUID().toString());

        // Create the ManualBusinessPlan
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualBusinessPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualBusinessPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchManualBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = manualBusinessPlanRepository.findAll().size();
        manualBusinessPlan.setId(UUID.randomUUID().toString());

        // Create the ManualBusinessPlan
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualBusinessPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamManualBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = manualBusinessPlanRepository.findAll().size();
        manualBusinessPlan.setId(UUID.randomUUID().toString());

        // Create the ManualBusinessPlan
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualBusinessPlanMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateManualBusinessPlanWithPatch() throws Exception {
        // Initialize the database
        manualBusinessPlanRepository.save(manualBusinessPlan);

        int databaseSizeBeforeUpdate = manualBusinessPlanRepository.findAll().size();

        // Update the manualBusinessPlan using partial update
        ManualBusinessPlan partialUpdatedManualBusinessPlan = new ManualBusinessPlan();
        partialUpdatedManualBusinessPlan.setId(manualBusinessPlan.getId());

        partialUpdatedManualBusinessPlan.name(UPDATED_NAME).creationDate(UPDATED_CREATION_DATE).entrepreneurName(UPDATED_ENTREPRENEUR_NAME);

        restManualBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualBusinessPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManualBusinessPlan))
            )
            .andExpect(status().isOk());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeUpdate);
        ManualBusinessPlan testManualBusinessPlan = manualBusinessPlanList.get(manualBusinessPlanList.size() - 1);
        assertThat(testManualBusinessPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManualBusinessPlan.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testManualBusinessPlan.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testManualBusinessPlan.getEntrepreneurName()).isEqualTo(UPDATED_ENTREPRENEUR_NAME);
    }

    @Test
    void fullUpdateManualBusinessPlanWithPatch() throws Exception {
        // Initialize the database
        manualBusinessPlanRepository.save(manualBusinessPlan);

        int databaseSizeBeforeUpdate = manualBusinessPlanRepository.findAll().size();

        // Update the manualBusinessPlan using partial update
        ManualBusinessPlan partialUpdatedManualBusinessPlan = new ManualBusinessPlan();
        partialUpdatedManualBusinessPlan.setId(manualBusinessPlan.getId());

        partialUpdatedManualBusinessPlan
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .entrepreneurName(UPDATED_ENTREPRENEUR_NAME);

        restManualBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualBusinessPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManualBusinessPlan))
            )
            .andExpect(status().isOk());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeUpdate);
        ManualBusinessPlan testManualBusinessPlan = manualBusinessPlanList.get(manualBusinessPlanList.size() - 1);
        assertThat(testManualBusinessPlan.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testManualBusinessPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testManualBusinessPlan.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testManualBusinessPlan.getEntrepreneurName()).isEqualTo(UPDATED_ENTREPRENEUR_NAME);
    }

    @Test
    void patchNonExistingManualBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = manualBusinessPlanRepository.findAll().size();
        manualBusinessPlan.setId(UUID.randomUUID().toString());

        // Create the ManualBusinessPlan
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manualBusinessPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchManualBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = manualBusinessPlanRepository.findAll().size();
        manualBusinessPlan.setId(UUID.randomUUID().toString());

        // Create the ManualBusinessPlan
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamManualBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = manualBusinessPlanRepository.findAll().size();
        manualBusinessPlan.setId(UUID.randomUUID().toString());

        // Create the ManualBusinessPlan
        ManualBusinessPlanDTO manualBusinessPlanDTO = manualBusinessPlanMapper.toDto(manualBusinessPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manualBusinessPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualBusinessPlan in the database
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteManualBusinessPlan() throws Exception {
        // Initialize the database
        manualBusinessPlanRepository.save(manualBusinessPlan);

        int databaseSizeBeforeDelete = manualBusinessPlanRepository.findAll().size();

        // Delete the manualBusinessPlan
        restManualBusinessPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, manualBusinessPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ManualBusinessPlan> manualBusinessPlanList = manualBusinessPlanRepository.findAll();
        assertThat(manualBusinessPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
