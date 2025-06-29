package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.Team;
import com.readytoplanbe.myapp.service.dto.TeamDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Team} and its DTO {@link TeamDTO}.
 */
@Mapper(componentModel = "spring", uses = { CompanyMapper.class })
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {}
