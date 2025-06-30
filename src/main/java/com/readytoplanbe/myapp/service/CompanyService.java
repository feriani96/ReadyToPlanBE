package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.domain.Company;
import com.readytoplanbe.myapp.domain.enumeration.EntityType;
import com.readytoplanbe.myapp.repository.CompanyRepository;
import com.readytoplanbe.myapp.service.dto.CompanyDTO;
import com.readytoplanbe.myapp.service.mapper.CompanyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;
    private final  AIGenerationService aiGenerationService;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper, AIGenerationService aiGenerationService) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.aiGenerationService = aiGenerationService;
    }

    @Async
    public void generateAIDescription(String companyId, EntityType entityType, String prompt) {
        try {
            aiGenerationService.generateAndSave(companyId, entityType, prompt);
        } catch (Exception e) {
            log.error("Erreur asynchrone lors de la génération AI pour {} {}", entityType, companyId, e);
        }
    }

    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save ProductOrService : {}", companyDTO);

        Company company = companyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);

        try {
            String prompt = String.format(
                "Génère une description professionnelle du company ou service suivant pour un business plan :\n" +
                    "- EnterpriseName : %s\n" +
                    "- Description : %s\n" +
                    "- Amount : %.2f €\n" +
                    "- Country : %s\n" +
                    "Merci de rédiger une réponse claire, synthétique, et professionnelle.",
                company.getEnterpriseName(),
                company.getDescription(),
                company.getAmount(),
                company.getCountry()
            );

            aiGenerationService.generateAndSave(company.getId(), EntityType.COMPANY, prompt);
        } catch (Exception e) {
            log.error("Erreur lors de la génération AI, mais la company est sauvegardée", e);
            // On continue même si l'AI échoue
        }

        return companyMapper.toDto(company);
    }

    /**
     * Update a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyDTO update(CompanyDTO companyDTO) {
        log.debug("Request to update Company : {}", companyDTO);
        Company company = companyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);
        return companyMapper.toDto(company);
    }

    /**
     * Partially update a company.
     *
     * @param companyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyDTO> partialUpdate(CompanyDTO companyDTO) {
        log.debug("Request to partially update Company : {}", companyDTO);

        return companyRepository
            .findById(companyDTO.getId())
            .map(existingCompany -> {
                companyMapper.partialUpdate(existingCompany, companyDTO);

                return existingCompany;
            })
            .map(companyRepository::save)
            .map(companyMapper::toDto);
    }

    /**
     * Get all the companies.
     *
     * @return the list of entities.
     */
    public List<CompanyDTO> findAll() {
        log.debug("Request to get all Companies");
        return companyRepository.findAll().stream().map(companyMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<CompanyDTO> findOne(String id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id).map(companyMapper::toDto);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
