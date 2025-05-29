package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.Enterprise;
import com.readytoplanbe.myapp.service.dto.EnterpriseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enterprise} and its DTO {@link EnterpriseDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnterpriseMapper extends EntityMapper<EnterpriseDTO, Enterprise> {}
