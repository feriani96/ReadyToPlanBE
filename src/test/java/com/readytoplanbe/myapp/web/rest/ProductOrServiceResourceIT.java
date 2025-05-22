package com.readytoplanbe.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.readytoplanbe.myapp.IntegrationTest;
import com.readytoplanbe.myapp.domain.ProductOrService;
import com.readytoplanbe.myapp.repository.ProductOrServiceRepository;
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
 * Integration tests for the {@link ProductOrServiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductOrServiceResourceIT {

    private static final String DEFAULT_NAME_PRODUCT_OR_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_PRODUCT_OR_SERVICE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_UNIT_PRICE = 0D;
    private static final Double UPDATED_UNIT_PRICE = 1D;

    private static final Integer DEFAULT_ESTIMATED_MONTHLY_SALES = 1;
    private static final Integer UPDATED_ESTIMATED_MONTHLY_SALES = 2;

    private static final Integer DEFAULT_DURATION_IN_MONTHS = 1;
    private static final Integer UPDATED_DURATION_IN_MONTHS = 2;

    private static final String ENTITY_API_URL = "/api/product-or-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductOrServiceRepository productOrServiceRepository;

    @Mock
    private ProductOrServiceRepository productOrServiceRepositoryMock;

    @Autowired
    private MockMvc restProductOrServiceMockMvc;

    private ProductOrService productOrService;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrService createEntity() {
        ProductOrService productOrService = new ProductOrService()
            .nameProductOrService(DEFAULT_NAME_PRODUCT_OR_SERVICE)
            .productDescription(DEFAULT_PRODUCT_DESCRIPTION)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .estimatedMonthlySales(DEFAULT_ESTIMATED_MONTHLY_SALES)
            .durationInMonths(DEFAULT_DURATION_IN_MONTHS);
        return productOrService;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrService createUpdatedEntity() {
        ProductOrService productOrService = new ProductOrService()
            .nameProductOrService(UPDATED_NAME_PRODUCT_OR_SERVICE)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .estimatedMonthlySales(UPDATED_ESTIMATED_MONTHLY_SALES)
            .durationInMonths(UPDATED_DURATION_IN_MONTHS);
        return productOrService;
    }

    @BeforeEach
    public void initTest() {
        productOrServiceRepository.deleteAll();
        productOrService = createEntity();
    }

    @Test
    void createProductOrService() throws Exception {
        int databaseSizeBeforeCreate = productOrServiceRepository.findAll().size();
        // Create the ProductOrService
        restProductOrServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isCreated());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOrService testProductOrService = productOrServiceList.get(productOrServiceList.size() - 1);
        assertThat(testProductOrService.getNameProductOrService()).isEqualTo(DEFAULT_NAME_PRODUCT_OR_SERVICE);
        assertThat(testProductOrService.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProductOrService.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testProductOrService.getEstimatedMonthlySales()).isEqualTo(DEFAULT_ESTIMATED_MONTHLY_SALES);
        assertThat(testProductOrService.getDurationInMonths()).isEqualTo(DEFAULT_DURATION_IN_MONTHS);
    }

    @Test
    void createProductOrServiceWithExistingId() throws Exception {
        // Create the ProductOrService with an existing ID
        productOrService.setId("existing_id");

        int databaseSizeBeforeCreate = productOrServiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOrServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameProductOrServiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrServiceRepository.findAll().size();
        // set the field null
        productOrService.setNameProductOrService(null);

        // Create the ProductOrService, which fails.

        restProductOrServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isBadRequest());

        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkProductDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrServiceRepository.findAll().size();
        // set the field null
        productOrService.setProductDescription(null);

        // Create the ProductOrService, which fails.

        restProductOrServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isBadRequest());

        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrServiceRepository.findAll().size();
        // set the field null
        productOrService.setUnitPrice(null);

        // Create the ProductOrService, which fails.

        restProductOrServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isBadRequest());

        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDurationInMonthsIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrServiceRepository.findAll().size();
        // set the field null
        productOrService.setDurationInMonths(null);

        // Create the ProductOrService, which fails.

        restProductOrServiceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isBadRequest());

        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProductOrServices() throws Exception {
        // Initialize the database
        productOrServiceRepository.save(productOrService);

        // Get all the productOrServiceList
        restProductOrServiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOrService.getId())))
            .andExpect(jsonPath("$.[*].nameProductOrService").value(hasItem(DEFAULT_NAME_PRODUCT_OR_SERVICE)))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].estimatedMonthlySales").value(hasItem(DEFAULT_ESTIMATED_MONTHLY_SALES)))
            .andExpect(jsonPath("$.[*].durationInMonths").value(hasItem(DEFAULT_DURATION_IN_MONTHS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductOrServicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productOrServiceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductOrServiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productOrServiceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductOrServicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productOrServiceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductOrServiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productOrServiceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getProductOrService() throws Exception {
        // Initialize the database
        productOrServiceRepository.save(productOrService);

        // Get the productOrService
        restProductOrServiceMockMvc
            .perform(get(ENTITY_API_URL_ID, productOrService.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productOrService.getId()))
            .andExpect(jsonPath("$.nameProductOrService").value(DEFAULT_NAME_PRODUCT_OR_SERVICE))
            .andExpect(jsonPath("$.productDescription").value(DEFAULT_PRODUCT_DESCRIPTION))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.estimatedMonthlySales").value(DEFAULT_ESTIMATED_MONTHLY_SALES))
            .andExpect(jsonPath("$.durationInMonths").value(DEFAULT_DURATION_IN_MONTHS));
    }

    @Test
    void getNonExistingProductOrService() throws Exception {
        // Get the productOrService
        restProductOrServiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingProductOrService() throws Exception {
        // Initialize the database
        productOrServiceRepository.save(productOrService);

        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();

        // Update the productOrService
        ProductOrService updatedProductOrService = productOrServiceRepository.findById(productOrService.getId()).get();
        updatedProductOrService
            .nameProductOrService(UPDATED_NAME_PRODUCT_OR_SERVICE)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .estimatedMonthlySales(UPDATED_ESTIMATED_MONTHLY_SALES)
            .durationInMonths(UPDATED_DURATION_IN_MONTHS);

        restProductOrServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductOrService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductOrService))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
        ProductOrService testProductOrService = productOrServiceList.get(productOrServiceList.size() - 1);
        assertThat(testProductOrService.getNameProductOrService()).isEqualTo(UPDATED_NAME_PRODUCT_OR_SERVICE);
        assertThat(testProductOrService.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProductOrService.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testProductOrService.getEstimatedMonthlySales()).isEqualTo(UPDATED_ESTIMATED_MONTHLY_SALES);
        assertThat(testProductOrService.getDurationInMonths()).isEqualTo(UPDATED_DURATION_IN_MONTHS);
    }

    @Test
    void putNonExistingProductOrService() throws Exception {
        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();
        productOrService.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productOrService.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProductOrService() throws Exception {
        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();
        productOrService.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrServiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProductOrService() throws Exception {
        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();
        productOrService.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrServiceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductOrServiceWithPatch() throws Exception {
        // Initialize the database
        productOrServiceRepository.save(productOrService);

        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();

        // Update the productOrService using partial update
        ProductOrService partialUpdatedProductOrService = new ProductOrService();
        partialUpdatedProductOrService.setId(productOrService.getId());

        partialUpdatedProductOrService.estimatedMonthlySales(UPDATED_ESTIMATED_MONTHLY_SALES).durationInMonths(UPDATED_DURATION_IN_MONTHS);

        restProductOrServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOrService))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
        ProductOrService testProductOrService = productOrServiceList.get(productOrServiceList.size() - 1);
        assertThat(testProductOrService.getNameProductOrService()).isEqualTo(DEFAULT_NAME_PRODUCT_OR_SERVICE);
        assertThat(testProductOrService.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProductOrService.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testProductOrService.getEstimatedMonthlySales()).isEqualTo(UPDATED_ESTIMATED_MONTHLY_SALES);
        assertThat(testProductOrService.getDurationInMonths()).isEqualTo(UPDATED_DURATION_IN_MONTHS);
    }

    @Test
    void fullUpdateProductOrServiceWithPatch() throws Exception {
        // Initialize the database
        productOrServiceRepository.save(productOrService);

        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();

        // Update the productOrService using partial update
        ProductOrService partialUpdatedProductOrService = new ProductOrService();
        partialUpdatedProductOrService.setId(productOrService.getId());

        partialUpdatedProductOrService
            .nameProductOrService(UPDATED_NAME_PRODUCT_OR_SERVICE)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .unitPrice(UPDATED_UNIT_PRICE)
            .estimatedMonthlySales(UPDATED_ESTIMATED_MONTHLY_SALES)
            .durationInMonths(UPDATED_DURATION_IN_MONTHS);

        restProductOrServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductOrService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOrService))
            )
            .andExpect(status().isOk());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
        ProductOrService testProductOrService = productOrServiceList.get(productOrServiceList.size() - 1);
        assertThat(testProductOrService.getNameProductOrService()).isEqualTo(UPDATED_NAME_PRODUCT_OR_SERVICE);
        assertThat(testProductOrService.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProductOrService.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testProductOrService.getEstimatedMonthlySales()).isEqualTo(UPDATED_ESTIMATED_MONTHLY_SALES);
        assertThat(testProductOrService.getDurationInMonths()).isEqualTo(UPDATED_DURATION_IN_MONTHS);
    }

    @Test
    void patchNonExistingProductOrService() throws Exception {
        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();
        productOrService.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOrServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productOrService.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProductOrService() throws Exception {
        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();
        productOrService.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrServiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProductOrService() throws Exception {
        int databaseSizeBeforeUpdate = productOrServiceRepository.findAll().size();
        productOrService.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductOrServiceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productOrService))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductOrService in the database
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProductOrService() throws Exception {
        // Initialize the database
        productOrServiceRepository.save(productOrService);

        int databaseSizeBeforeDelete = productOrServiceRepository.findAll().size();

        // Delete the productOrService
        restProductOrServiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, productOrService.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductOrService> productOrServiceList = productOrServiceRepository.findAll();
        assertThat(productOrServiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
