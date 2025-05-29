package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.ManualBusinessPlan;
import com.readytoplanbe.myapp.repository.ManualBusinessPlanRepository;
import com.readytoplanbe.myapp.service.ManualBusinessPlanService;
import com.readytoplanbe.myapp.service.dto.ManualBusinessPlanDTO;
import com.readytoplanbe.myapp.service.mapper.ManualBusinessPlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ManualBusinessPlan}.
 */
@Service
public class ManualBusinessPlanServiceImpl implements ManualBusinessPlanService {

    private final Logger log = LoggerFactory.getLogger(ManualBusinessPlanServiceImpl.class);

    private final ManualBusinessPlanRepository manualBusinessPlanRepository;

    private final ManualBusinessPlanMapper manualBusinessPlanMapper;

    public ManualBusinessPlanServiceImpl(
        ManualBusinessPlanRepository manualBusinessPlanRepository,
        ManualBusinessPlanMapper manualBusinessPlanMapper
    ) {
        this.manualBusinessPlanRepository = manualBusinessPlanRepository;
        this.manualBusinessPlanMapper = manualBusinessPlanMapper;
    }

    @Override
    public ManualBusinessPlanDTO save(ManualBusinessPlanDTO manualBusinessPlanDTO) {
        log.debug("Request to save ManualBusinessPlan : {}", manualBusinessPlanDTO);
        ManualBusinessPlan manualBusinessPlan = manualBusinessPlanMapper.toEntity(manualBusinessPlanDTO);
        manualBusinessPlan = manualBusinessPlanRepository.save(manualBusinessPlan);
        return manualBusinessPlanMapper.toDto(manualBusinessPlan);
    }

    @Override
    public ManualBusinessPlanDTO update(ManualBusinessPlanDTO manualBusinessPlanDTO) {
        log.debug("Request to update ManualBusinessPlan : {}", manualBusinessPlanDTO);
        ManualBusinessPlan manualBusinessPlan = manualBusinessPlanMapper.toEntity(manualBusinessPlanDTO);
        manualBusinessPlan = manualBusinessPlanRepository.save(manualBusinessPlan);
        return manualBusinessPlanMapper.toDto(manualBusinessPlan);
    }

    @Override
    public Optional<ManualBusinessPlanDTO> partialUpdate(ManualBusinessPlanDTO manualBusinessPlanDTO) {
        log.debug("Request to partially update ManualBusinessPlan : {}", manualBusinessPlanDTO);

        return manualBusinessPlanRepository
            .findById(manualBusinessPlanDTO.getId())
            .map(existingManualBusinessPlan -> {
                manualBusinessPlanMapper.partialUpdate(existingManualBusinessPlan, manualBusinessPlanDTO);

                return existingManualBusinessPlan;
            })
            .map(manualBusinessPlanRepository::save)
            .map(manualBusinessPlanMapper::toDto);
    }

    @Override
    public Page<ManualBusinessPlanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ManualBusinessPlans");
        return manualBusinessPlanRepository.findAll(pageable).map(manualBusinessPlanMapper::toDto);
    }

    @Override
    public Optional<ManualBusinessPlanDTO> findOne(String id) {
        log.debug("Request to get ManualBusinessPlan : {}", id);
        return manualBusinessPlanRepository.findById(id).map(manualBusinessPlanMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ManualBusinessPlan : {}", id);
        manualBusinessPlanRepository.deleteById(id);
    }
}
