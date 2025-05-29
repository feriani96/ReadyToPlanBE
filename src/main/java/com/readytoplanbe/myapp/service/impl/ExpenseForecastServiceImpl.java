package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.ExpenseForecast;
import com.readytoplanbe.myapp.repository.ExpenseForecastRepository;
import com.readytoplanbe.myapp.service.ExpenseForecastService;
import com.readytoplanbe.myapp.service.dto.ExpenseForecastDTO;
import com.readytoplanbe.myapp.service.mapper.ExpenseForecastMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ExpenseForecast}.
 */
@Service
public class ExpenseForecastServiceImpl implements ExpenseForecastService {

    private final Logger log = LoggerFactory.getLogger(ExpenseForecastServiceImpl.class);

    private final ExpenseForecastRepository expenseForecastRepository;

    private final ExpenseForecastMapper expenseForecastMapper;

    public ExpenseForecastServiceImpl(ExpenseForecastRepository expenseForecastRepository, ExpenseForecastMapper expenseForecastMapper) {
        this.expenseForecastRepository = expenseForecastRepository;
        this.expenseForecastMapper = expenseForecastMapper;
    }

    @Override
    public ExpenseForecastDTO save(ExpenseForecastDTO expenseForecastDTO) {
        log.debug("Request to save ExpenseForecast : {}", expenseForecastDTO);
        ExpenseForecast expenseForecast = expenseForecastMapper.toEntity(expenseForecastDTO);
        expenseForecast = expenseForecastRepository.save(expenseForecast);
        return expenseForecastMapper.toDto(expenseForecast);
    }

    @Override
    public ExpenseForecastDTO update(ExpenseForecastDTO expenseForecastDTO) {
        log.debug("Request to update ExpenseForecast : {}", expenseForecastDTO);
        ExpenseForecast expenseForecast = expenseForecastMapper.toEntity(expenseForecastDTO);
        expenseForecast = expenseForecastRepository.save(expenseForecast);
        return expenseForecastMapper.toDto(expenseForecast);
    }

    @Override
    public Optional<ExpenseForecastDTO> partialUpdate(ExpenseForecastDTO expenseForecastDTO) {
        log.debug("Request to partially update ExpenseForecast : {}", expenseForecastDTO);

        return expenseForecastRepository
            .findById(expenseForecastDTO.getId())
            .map(existingExpenseForecast -> {
                expenseForecastMapper.partialUpdate(existingExpenseForecast, expenseForecastDTO);

                return existingExpenseForecast;
            })
            .map(expenseForecastRepository::save)
            .map(expenseForecastMapper::toDto);
    }

    @Override
    public Page<ExpenseForecastDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExpenseForecasts");
        return expenseForecastRepository.findAll(pageable).map(expenseForecastMapper::toDto);
    }

    public Page<ExpenseForecastDTO> findAllWithEagerRelationships(Pageable pageable) {
        return expenseForecastRepository.findAllWithEagerRelationships(pageable).map(expenseForecastMapper::toDto);
    }

    @Override
    public Optional<ExpenseForecastDTO> findOne(String id) {
        log.debug("Request to get ExpenseForecast : {}", id);
        return expenseForecastRepository.findOneWithEagerRelationships(id).map(expenseForecastMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ExpenseForecast : {}", id);
        expenseForecastRepository.deleteById(id);
    }
}
