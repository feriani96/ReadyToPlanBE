package com.readytoplanbe.myapp.service.mapper;
import com.readytoplanbe.myapp.domain.BusinessPlanFinal;
import com.readytoplanbe.myapp.service.dto.BusinessPlanFinalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface BusinessPlanFinalMapper {
    @Mapping(source = "finalContent", target = "finalContent")
    BusinessPlanFinalDTO toDto(BusinessPlanFinal entity);
    @Mapping(source = "finalContent", target = "finalContent")
    BusinessPlanFinal toEntity(BusinessPlanFinalDTO dto);
}
