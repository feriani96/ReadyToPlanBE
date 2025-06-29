package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.FinancialForecast;
import com.readytoplanbe.myapp.service.dto.FinancialForecastDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinancialForecast} and its DTO {@link FinancialForecastDTO}.
 */
@Mapper(componentModel = "spring")
public interface FinancialForecastMapper extends EntityMapper<FinancialForecastDTO, FinancialForecast> {}
