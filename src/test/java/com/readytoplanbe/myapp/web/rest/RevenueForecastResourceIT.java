package com.readytoplanbe.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.readytoplanbe.myapp.IntegrationTest;
import com.readytoplanbe.myapp.domain.RevenueForecast;
import com.readytoplanbe.myapp.domain.enumeration.Month;
import com.readytoplanbe.myapp.repository.RevenueForecastRepository;
import com.readytoplanbe.myapp.service.RevenueForecastService;
import com.readytoplanbe.myapp.service.dto.RevenueForecastDTO;
import com.readytoplanbe.myapp.service.mapper.RevenueForecastMapper;
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
 * Integration tests for the {@link RevenueForecastResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RevenueForecastResourceIT {

    private static final Month DEFAULT_MONTH = Month.JANUARY;
    private static final Month UPDATED_MONTH = Month.FEBRUARY;

    private static final Integer DEFAULT_YEAR = 1900;
    private static final Integer UPDATED_YEAR = 1901;

    private static final Double DEFAULT_UNITS_SOLD = 0D;
    private static final Double UPDATED_UNITS_SOLD = 1D;

    private static final Double DEFAULT_TOTAL_REVENUE = 0D;
    private static final Double UPDATED_TOTAL_REVENUE = 1D;

    private static final String ENTITY_API_URL = "/api/revenue-forecasts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RevenueForecastRepository revenueForecastRepository;

    @Mock
    private RevenueForecastRepository revenueForecastRepositoryMock;

    @Autowired
    private RevenueForecastMapper revenueForecastMapper;

    @Mock
    private RevenueForecastService revenueForecastServiceMock;

    @Autowired
    private MockMvc restRevenueForecastMockMvc;

    private RevenueForecast revenueForecast;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RevenueForecast createEntity() {
        RevenueForecast revenueForecast = new RevenueForecast()
            .month(DEFAULT_MONTH)
            .year(DEFAULT_YEAR)
            .unitsSold(DEFAULT_UNITS_SOLD)
            .totalRevenue(DEFAULT_TOTAL_REVENUE);
        return revenueForecast;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RevenueForecast createUpdatedEntity() {
        RevenueForecast revenueForecast = new RevenueForecast()
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .unitsSold(UPDATED_UNITS_SOLD)
            .totalRevenue(UPDATED_TOTAL_REVENUE);
        return revenueForecast;
    }

    @BeforeEach
    public void initTest() {
        revenueForecastRepository.deleteAll();
        revenueForecast = createEntity();
    }

    @Test
    void createRevenueForecast() throws Exception {
        int databaseSizeBeforeCreate = revenueForecastRepository.findAll().size();
        // Create the RevenueForecast
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);
        restRevenueForecastMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeCreate + 1);
        RevenueForecast testRevenueForecast = revenueForecastList.get(revenueForecastList.size() - 1);
        assertThat(testRevenueForecast.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testRevenueForecast.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testRevenueForecast.getUnitsSold()).isEqualTo(DEFAULT_UNITS_SOLD);
        assertThat(testRevenueForecast.getTotalRevenue()).isEqualTo(DEFAULT_TOTAL_REVENUE);
    }

    @Test
    void createRevenueForecastWithExistingId() throws Exception {
        // Create the RevenueForecast with an existing ID
        revenueForecast.setId("existing_id");
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        int databaseSizeBeforeCreate = revenueForecastRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevenueForecastMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = revenueForecastRepository.findAll().size();
        // set the field null
        revenueForecast.setMonth(null);

        // Create the RevenueForecast, which fails.
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        restRevenueForecastMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isBadRequest());

        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = revenueForecastRepository.findAll().size();
        // set the field null
        revenueForecast.setYear(null);

        // Create the RevenueForecast, which fails.
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        restRevenueForecastMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isBadRequest());

        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUnitsSoldIsRequired() throws Exception {
        int databaseSizeBeforeTest = revenueForecastRepository.findAll().size();
        // set the field null
        revenueForecast.setUnitsSold(null);

        // Create the RevenueForecast, which fails.
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        restRevenueForecastMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isBadRequest());

        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalRevenueIsRequired() throws Exception {
        int databaseSizeBeforeTest = revenueForecastRepository.findAll().size();
        // set the field null
        revenueForecast.setTotalRevenue(null);

        // Create the RevenueForecast, which fails.
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        restRevenueForecastMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isBadRequest());

        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRevenueForecasts() throws Exception {
        // Initialize the database
        revenueForecastRepository.save(revenueForecast);

        // Get all the revenueForecastList
        restRevenueForecastMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revenueForecast.getId())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].unitsSold").value(hasItem(DEFAULT_UNITS_SOLD.doubleValue())))
            .andExpect(jsonPath("$.[*].totalRevenue").value(hasItem(DEFAULT_TOTAL_REVENUE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRevenueForecastsWithEagerRelationshipsIsEnabled() throws Exception {
        when(revenueForecastServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRevenueForecastMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(revenueForecastServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRevenueForecastsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(revenueForecastServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRevenueForecastMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(revenueForecastRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getRevenueForecast() throws Exception {
        // Initialize the database
        revenueForecastRepository.save(revenueForecast);

        // Get the revenueForecast
        restRevenueForecastMockMvc
            .perform(get(ENTITY_API_URL_ID, revenueForecast.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(revenueForecast.getId()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.unitsSold").value(DEFAULT_UNITS_SOLD.doubleValue()))
            .andExpect(jsonPath("$.totalRevenue").value(DEFAULT_TOTAL_REVENUE.doubleValue()));
    }

    @Test
    void getNonExistingRevenueForecast() throws Exception {
        // Get the revenueForecast
        restRevenueForecastMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingRevenueForecast() throws Exception {
        // Initialize the database
        revenueForecastRepository.save(revenueForecast);

        int databaseSizeBeforeUpdate = revenueForecastRepository.findAll().size();

        // Update the revenueForecast
        RevenueForecast updatedRevenueForecast = revenueForecastRepository.findById(revenueForecast.getId()).get();
        updatedRevenueForecast.month(UPDATED_MONTH).year(UPDATED_YEAR).unitsSold(UPDATED_UNITS_SOLD).totalRevenue(UPDATED_TOTAL_REVENUE);
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(updatedRevenueForecast);

        restRevenueForecastMockMvc
            .perform(
                put(ENTITY_API_URL_ID, revenueForecastDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isOk());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeUpdate);
        RevenueForecast testRevenueForecast = revenueForecastList.get(revenueForecastList.size() - 1);
        assertThat(testRevenueForecast.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testRevenueForecast.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testRevenueForecast.getUnitsSold()).isEqualTo(UPDATED_UNITS_SOLD);
        assertThat(testRevenueForecast.getTotalRevenue()).isEqualTo(UPDATED_TOTAL_REVENUE);
    }

    @Test
    void putNonExistingRevenueForecast() throws Exception {
        int databaseSizeBeforeUpdate = revenueForecastRepository.findAll().size();
        revenueForecast.setId(UUID.randomUUID().toString());

        // Create the RevenueForecast
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRevenueForecastMockMvc
            .perform(
                put(ENTITY_API_URL_ID, revenueForecastDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRevenueForecast() throws Exception {
        int databaseSizeBeforeUpdate = revenueForecastRepository.findAll().size();
        revenueForecast.setId(UUID.randomUUID().toString());

        // Create the RevenueForecast
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevenueForecastMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRevenueForecast() throws Exception {
        int databaseSizeBeforeUpdate = revenueForecastRepository.findAll().size();
        revenueForecast.setId(UUID.randomUUID().toString());

        // Create the RevenueForecast
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevenueForecastMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRevenueForecastWithPatch() throws Exception {
        // Initialize the database
        revenueForecastRepository.save(revenueForecast);

        int databaseSizeBeforeUpdate = revenueForecastRepository.findAll().size();

        // Update the revenueForecast using partial update
        RevenueForecast partialUpdatedRevenueForecast = new RevenueForecast();
        partialUpdatedRevenueForecast.setId(revenueForecast.getId());

        partialUpdatedRevenueForecast.unitsSold(UPDATED_UNITS_SOLD).totalRevenue(UPDATED_TOTAL_REVENUE);

        restRevenueForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRevenueForecast.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRevenueForecast))
            )
            .andExpect(status().isOk());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeUpdate);
        RevenueForecast testRevenueForecast = revenueForecastList.get(revenueForecastList.size() - 1);
        assertThat(testRevenueForecast.getMonth()).isEqualTo(DEFAULT_MONTH);
        assertThat(testRevenueForecast.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testRevenueForecast.getUnitsSold()).isEqualTo(UPDATED_UNITS_SOLD);
        assertThat(testRevenueForecast.getTotalRevenue()).isEqualTo(UPDATED_TOTAL_REVENUE);
    }

    @Test
    void fullUpdateRevenueForecastWithPatch() throws Exception {
        // Initialize the database
        revenueForecastRepository.save(revenueForecast);

        int databaseSizeBeforeUpdate = revenueForecastRepository.findAll().size();

        // Update the revenueForecast using partial update
        RevenueForecast partialUpdatedRevenueForecast = new RevenueForecast();
        partialUpdatedRevenueForecast.setId(revenueForecast.getId());

        partialUpdatedRevenueForecast
            .month(UPDATED_MONTH)
            .year(UPDATED_YEAR)
            .unitsSold(UPDATED_UNITS_SOLD)
            .totalRevenue(UPDATED_TOTAL_REVENUE);

        restRevenueForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRevenueForecast.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRevenueForecast))
            )
            .andExpect(status().isOk());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeUpdate);
        RevenueForecast testRevenueForecast = revenueForecastList.get(revenueForecastList.size() - 1);
        assertThat(testRevenueForecast.getMonth()).isEqualTo(UPDATED_MONTH);
        assertThat(testRevenueForecast.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testRevenueForecast.getUnitsSold()).isEqualTo(UPDATED_UNITS_SOLD);
        assertThat(testRevenueForecast.getTotalRevenue()).isEqualTo(UPDATED_TOTAL_REVENUE);
    }

    @Test
    void patchNonExistingRevenueForecast() throws Exception {
        int databaseSizeBeforeUpdate = revenueForecastRepository.findAll().size();
        revenueForecast.setId(UUID.randomUUID().toString());

        // Create the RevenueForecast
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRevenueForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, revenueForecastDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRevenueForecast() throws Exception {
        int databaseSizeBeforeUpdate = revenueForecastRepository.findAll().size();
        revenueForecast.setId(UUID.randomUUID().toString());

        // Create the RevenueForecast
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevenueForecastMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRevenueForecast() throws Exception {
        int databaseSizeBeforeUpdate = revenueForecastRepository.findAll().size();
        revenueForecast.setId(UUID.randomUUID().toString());

        // Create the RevenueForecast
        RevenueForecastDTO revenueForecastDTO = revenueForecastMapper.toDto(revenueForecast);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRevenueForecastMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(revenueForecastDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RevenueForecast in the database
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRevenueForecast() throws Exception {
        // Initialize the database
        revenueForecastRepository.save(revenueForecast);

        int databaseSizeBeforeDelete = revenueForecastRepository.findAll().size();

        // Delete the revenueForecast
        restRevenueForecastMockMvc
            .perform(delete(ENTITY_API_URL_ID, revenueForecast.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RevenueForecast> revenueForecastList = revenueForecastRepository.findAll();
        assertThat(revenueForecastList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
