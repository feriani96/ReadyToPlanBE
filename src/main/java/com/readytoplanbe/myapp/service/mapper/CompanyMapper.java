package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.Company;
import com.readytoplanbe.myapp.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {}
