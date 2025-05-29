package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.ExpenseForecast;
import com.readytoplanbe.myapp.domain.FinancialForecast;
import com.readytoplanbe.myapp.service.dto.ExpenseForecastDTO;
import com.readytoplanbe.myapp.service.dto.FinancialForecastDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExpenseForecast} and its DTO {@link ExpenseForecastDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExpenseForecastMapper extends EntityMapper<ExpenseForecastDTO, ExpenseForecast> {
    @Mapping(target = "financialForecast", source = "financialForecast", qualifiedByName = "financialForecastStartDate")
    ExpenseForecastDTO toDto(ExpenseForecast s);

    @Named("financialForecastStartDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "startDate", source = "startDate")
    FinancialForecastDTO toDtoFinancialForecastStartDate(FinancialForecast financialForecast);
}
