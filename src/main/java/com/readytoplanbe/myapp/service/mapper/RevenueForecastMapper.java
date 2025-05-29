package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.FinancialForecast;
import com.readytoplanbe.myapp.domain.ProductOrService;
import com.readytoplanbe.myapp.domain.RevenueForecast;
import com.readytoplanbe.myapp.service.dto.FinancialForecastDTO;
import com.readytoplanbe.myapp.service.dto.ProductOrServiceDTO;
import com.readytoplanbe.myapp.service.dto.RevenueForecastDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RevenueForecast} and its DTO {@link RevenueForecastDTO}.
 */
@Mapper(componentModel = "spring")
public interface RevenueForecastMapper extends EntityMapper<RevenueForecastDTO, RevenueForecast> {
    @Mapping(target = "productOrService", source = "productOrService", qualifiedByName = "productOrServiceNameProductOrService")
    @Mapping(target = "financialForecast", source = "financialForecast", qualifiedByName = "financialForecastStartDate")
    RevenueForecastDTO toDto(RevenueForecast s);

    @Named("productOrServiceNameProductOrService")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nameProductOrService", source = "nameProductOrService")
    ProductOrServiceDTO toDtoProductOrServiceNameProductOrService(ProductOrService productOrService);

    @Named("financialForecastStartDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "startDate", source = "startDate")
    FinancialForecastDTO toDtoFinancialForecastStartDate(FinancialForecast financialForecast);
}
