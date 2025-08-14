package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.FinancialForecast;
import com.readytoplanbe.myapp.domain.ManualBusinessPlan;
import com.readytoplanbe.myapp.service.dto.FinancialForecastDTO;
import com.readytoplanbe.myapp.service.dto.ManualBusinessPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinancialForecast} and its DTO {@link FinancialForecastDTO}.
 */
@Mapper(componentModel = "spring")
public interface FinancialForecastMapper extends EntityMapper<FinancialForecastDTO, FinancialForecast> {
    @Mapping(target = "manualBusinessPlan", source = "manualBusinessPlan", qualifiedByName = "manualBusinessPlanName")
    FinancialForecastDTO toDto(FinancialForecast s);

    @Named("manualBusinessPlanName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ManualBusinessPlanDTO toDtoManualBusinessPlanName(ManualBusinessPlan manualBusinessPlan);
}
