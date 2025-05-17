package com.readytoplanbe.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.readytoplanbe.myapp.IntegrationTest;
import com.readytoplanbe.myapp.domain.BusinessPlan;
import com.readytoplanbe.myapp.domain.enumeration.Country;
import com.readytoplanbe.myapp.domain.enumeration.Currency;
import com.readytoplanbe.myapp.domain.enumeration.Languages;
import com.readytoplanbe.myapp.repository.BusinessPlanRepository;
import com.readytoplanbe.myapp.service.dto.BusinessPlanDTO;
import com.readytoplanbe.myapp.service.mapper.BusinessPlanMapper;
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
 * Integration tests for the {@link BusinessPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusinessPlanResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_COMPANY_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMPANY_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Country DEFAULT_COUNTRY = Country.ALGERIA;
    private static final Country UPDATED_COUNTRY = Country.ARGENTINA;

    private static final Languages DEFAULT_LANGUAGES = Languages.ENGLISH;
    private static final Languages UPDATED_LANGUAGES = Languages.FRENCH;

    private static final String DEFAULT_COMPANY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_ANTICIPATED_PROJECT_SIZE = 1D;
    private static final Double UPDATED_ANTICIPATED_PROJECT_SIZE = 2D;

    private static final Currency DEFAULT_CURRENCY = Currency.USD;
    private static final Currency UPDATED_CURRENCY = Currency.EUR;

    private static final String ENTITY_API_URL = "/api/business-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BusinessPlanRepository businessPlanRepository;

    @Autowired
    private BusinessPlanMapper businessPlanMapper;

    @Autowired
    private MockMvc restBusinessPlanMockMvc;

    private BusinessPlan businessPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessPlan createEntity() {
        BusinessPlan businessPlan = new BusinessPlan()
            .companyName(DEFAULT_COMPANY_NAME)
            .companyStartDate(DEFAULT_COMPANY_START_DATE)
            .country(DEFAULT_COUNTRY)
            .languages(DEFAULT_LANGUAGES)
            .companyDescription(DEFAULT_COMPANY_DESCRIPTION)
            .anticipatedProjectSize(DEFAULT_ANTICIPATED_PROJECT_SIZE)
            .currency(DEFAULT_CURRENCY);
        return businessPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessPlan createUpdatedEntity() {
        BusinessPlan businessPlan = new BusinessPlan()
            .companyName(UPDATED_COMPANY_NAME)
            .companyStartDate(UPDATED_COMPANY_START_DATE)
            .country(UPDATED_COUNTRY)
            .languages(UPDATED_LANGUAGES)
            .companyDescription(UPDATED_COMPANY_DESCRIPTION)
            .anticipatedProjectSize(UPDATED_ANTICIPATED_PROJECT_SIZE)
            .currency(UPDATED_CURRENCY);
        return businessPlan;
    }

    @BeforeEach
    public void initTest() {
        businessPlanRepository.deleteAll();
        businessPlan = createEntity();
    }

    @Test
    void createBusinessPlan() throws Exception {
        int databaseSizeBeforeCreate = businessPlanRepository.findAll().size();
        // Create the BusinessPlan
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);
        restBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessPlan testBusinessPlan = businessPlanList.get(businessPlanList.size() - 1);
        assertThat(testBusinessPlan.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testBusinessPlan.getCompanyStartDate()).isEqualTo(DEFAULT_COMPANY_START_DATE);
        assertThat(testBusinessPlan.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testBusinessPlan.getLanguages()).isEqualTo(DEFAULT_LANGUAGES);
        assertThat(testBusinessPlan.getCompanyDescription()).isEqualTo(DEFAULT_COMPANY_DESCRIPTION);
        assertThat(testBusinessPlan.getAnticipatedProjectSize()).isEqualTo(DEFAULT_ANTICIPATED_PROJECT_SIZE);
        assertThat(testBusinessPlan.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
    }

    @Test
    void createBusinessPlanWithExistingId() throws Exception {
        // Create the BusinessPlan with an existing ID
        businessPlan.setId("existing_id");
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        int databaseSizeBeforeCreate = businessPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessPlanRepository.findAll().size();
        // set the field null
        businessPlan.setCompanyName(null);

        // Create the BusinessPlan, which fails.
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        restBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCompanyStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessPlanRepository.findAll().size();
        // set the field null
        businessPlan.setCompanyStartDate(null);

        // Create the BusinessPlan, which fails.
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        restBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessPlanRepository.findAll().size();
        // set the field null
        businessPlan.setCountry(null);

        // Create the BusinessPlan, which fails.
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        restBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkLanguagesIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessPlanRepository.findAll().size();
        // set the field null
        businessPlan.setLanguages(null);

        // Create the BusinessPlan, which fails.
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        restBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCompanyDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessPlanRepository.findAll().size();
        // set the field null
        businessPlan.setCompanyDescription(null);

        // Create the BusinessPlan, which fails.
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        restBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAnticipatedProjectSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessPlanRepository.findAll().size();
        // set the field null
        businessPlan.setAnticipatedProjectSize(null);

        // Create the BusinessPlan, which fails.
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        restBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessPlanRepository.findAll().size();
        // set the field null
        businessPlan.setCurrency(null);

        // Create the BusinessPlan, which fails.
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        restBusinessPlanMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllBusinessPlans() throws Exception {
        // Initialize the database
        businessPlanRepository.save(businessPlan);

        // Get all the businessPlanList
        restBusinessPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessPlan.getId())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].companyStartDate").value(hasItem(DEFAULT_COMPANY_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].languages").value(hasItem(DEFAULT_LANGUAGES.toString())))
            .andExpect(jsonPath("$.[*].companyDescription").value(hasItem(DEFAULT_COMPANY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].anticipatedProjectSize").value(hasItem(DEFAULT_ANTICIPATED_PROJECT_SIZE.doubleValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    void getBusinessPlan() throws Exception {
        // Initialize the database
        businessPlanRepository.save(businessPlan);

        // Get the businessPlan
        restBusinessPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, businessPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessPlan.getId()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.companyStartDate").value(DEFAULT_COMPANY_START_DATE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.languages").value(DEFAULT_LANGUAGES.toString()))
            .andExpect(jsonPath("$.companyDescription").value(DEFAULT_COMPANY_DESCRIPTION))
            .andExpect(jsonPath("$.anticipatedProjectSize").value(DEFAULT_ANTICIPATED_PROJECT_SIZE.doubleValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    void getNonExistingBusinessPlan() throws Exception {
        // Get the businessPlan
        restBusinessPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingBusinessPlan() throws Exception {
        // Initialize the database
        businessPlanRepository.save(businessPlan);

        int databaseSizeBeforeUpdate = businessPlanRepository.findAll().size();

        // Update the businessPlan
        BusinessPlan updatedBusinessPlan = businessPlanRepository.findById(businessPlan.getId()).get();
        updatedBusinessPlan
            .companyName(UPDATED_COMPANY_NAME)
            .companyStartDate(UPDATED_COMPANY_START_DATE)
            .country(UPDATED_COUNTRY)
            .languages(UPDATED_LANGUAGES)
            .companyDescription(UPDATED_COMPANY_DESCRIPTION)
            .anticipatedProjectSize(UPDATED_ANTICIPATED_PROJECT_SIZE)
            .currency(UPDATED_CURRENCY);
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(updatedBusinessPlan);

        restBusinessPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeUpdate);
        BusinessPlan testBusinessPlan = businessPlanList.get(businessPlanList.size() - 1);
        assertThat(testBusinessPlan.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testBusinessPlan.getCompanyStartDate()).isEqualTo(UPDATED_COMPANY_START_DATE);
        assertThat(testBusinessPlan.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testBusinessPlan.getLanguages()).isEqualTo(UPDATED_LANGUAGES);
        assertThat(testBusinessPlan.getCompanyDescription()).isEqualTo(UPDATED_COMPANY_DESCRIPTION);
        assertThat(testBusinessPlan.getAnticipatedProjectSize()).isEqualTo(UPDATED_ANTICIPATED_PROJECT_SIZE);
        assertThat(testBusinessPlan.getCurrency()).isEqualTo(UPDATED_CURRENCY);
    }

    @Test
    void putNonExistingBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = businessPlanRepository.findAll().size();
        businessPlan.setId(UUID.randomUUID().toString());

        // Create the BusinessPlan
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = businessPlanRepository.findAll().size();
        businessPlan.setId(UUID.randomUUID().toString());

        // Create the BusinessPlan
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = businessPlanRepository.findAll().size();
        businessPlan.setId(UUID.randomUUID().toString());

        // Create the BusinessPlan
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessPlanMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBusinessPlanWithPatch() throws Exception {
        // Initialize the database
        businessPlanRepository.save(businessPlan);

        int databaseSizeBeforeUpdate = businessPlanRepository.findAll().size();

        // Update the businessPlan using partial update
        BusinessPlan partialUpdatedBusinessPlan = new BusinessPlan();
        partialUpdatedBusinessPlan.setId(businessPlan.getId());

        partialUpdatedBusinessPlan
            .companyName(UPDATED_COMPANY_NAME)
            .country(UPDATED_COUNTRY)
            .companyDescription(UPDATED_COMPANY_DESCRIPTION)
            .anticipatedProjectSize(UPDATED_ANTICIPATED_PROJECT_SIZE)
            .currency(UPDATED_CURRENCY);

        restBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessPlan))
            )
            .andExpect(status().isOk());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeUpdate);
        BusinessPlan testBusinessPlan = businessPlanList.get(businessPlanList.size() - 1);
        assertThat(testBusinessPlan.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testBusinessPlan.getCompanyStartDate()).isEqualTo(DEFAULT_COMPANY_START_DATE);
        assertThat(testBusinessPlan.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testBusinessPlan.getLanguages()).isEqualTo(DEFAULT_LANGUAGES);
        assertThat(testBusinessPlan.getCompanyDescription()).isEqualTo(UPDATED_COMPANY_DESCRIPTION);
        assertThat(testBusinessPlan.getAnticipatedProjectSize()).isEqualTo(UPDATED_ANTICIPATED_PROJECT_SIZE);
        assertThat(testBusinessPlan.getCurrency()).isEqualTo(UPDATED_CURRENCY);
    }

    @Test
    void fullUpdateBusinessPlanWithPatch() throws Exception {
        // Initialize the database
        businessPlanRepository.save(businessPlan);

        int databaseSizeBeforeUpdate = businessPlanRepository.findAll().size();

        // Update the businessPlan using partial update
        BusinessPlan partialUpdatedBusinessPlan = new BusinessPlan();
        partialUpdatedBusinessPlan.setId(businessPlan.getId());

        partialUpdatedBusinessPlan
            .companyName(UPDATED_COMPANY_NAME)
            .companyStartDate(UPDATED_COMPANY_START_DATE)
            .country(UPDATED_COUNTRY)
            .languages(UPDATED_LANGUAGES)
            .companyDescription(UPDATED_COMPANY_DESCRIPTION)
            .anticipatedProjectSize(UPDATED_ANTICIPATED_PROJECT_SIZE)
            .currency(UPDATED_CURRENCY);

        restBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessPlan))
            )
            .andExpect(status().isOk());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeUpdate);
        BusinessPlan testBusinessPlan = businessPlanList.get(businessPlanList.size() - 1);
        assertThat(testBusinessPlan.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testBusinessPlan.getCompanyStartDate()).isEqualTo(UPDATED_COMPANY_START_DATE);
        assertThat(testBusinessPlan.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testBusinessPlan.getLanguages()).isEqualTo(UPDATED_LANGUAGES);
        assertThat(testBusinessPlan.getCompanyDescription()).isEqualTo(UPDATED_COMPANY_DESCRIPTION);
        assertThat(testBusinessPlan.getAnticipatedProjectSize()).isEqualTo(UPDATED_ANTICIPATED_PROJECT_SIZE);
        assertThat(testBusinessPlan.getCurrency()).isEqualTo(UPDATED_CURRENCY);
    }

    @Test
    void patchNonExistingBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = businessPlanRepository.findAll().size();
        businessPlan.setId(UUID.randomUUID().toString());

        // Create the BusinessPlan
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = businessPlanRepository.findAll().size();
        businessPlan.setId(UUID.randomUUID().toString());

        // Create the BusinessPlan
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBusinessPlan() throws Exception {
        int databaseSizeBeforeUpdate = businessPlanRepository.findAll().size();
        businessPlan.setId(UUID.randomUUID().toString());

        // Create the BusinessPlan
        BusinessPlanDTO businessPlanDTO = businessPlanMapper.toDto(businessPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessPlanMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessPlanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessPlan in the database
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBusinessPlan() throws Exception {
        // Initialize the database
        businessPlanRepository.save(businessPlan);

        int databaseSizeBeforeDelete = businessPlanRepository.findAll().size();

        // Delete the businessPlan
        restBusinessPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessPlan> businessPlanList = businessPlanRepository.findAll();
        assertThat(businessPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
