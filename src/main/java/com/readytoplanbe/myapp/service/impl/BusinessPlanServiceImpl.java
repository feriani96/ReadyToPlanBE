package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.BusinessPlan;
import com.readytoplanbe.myapp.repository.BusinessPlanRepository;
import com.readytoplanbe.myapp.service.BusinessPlanGeneratorService;
import com.readytoplanbe.myapp.service.BusinessPlanService;
import com.readytoplanbe.myapp.service.dto.BusinessPlanDTO;
import com.readytoplanbe.myapp.service.dto.BusinessPlanInputDTO;
import com.readytoplanbe.myapp.service.mapper.BusinessPlanMapper;
import java.util.Optional;

import com.readytoplanbe.myapp.web.rest.errors.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link BusinessPlan}.
 */
@Service
public class BusinessPlanServiceImpl implements BusinessPlanService {

    private final Logger log = LoggerFactory.getLogger(BusinessPlanServiceImpl.class);

    private final BusinessPlanRepository businessPlanRepository;

    private final BusinessPlanMapper businessPlanMapper;

    private final BusinessPlanGeneratorService businessPlanGeneratorService;

    @Value("${gemini.api.key}")
    private String geminiApiKey;


    public BusinessPlanServiceImpl(BusinessPlanRepository businessPlanRepository,
                                   BusinessPlanMapper businessPlanMapper,
                                   BusinessPlanGeneratorService businessPlanGeneratorService) {
        this.businessPlanRepository = businessPlanRepository;
        this.businessPlanMapper = businessPlanMapper;
        this.businessPlanGeneratorService = businessPlanGeneratorService;
    }

    @Override
    public BusinessPlanDTO generateAndSaveFromInput(BusinessPlanInputDTO inputDTO) {
        String generatedPresentation = businessPlanGeneratorService.generatePresentation(inputDTO, geminiApiKey);

        BusinessPlanDTO dto = new BusinessPlanDTO();
        dto.setCompanyName(inputDTO.getCompanyName());
        dto.setCompanyDescription(inputDTO.getCompanyDescription());
        dto.setCompanyStartDate(inputDTO.getCompanyStartDate());
        dto.setCountry(inputDTO.getCountry());
        dto.setLanguages(inputDTO.getLanguages());
        dto.setAnticipatedProjectSize(inputDTO.getAnticipatedProjectSize());
        dto.setCurrency(inputDTO.getCurrency());
        dto.setGeneratedPresentation(generatedPresentation);

        return save(dto);
    }

    @Override
    public BusinessPlanDTO updatePresentation(String id, String presentation) {
        Optional<BusinessPlanDTO> optionalPlan = findOne(id);
        if (optionalPlan.isPresent()) {
            BusinessPlanDTO plan = optionalPlan.get();
            plan.setGeneratedPresentation(presentation);
            return save(plan);
        }
        throw new EntityNotFoundException("BusinessPlan not found");
    }

    @Override
    public BusinessPlanDTO save(BusinessPlanDTO businessPlanDTO) {
        log.debug("Request to save BusinessPlan : {}", businessPlanDTO);
        BusinessPlan businessPlan = businessPlanMapper.toEntity(businessPlanDTO);
        businessPlan = businessPlanRepository.save(businessPlan);
        return businessPlanMapper.toDto(businessPlan);
    }

    @Override
    public BusinessPlanDTO update(BusinessPlanDTO businessPlanDTO) {
        log.debug("Request to update BusinessPlan : {}", businessPlanDTO);
        BusinessPlan businessPlan = businessPlanMapper.toEntity(businessPlanDTO);
        businessPlan = businessPlanRepository.save(businessPlan);
        return businessPlanMapper.toDto(businessPlan);
    }

    @Override
    public Optional<BusinessPlanDTO> partialUpdate(BusinessPlanDTO businessPlanDTO) {
        log.debug("Request to partially update BusinessPlan : {}", businessPlanDTO);

        return businessPlanRepository
            .findById(businessPlanDTO.getId())
            .map(existingBusinessPlan -> {
                businessPlanMapper.partialUpdate(existingBusinessPlan, businessPlanDTO);

                return existingBusinessPlan;
            })
            .map(businessPlanRepository::save)
            .map(businessPlanMapper::toDto);
    }

    @Override
    public Page<BusinessPlanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessPlans");
        return businessPlanRepository.findAll(pageable).map(businessPlanMapper::toDto);
    }

    @Override
    public Optional<BusinessPlanDTO> findOne(String id) {
        log.debug("Request to get BusinessPlan : {}", id);
        return businessPlanRepository.findById(id).map(businessPlanMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete BusinessPlan : {}", id);
        businessPlanRepository.deleteById(id);
    }
}
