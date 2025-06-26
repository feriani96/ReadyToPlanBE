package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.Marketing;
import com.readytoplanbe.myapp.service.dto.MarketingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Marketing} and its DTO {@link MarketingDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface MarketingMapper extends EntityMapper<MarketingDTO, Marketing> {}
