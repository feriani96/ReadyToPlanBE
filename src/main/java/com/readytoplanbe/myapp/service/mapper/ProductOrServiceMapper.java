package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.Company;
import com.readytoplanbe.myapp.domain.ProductOrService;
import com.readytoplanbe.myapp.service.dto.ProductOrServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOrService} and its DTO {@link ProductOrServiceDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface ProductOrServiceMapper extends EntityMapper<ProductOrServiceDTO, ProductOrService> {
}
