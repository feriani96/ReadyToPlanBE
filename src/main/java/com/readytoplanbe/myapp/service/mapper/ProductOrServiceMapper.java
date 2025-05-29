package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.ManualBusinessPlan;
import com.readytoplanbe.myapp.domain.ProductOrService;
import com.readytoplanbe.myapp.service.dto.ManualBusinessPlanDTO;
import com.readytoplanbe.myapp.service.dto.ProductOrServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOrService} and its DTO {@link ProductOrServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductOrServiceMapper extends EntityMapper<ProductOrServiceDTO, ProductOrService> {
    @Mapping(target = "manualBusinessPlan", source = "manualBusinessPlan", qualifiedByName = "manualBusinessPlanName")
    ProductOrServiceDTO toDto(ProductOrService s);

    @Named("manualBusinessPlanName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ManualBusinessPlanDTO toDtoManualBusinessPlanName(ManualBusinessPlan manualBusinessPlan);
}
