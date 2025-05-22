package com.readytoplanbe.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.readytoplanbe.myapp.IntegrationTest;
import com.readytoplanbe.myapp.domain.FinancialForecast;
import com.readytoplanbe.myapp.repository.FinancialForecastRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link FinancialForecastResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FinancialForecastResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DURATION_IN_MONTHS = 1;
    private static final Integer UPDATED_DURATION_IN_MONTHS = 2;

    private static final String ENTITY_API_URL = "/api/financial-forecasts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private FinancialForecastRepository financialForecastRepository;

    @Mock
    private FinancialForecastRepository financialForecastRepositoryMock;

    @Autowired
    private MockMvc restFinancialForecastMockMvc;

    private FinancialForecast financialForecast;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialForecast createEntity() {
        FinancialForecast financialForecast = new FinancialForecast()
            .startDate(DEFAULT_START_DATE)
            .durationInMonths(DEFAULT_DURATION_IN_MONTHS);
        return financialForecast;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialForecast createUpdatedEntity() {
        FinancialForecast financialForecast = new FinancialForecast()
            .startDate(UPDATED_START_DATE)
            .durationInMonths(UPDATED_DURATION_IN_MONTHS);
        return financialForecast;
    }

    @BeforeEach
    public void initTest() {
        financialForecastRepository.deleteAll();
        financialForecast = createEntity();
    }

    @Test
    void createFinancialForecast() throws Exception {
        int databaseSizeBeforeCreate = financialForecastRepository.findAll().size();
        // Create the FinancialForecast
        restFinancialForecastMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(financialForecast))
            )
            .andExpect(status().isCreated());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeCreate + 1);
        FinancialForecast testFinancialForecast = financialForecastList.get(financialForecastList.size() - 1);
        assertThat(testFinancialForecast.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFinancialForecast.getDurationInMonths()).isEqualTo(DEFAULT_DURATION_IN_MONTHS);
    }

    @Test
    void createFinancialForecastWithExistingId() throws Exception {
        // Create the FinancialForecast with an existing ID
        financialForecast.setId("existing_id");

        int databaseSizeBeforeCreate = financialForecastRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinancialForecastMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(financialForecast))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = financialForecastRepository.findAll().size();
        // set the field null
        financialForecast.setStartDate(null);

        // Create the FinancialForecast, which fails.

        restFinancialForecastMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(financialForecast))
            )
            .andExpect(status().isBadRequest());

        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFinancialForecasts() throws Exception {
        // Initialize the database
        financialForecastRepository.save(financialForecast);

        // Get all the financialForecastList
        restFinancialForecastMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialForecast.getId())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].durationInMonths").value(hasItem(DEFAULT_DURATION_IN_MONTHS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinancialForecastsWithEagerRelationshipsIsEnabled() throws Exception {
        when(financialForecastRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFinancialForecastMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(financialForecastRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinancialForecastsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(financialForecastRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFinancialForecastMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(financialForecastRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getFinancialForecast() throws Exception {
        // Initialize the database
        financialForecastRepository.save(financialForecast);

        // Get the financialForecast
        restFinancialForecastMockMvc
            .perform(get(ENTITY_API_URL_ID, financialForecast.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(financialForecast.getId()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.durationInMonths").value(DEFAULT_DURATION_IN_MONTHS));
    }

    @Test
    void getNonExistingFinancialForecast() throws Exception {
        // Get the financialForecast
        restFinancialForecastMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFinancialForecast() throws Exception {
        // Initialize the database
        financialForecastRepository.save(financialForecast);

        int databaseSizeBeforeUpdate = financialForecastRepository.findAll().size();

        // Update the financialForecast
        FinancialForecast updatedFinancialForecast = financialForecastRepository.findById(financialForecast.getId()).get();
        updatedFinancialForecast.startDate(UPDATED_START_DATE).durationInMonths(UPDATED_DURATION_IN_MONTHS);

        restFinancialForecastMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFinancialForecast.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFinancialForecast))
            )
            .andExpect(status().isOk());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeUpdate);
        FinancialForecast testFinancialForecast = financialForecastList.get(financialForecastList.size() - 1);
        assertThat(testFinancialForecast.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFinancialForecast.getDurationInMonths()).isEqualTo(UPDATED_DURATION_IN_MONTHS);
    }

    @Test
    void putNonExistingFinancialForecast() throws Exception {
        int databaseSizeBeforeUpdate = financialForecastRepository.findAll().size();
        financialForecast.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialForecastMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialForecast.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialForecast))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFinancialForecast() throws Exception {
        int databaseSizeBeforeUpdate = financialForecastRepository.findAll().size();
        financialForecast.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialForecastMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(financialForecast))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFinancialForecast() throws Exception {
        int databaseSizeBeforeUpdate = financialForecastRepository.findAll().size();
        financialForecast.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialForecastMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(financialForecast))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFinancialForecastWithPatch() throws Exception {
        // Initialize the database
        financialForecastRepository.save(financialForecast);

        int databaseSizeBeforeUpdate = financialForecastRepository.findAll().size();

        // Update the financialForecast using partial update
        FinancialForecast partialUpdatedFinancialForecast = new FinancialForecast();
        partialUpdatedFinancialForecast.setId(financialForecast.getId());

        restFinancialForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialForecast.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinancialForecast))
            )
            .andExpect(status().isOk());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeUpdate);
        FinancialForecast testFinancialForecast = financialForecastList.get(financialForecastList.size() - 1);
        assertThat(testFinancialForecast.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFinancialForecast.getDurationInMonths()).isEqualTo(DEFAULT_DURATION_IN_MONTHS);
    }

    @Test
    void fullUpdateFinancialForecastWithPatch() throws Exception {
        // Initialize the database
        financialForecastRepository.save(financialForecast);

        int databaseSizeBeforeUpdate = financialForecastRepository.findAll().size();

        // Update the financialForecast using partial update
        FinancialForecast partialUpdatedFinancialForecast = new FinancialForecast();
        partialUpdatedFinancialForecast.setId(financialForecast.getId());

        partialUpdatedFinancialForecast.startDate(UPDATED_START_DATE).durationInMonths(UPDATED_DURATION_IN_MONTHS);

        restFinancialForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialForecast.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFinancialForecast))
            )
            .andExpect(status().isOk());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeUpdate);
        FinancialForecast testFinancialForecast = financialForecastList.get(financialForecastList.size() - 1);
        assertThat(testFinancialForecast.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFinancialForecast.getDurationInMonths()).isEqualTo(UPDATED_DURATION_IN_MONTHS);
    }

    @Test
    void patchNonExistingFinancialForecast() throws Exception {
        int databaseSizeBeforeUpdate = financialForecastRepository.findAll().size();
        financialForecast.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, financialForecast.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(financialForecast))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFinancialForecast() throws Exception {
        int databaseSizeBeforeUpdate = financialForecastRepository.findAll().size();
        financialForecast.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(financialForecast))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFinancialForecast() throws Exception {
        int databaseSizeBeforeUpdate = financialForecastRepository.findAll().size();
        financialForecast.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialForecastMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(financialForecast))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialForecast in the database
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFinancialForecast() throws Exception {
        // Initialize the database
        financialForecastRepository.save(financialForecast);

        int databaseSizeBeforeDelete = financialForecastRepository.findAll().size();

        // Delete the financialForecast
        restFinancialForecastMockMvc
            .perform(delete(ENTITY_API_URL_ID, financialForecast.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FinancialForecast> financialForecastList = financialForecastRepository.findAll();
        assertThat(financialForecastList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
