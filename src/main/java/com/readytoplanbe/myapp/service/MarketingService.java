package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.domain.Company;
import com.readytoplanbe.myapp.domain.Marketing;
import com.readytoplanbe.myapp.domain.enumeration.EntityType;
import com.readytoplanbe.myapp.repository.CompanyRepository;
import com.readytoplanbe.myapp.repository.MarketingRepository;
import com.readytoplanbe.myapp.service.dto.MarketingDTO;
import com.readytoplanbe.myapp.service.mapper.MarketingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Marketing}.
 */
@Service
public class MarketingService {

    private final Logger log = LoggerFactory.getLogger(MarketingService.class);

    private final MarketingRepository marketingRepository;

    private final MarketingMapper marketingMapper;
    private final CompanyRepository companyRepository;
    private final AIGenerationService aiGenerationService;

    public MarketingService(MarketingRepository marketingRepository, MarketingMapper marketingMapper, CompanyRepository companyRepository, AIGenerationService aiGenerationService) {
        this.marketingRepository = marketingRepository;
        this.marketingMapper = marketingMapper;
        this.companyRepository = companyRepository;
        this.aiGenerationService = aiGenerationService;
    }

    public MarketingDTO save(MarketingDTO marketingDTO, String companyId) {
        log.debug("Request to save Team : {}", marketingDTO);
        Marketing marketing = marketingMapper.toEntity(marketingDTO);
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        marketing.setCompany(company);
        marketing = marketingRepository.save(marketing);
        String prompt = "Donne une description professionnelle de cette membre : " + marketing.getMarketing_channel() +marketing.getSales_target()+ marketing.getDistribution_Channel();
        aiGenerationService.generateAndSave(
            marketing.getId(),
            EntityType.MARKETING,
            prompt// Assure-toi que ce champ existe bien
        );
        return marketingMapper.toDto(marketing);
    }
    public List<MarketingDTO> findByCompanyId(String companyId) {
        List<Marketing> marketings = marketingRepository.findAllByCompanyId(companyId);
        return marketings.stream().map(marketingMapper::toDto).collect(Collectors.toList());

    }

    /**
     * Update a marketing.
     *
     * @param marketingDTO the entity to save.
     * @return the persisted entity.
     */
    public MarketingDTO update(MarketingDTO marketingDTO) {
        log.debug("Request to update Marketing : {}", marketingDTO);
        Marketing marketing = marketingMapper.toEntity(marketingDTO);
        marketing = marketingRepository.save(marketing);
        return marketingMapper.toDto(marketing);
    }

    /**
     * Partially update a marketing.
     *
     * @param marketingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MarketingDTO> partialUpdate(MarketingDTO marketingDTO) {
        log.debug("Request to partially update Marketing : {}", marketingDTO);

        return marketingRepository
            .findById(marketingDTO.getId())
            .map(existingMarketing -> {
                marketingMapper.partialUpdate(existingMarketing, marketingDTO);

                return existingMarketing;
            })
            .map(marketingRepository::save)
            .map(marketingMapper::toDto);
    }

    /**
     * Get all the marketings.
     *
     * @return the list of entities.
     */
    public List<MarketingDTO> findAll() {
        log.debug("Request to get all Marketings");
        return marketingRepository.findAll().stream().map(marketingMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one marketing by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<MarketingDTO> findOne(String id) {
        log.debug("Request to get Marketing : {}", id);
        return marketingRepository.findById(id).map(marketingMapper::toDto);
    }

    /**
     * Delete the marketing by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Marketing : {}", id);
        marketingRepository.deleteById(id);
    }
}
