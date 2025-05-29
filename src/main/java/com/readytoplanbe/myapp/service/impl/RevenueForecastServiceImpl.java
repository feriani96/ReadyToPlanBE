package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.RevenueForecast;
import com.readytoplanbe.myapp.repository.RevenueForecastRepository;
import com.readytoplanbe.myapp.service.RevenueForecastService;
import com.readytoplanbe.myapp.service.dto.RevenueForecastDTO;
import com.readytoplanbe.myapp.service.mapper.RevenueForecastMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link RevenueForecast}.
 */
@Service
public class RevenueForecastServiceImpl implements RevenueForecastService {

    private final Logger log = LoggerFactory.getLogger(RevenueForecastServiceImpl.class);

    private final RevenueForecastRepository revenueForecastRepository;

    private final RevenueForecastMapper revenueForecastMapper;

    public RevenueForecastServiceImpl(RevenueForecastRepository revenueForecastRepository, RevenueForecastMapper revenueForecastMapper) {
        this.revenueForecastRepository = revenueForecastRepository;
        this.revenueForecastMapper = revenueForecastMapper;
    }

    @Override
    public RevenueForecastDTO save(RevenueForecastDTO revenueForecastDTO) {
        log.debug("Request to save RevenueForecast : {}", revenueForecastDTO);
        RevenueForecast revenueForecast = revenueForecastMapper.toEntity(revenueForecastDTO);
        revenueForecast = revenueForecastRepository.save(revenueForecast);
        return revenueForecastMapper.toDto(revenueForecast);
    }

    @Override
    public RevenueForecastDTO update(RevenueForecastDTO revenueForecastDTO) {
        log.debug("Request to update RevenueForecast : {}", revenueForecastDTO);
        RevenueForecast revenueForecast = revenueForecastMapper.toEntity(revenueForecastDTO);
        revenueForecast = revenueForecastRepository.save(revenueForecast);
        return revenueForecastMapper.toDto(revenueForecast);
    }

    @Override
    public Optional<RevenueForecastDTO> partialUpdate(RevenueForecastDTO revenueForecastDTO) {
        log.debug("Request to partially update RevenueForecast : {}", revenueForecastDTO);

        return revenueForecastRepository
            .findById(revenueForecastDTO.getId())
            .map(existingRevenueForecast -> {
                revenueForecastMapper.partialUpdate(existingRevenueForecast, revenueForecastDTO);

                return existingRevenueForecast;
            })
            .map(revenueForecastRepository::save)
            .map(revenueForecastMapper::toDto);
    }

    @Override
    public Page<RevenueForecastDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RevenueForecasts");
        return revenueForecastRepository.findAll(pageable).map(revenueForecastMapper::toDto);
    }

    public Page<RevenueForecastDTO> findAllWithEagerRelationships(Pageable pageable) {
        return revenueForecastRepository.findAllWithEagerRelationships(pageable).map(revenueForecastMapper::toDto);
    }

    @Override
    public Optional<RevenueForecastDTO> findOne(String id) {
        log.debug("Request to get RevenueForecast : {}", id);
        return revenueForecastRepository.findOneWithEagerRelationships(id).map(revenueForecastMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete RevenueForecast : {}", id);
        revenueForecastRepository.deleteById(id);
    }
}
