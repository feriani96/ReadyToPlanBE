package com.readytoplanbe.myapp.service.impl;

import com.readytoplanbe.myapp.domain.ProductOrService;
import com.readytoplanbe.myapp.repository.ProductOrServiceRepository;
import com.readytoplanbe.myapp.service.ProductOrServiceService;
import com.readytoplanbe.myapp.service.dto.ProductOrServiceDTO;
import com.readytoplanbe.myapp.service.mapper.ProductOrServiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link ProductOrService}.
 */
@Service
public class ProductOrServiceServiceImpl implements ProductOrServiceService {

    private final Logger log = LoggerFactory.getLogger(ProductOrServiceServiceImpl.class);

    private final ProductOrServiceRepository productOrServiceRepository;

    private final ProductOrServiceMapper productOrServiceMapper;

    public ProductOrServiceServiceImpl(
        ProductOrServiceRepository productOrServiceRepository,
        ProductOrServiceMapper productOrServiceMapper
    ) {
        this.productOrServiceRepository = productOrServiceRepository;
        this.productOrServiceMapper = productOrServiceMapper;
    }

    @Override
    public ProductOrServiceDTO save(ProductOrServiceDTO productOrServiceDTO) {
        log.debug("Request to save ProductOrService : {}", productOrServiceDTO);
        ProductOrService productOrService = productOrServiceMapper.toEntity(productOrServiceDTO);
        productOrService = productOrServiceRepository.save(productOrService);
        return productOrServiceMapper.toDto(productOrService);
    }

    @Override
    public ProductOrServiceDTO update(ProductOrServiceDTO productOrServiceDTO) {
        log.debug("Request to update ProductOrService : {}", productOrServiceDTO);
        ProductOrService productOrService = productOrServiceMapper.toEntity(productOrServiceDTO);
        productOrService = productOrServiceRepository.save(productOrService);
        return productOrServiceMapper.toDto(productOrService);
    }

    @Override
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

    @Override
    public Page<ProductOrServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductOrServices");
        return productOrServiceRepository.findAll(pageable).map(productOrServiceMapper::toDto);
    }

    public Page<ProductOrServiceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productOrServiceRepository.findAllWithEagerRelationships(pageable).map(productOrServiceMapper::toDto);
    }

    @Override
    public Optional<ProductOrServiceDTO> findOne(String id) {
        log.debug("Request to get ProductOrService : {}", id);
        return productOrServiceRepository.findOneWithEagerRelationships(id).map(productOrServiceMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ProductOrService : {}", id);
        productOrServiceRepository.deleteById(id);
    }
}
