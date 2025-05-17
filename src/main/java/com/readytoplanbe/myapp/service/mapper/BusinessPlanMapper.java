package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.BusinessPlan;
import com.readytoplanbe.myapp.service.dto.BusinessPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BusinessPlan} and its DTO {@link BusinessPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusinessPlanMapper extends EntityMapper<BusinessPlanDTO, BusinessPlan> {}
