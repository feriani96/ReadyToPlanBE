package com.readytoplanbe.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.readytoplanbe.myapp.IntegrationTest;
import com.readytoplanbe.myapp.domain.Enterprise;
import com.readytoplanbe.myapp.domain.enumeration.Country;
import com.readytoplanbe.myapp.domain.enumeration.Currency;
import com.readytoplanbe.myapp.repository.EnterpriseRepository;
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
 * Integration tests for the {@link EnterpriseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnterpriseResourceIT {

    private static final String DEFAULT_ENTERPRISE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTERPRISE_NAME = "BBBBBBBBBB";

    private static final Country DEFAULT_COUNTRY = Country.ALGERIA;
    private static final Country UPDATED_COUNTRY = Country.ARGENTINA;

    private static final Integer DEFAULT_PHONE_NUMBER = 1;
    private static final Integer UPDATED_PHONE_NUMBER = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final Currency DEFAULT_CURRENCY = Currency.USD;
    private static final Currency UPDATED_CURRENCY = Currency.EUR;

    private static final String ENTITY_API_URL = "/api/enterprises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private MockMvc restEnterpriseMockMvc;

    private Enterprise enterprise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enterprise createEntity() {
        Enterprise enterprise = new Enterprise()
            .enterpriseName(DEFAULT_ENTERPRISE_NAME)
            .country(DEFAULT_COUNTRY)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY);
        return enterprise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enterprise createUpdatedEntity() {
        Enterprise enterprise = new Enterprise()
            .enterpriseName(UPDATED_ENTERPRISE_NAME)
            .country(UPDATED_COUNTRY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY);
        return enterprise;
    }

    @BeforeEach
    public void initTest() {
        enterpriseRepository.deleteAll();
        enterprise = createEntity();
    }

    @Test
    void createEnterprise() throws Exception {
        int databaseSizeBeforeCreate = enterpriseRepository.findAll().size();
        // Create the Enterprise
        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterprise)))
            .andExpect(status().isCreated());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeCreate + 1);
        Enterprise testEnterprise = enterpriseList.get(enterpriseList.size() - 1);
        assertThat(testEnterprise.getEnterpriseName()).isEqualTo(DEFAULT_ENTERPRISE_NAME);
        assertThat(testEnterprise.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testEnterprise.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testEnterprise.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEnterprise.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testEnterprise.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
    }

    @Test
    void createEnterpriseWithExistingId() throws Exception {
        // Create the Enterprise with an existing ID
        enterprise.setId("existing_id");

        int databaseSizeBeforeCreate = enterpriseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterprise)))
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkEnterpriseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setEnterpriseName(null);

        // Create the Enterprise, which fails.

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterprise)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setCountry(null);

        // Create the Enterprise, which fails.

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterprise)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setPhoneNumber(null);

        // Create the Enterprise, which fails.

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterprise)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setDescription(null);

        // Create the Enterprise, which fails.

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterprise)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setAmount(null);

        // Create the Enterprise, which fails.

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterprise)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = enterpriseRepository.findAll().size();
        // set the field null
        enterprise.setCurrency(null);

        // Create the Enterprise, which fails.

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterprise)))
            .andExpect(status().isBadRequest());

        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllEnterprises() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        // Get all the enterpriseList
        restEnterpriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enterprise.getId())))
            .andExpect(jsonPath("$.[*].enterpriseName").value(hasItem(DEFAULT_ENTERPRISE_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    void getEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        // Get the enterprise
        restEnterpriseMockMvc
            .perform(get(ENTITY_API_URL_ID, enterprise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enterprise.getId()))
            .andExpect(jsonPath("$.enterpriseName").value(DEFAULT_ENTERPRISE_NAME))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    void getNonExistingEnterprise() throws Exception {
        // Get the enterprise
        restEnterpriseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();

        // Update the enterprise
        Enterprise updatedEnterprise = enterpriseRepository.findById(enterprise.getId()).get();
        updatedEnterprise
            .enterpriseName(UPDATED_ENTERPRISE_NAME)
            .country(UPDATED_COUNTRY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY);

        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEnterprise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
        Enterprise testEnterprise = enterpriseList.get(enterpriseList.size() - 1);
        assertThat(testEnterprise.getEnterpriseName()).isEqualTo(UPDATED_ENTERPRISE_NAME);
        assertThat(testEnterprise.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEnterprise.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEnterprise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEnterprise.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testEnterprise.getCurrency()).isEqualTo(UPDATED_CURRENCY);
    }

    @Test
    void putNonExistingEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enterprise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enterprise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enterprise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enterprise)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEnterpriseWithPatch() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();

        // Update the enterprise using partial update
        Enterprise partialUpdatedEnterprise = new Enterprise();
        partialUpdatedEnterprise.setId(enterprise.getId());

        partialUpdatedEnterprise
            .enterpriseName(UPDATED_ENTERPRISE_NAME)
            .country(UPDATED_COUNTRY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT);

        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
        Enterprise testEnterprise = enterpriseList.get(enterpriseList.size() - 1);
        assertThat(testEnterprise.getEnterpriseName()).isEqualTo(UPDATED_ENTERPRISE_NAME);
        assertThat(testEnterprise.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEnterprise.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEnterprise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEnterprise.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testEnterprise.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
    }

    @Test
    void fullUpdateEnterpriseWithPatch() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();

        // Update the enterprise using partial update
        Enterprise partialUpdatedEnterprise = new Enterprise();
        partialUpdatedEnterprise.setId(enterprise.getId());

        partialUpdatedEnterprise
            .enterpriseName(UPDATED_ENTERPRISE_NAME)
            .country(UPDATED_COUNTRY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY);

        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
        Enterprise testEnterprise = enterpriseList.get(enterpriseList.size() - 1);
        assertThat(testEnterprise.getEnterpriseName()).isEqualTo(UPDATED_ENTERPRISE_NAME);
        assertThat(testEnterprise.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEnterprise.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testEnterprise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEnterprise.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testEnterprise.getCurrency()).isEqualTo(UPDATED_CURRENCY);
    }

    @Test
    void patchNonExistingEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enterprise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enterprise))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEnterprise() throws Exception {
        int databaseSizeBeforeUpdate = enterpriseRepository.findAll().size();
        enterprise.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enterprise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enterprise in the database
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        int databaseSizeBeforeDelete = enterpriseRepository.findAll().size();

        // Delete the enterprise
        restEnterpriseMockMvc
            .perform(delete(ENTITY_API_URL_ID, enterprise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enterprise> enterpriseList = enterpriseRepository.findAll();
        assertThat(enterpriseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
