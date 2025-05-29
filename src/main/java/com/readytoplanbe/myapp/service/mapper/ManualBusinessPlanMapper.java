package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.ManualBusinessPlan;
import com.readytoplanbe.myapp.service.dto.ManualBusinessPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ManualBusinessPlan} and its DTO {@link ManualBusinessPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManualBusinessPlanMapper extends EntityMapper<ManualBusinessPlanDTO, ManualBusinessPlan> {}
