package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.BusinessPlan;
import com.readytoplanbe.myapp.domain.enumeration.ExportFormat;
import com.readytoplanbe.myapp.repository.BusinessPlanRepository;
import com.readytoplanbe.myapp.service.BusinessPlanService;
import com.readytoplanbe.myapp.service.dto.BusinessPlanDTO;
import com.readytoplanbe.myapp.service.mapper.BusinessPlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public BusinessPlanServiceImpl(BusinessPlanRepository businessPlanRepository, BusinessPlanMapper businessPlanMapper) {
        this.businessPlanRepository = businessPlanRepository;
        this.businessPlanMapper = businessPlanMapper;
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

    @Override
    public byte[] exportPresentation(String businessPlanId, ExportFormat format) {
        log.debug("Request to export BusinessPlan presentation: {}, format: {}", businessPlanId, format);

        // 1. Récupérer le business plan
        BusinessPlan businessPlan = businessPlanRepository.findById(businessPlanId)
            .orElseThrow(() -> new RuntimeException("BusinessPlan not found with id: " + businessPlanId));

        // 2. Générer le contenu selon le format
        switch(format) {
            case PDF:
                return generatePdfPresentation(businessPlan);
            case PPTX:
                return generatePptxPresentation(businessPlan);
            default:
                throw new UnsupportedOperationException("Unsupported export format: " + format);
        }
    }

    private byte[] generatePdfPresentation(BusinessPlan businessPlan) {
        throw new UnsupportedOperationException("PDF generation not implemented yet");
    }

    private byte[] generatePptxPresentation(BusinessPlan businessPlan) {
        throw new UnsupportedOperationException("PPTX generation not implemented yet");
    }
}
