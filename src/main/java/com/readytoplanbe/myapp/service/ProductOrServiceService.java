package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.domain.Company;
import com.readytoplanbe.myapp.domain.ProductOrService;
import com.readytoplanbe.myapp.domain.enumeration.EntityType;
import com.readytoplanbe.myapp.repository.CompanyRepository;
import com.readytoplanbe.myapp.repository.ProductOrServiceRepository;
import com.readytoplanbe.myapp.service.dto.ProductOrServiceDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.readytoplanbe.myapp.service.mapper.ProductOrServiceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Interface for managing {@link com.readytoplanbe.myapp.domain.ProductOrService}.
 */
@Service
public class ProductOrServiceService {

    private final Logger log = LoggerFactory.getLogger(ProductOrServiceService.class);

    private final ProductOrServiceRepository productOrServiceRepository;

    private final ProductOrServiceMapper productOrServiceMapper;
    private  final AIGenerationService aiGenerationService;
    private final CompanyRepository companyRepository;
    public ProductOrServiceService(ProductOrServiceRepository productOrServiceRepository, ProductOrServiceMapper productOrServiceMapper, AIGenerationService aiGenerationService, CompanyRepository companyRepository) {
        this.productOrServiceRepository = productOrServiceRepository;
        this.productOrServiceMapper = productOrServiceMapper;
        this.aiGenerationService = aiGenerationService;
        this.companyRepository = companyRepository;
    }


    public ProductOrServiceDTO save(ProductOrServiceDTO productOrServiceDTO, String companyId) {
        log.debug("Request to save ProductOrService : {}", productOrServiceDTO);

        ProductOrService productOrService = productOrServiceMapper.toEntity(productOrServiceDTO);

        // 🔗 Lier la Company si l'ID est fourni
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        productOrService.setCompany(company);

        productOrService = productOrServiceRepository.save(productOrService);

        // 🧠 Génération IA protégée par un try/catch
        try {
            String prompt = String.format(
                "Génère une description professionnelle du produit ou service suivant pour un business plan :\n" +
                    "- Nom : %s\n" +
                    "- Description : %s\n" +
                    "- Prix unitaire : %.2f €\n" +
                    "- Ventes mensuelles estimées : %d\n" +
                    "- Durée (mois) : %d\n" +
                    "Merci de rédiger une réponse claire, synthétique, et professionnelle.",
                productOrService.getNameProductOrService(),
                productOrService.getProductDescription(),
                productOrService.getUnitPrice(),
                productOrService.getEstimatedMonthlySales(),
                productOrService.getDurationInMonths()
            );

            aiGenerationService.generateAndSave(productOrService.getId(), EntityType.PRODUCT, prompt);

        } catch (Exception e) {
            log.warn("⚠️ L'appel à Gemini a échoué (quota dépassé ou erreur technique).", e);
        }

        return productOrServiceMapper.toDto(productOrService);
    }

    public List<ProductOrServiceDTO> findByCompanyId(String companyId) {
        List<ProductOrService> products = productOrServiceRepository.findAllByCompanyId(companyId);
        return products.stream().map(productOrServiceMapper::toDto).collect(Collectors.toList());

    }

    /**
     * Update a productOrService.
     *
     * @param productOrServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductOrServiceDTO update(ProductOrServiceDTO productOrServiceDTO) {
        log.debug("Request to update ProductOrService : {}", productOrServiceDTO);
        ProductOrService productOrService = productOrServiceMapper.toEntity(productOrServiceDTO);
        productOrService = productOrServiceRepository.save(productOrService);
        return productOrServiceMapper.toDto(productOrService);
    }

    /**
     * Partially update a productOrService.
     *
     * @param productOrServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductOrServiceDTO> partialUpdate(ProductOrServiceDTO productOrServiceDTO) {
        log.debug("Request to partially update ProductOrService : {}", productOrServiceDTO);

        return productOrServiceRepository
            .findById(productOrServiceDTO.getId())
            .map(existingProductOrService -> {
                productOrServiceMapper.partialUpdate(existingProductOrService, productOrServiceDTO);

                return existingProductOrService;
            })
            .map(productOrServiceRepository::save)
            .map(productOrServiceMapper::toDto);
    }

    /**
     * Get all the productOrServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<ProductOrServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductOrServices");
        return productOrServiceRepository.findAll(pageable).map(productOrServiceMapper::toDto);
    }

    /**
     * Get one productOrService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ProductOrServiceDTO> findOne(String id) {
        log.debug("Request to get ProductOrService : {}", id);
        return productOrServiceRepository.findById(id).map(productOrServiceMapper::toDto);
    }

    /**
     * Delete the productOrService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete ProductOrService : {}", id);
        productOrServiceRepository.deleteById(id);
    }
}
