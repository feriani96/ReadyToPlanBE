package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.FinancialForecast;
import com.readytoplanbe.myapp.repository.FinancialForecastRepository;
import com.readytoplanbe.myapp.service.FinancialForecastService;
import com.readytoplanbe.myapp.service.dto.FinancialForecastDTO;
import com.readytoplanbe.myapp.service.mapper.FinancialForecastMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link FinancialForecast}.
 */
@Service
public class FinancialForecastServiceImpl implements FinancialForecastService {

    private final Logger log = LoggerFactory.getLogger(FinancialForecastServiceImpl.class);

    private final FinancialForecastRepository financialForecastRepository;

    private final FinancialForecastMapper financialForecastMapper;

    public FinancialForecastServiceImpl(
        FinancialForecastRepository financialForecastRepository,
        FinancialForecastMapper financialForecastMapper
    ) {
        this.financialForecastRepository = financialForecastRepository;
        this.financialForecastMapper = financialForecastMapper;
    }

    @Override
    public FinancialForecastDTO save(FinancialForecastDTO financialForecastDTO) {
        log.debug("Request to save FinancialForecast : {}", financialForecastDTO);
        FinancialForecast financialForecast = financialForecastMapper.toEntity(financialForecastDTO);
        financialForecast = financialForecastRepository.save(financialForecast);
        return financialForecastMapper.toDto(financialForecast);
    }

    @Override
    public FinancialForecastDTO update(FinancialForecastDTO financialForecastDTO) {
        log.debug("Request to update FinancialForecast : {}", financialForecastDTO);
        FinancialForecast financialForecast = financialForecastMapper.toEntity(financialForecastDTO);
        financialForecast = financialForecastRepository.save(financialForecast);
        return financialForecastMapper.toDto(financialForecast);
    }

    @Override
    public Optional<FinancialForecastDTO> partialUpdate(FinancialForecastDTO financialForecastDTO) {
        log.debug("Request to partially update FinancialForecast : {}", financialForecastDTO);

        return financialForecastRepository
            .findById(financialForecastDTO.getId())
            .map(existingFinancialForecast -> {
                financialForecastMapper.partialUpdate(existingFinancialForecast, financialForecastDTO);

                return existingFinancialForecast;
            })
            .map(financialForecastRepository::save)
            .map(financialForecastMapper::toDto);
    }

    @Override
    public Page<FinancialForecastDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FinancialForecasts");
        return financialForecastRepository.findAll(pageable).map(financialForecastMapper::toDto);
    }

    public Page<FinancialForecastDTO> findAllWithEagerRelationships(Pageable pageable) {
        return financialForecastRepository.findAllWithEagerRelationships(pageable).map(financialForecastMapper::toDto);
    }

    @Override
    public Optional<FinancialForecastDTO> findOne(String id) {
        log.debug("Request to get FinancialForecast : {}", id);
        return financialForecastRepository.findOneWithEagerRelationships(id).map(financialForecastMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete FinancialForecast : {}", id);
        financialForecastRepository.deleteById(id);
    }
}
