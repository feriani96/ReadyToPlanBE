package com.readytoplanbe.myapp.service;

import com.readytoplanbe.myapp.domain.Company;
import com.readytoplanbe.myapp.domain.Team;
import com.readytoplanbe.myapp.domain.enumeration.EntityType;
import com.readytoplanbe.myapp.repository.CompanyRepository;
import com.readytoplanbe.myapp.repository.TeamRepository;
import com.readytoplanbe.myapp.service.dto.TeamDTO;
import com.readytoplanbe.myapp.service.mapper.TeamMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Team}.
 */
@Service
public class TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;
    private final CompanyRepository companyRepository;
    private final AIGenerationService aiGenerationService;
    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper, CompanyRepository companyRepository, AIGenerationService aiGenerationService) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.companyRepository = companyRepository;
        this.aiGenerationService = aiGenerationService;
    }


    public TeamDTO save(TeamDTO teamDTO,String companyId) {
        log.debug("Request to save Team : {}", teamDTO);
        Team team = teamMapper.toEntity(teamDTO);
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        team.setCompany(company);
        team = teamRepository.save(team);
        String prompt = "Donne une description professionnelle de cette membre : " + team.getName() +team.getRole()
            + ". Voici les  competances : " + team.getCompetance();
        aiGenerationService.generateAndSave(
            team.getId(),
            EntityType.TEAM,
            prompt// Assure-toi que ce champ existe bien
        );
        return teamMapper.toDto(team);
    }
    public List<TeamDTO> findByCompanyId(String companyId) {
        List<Team> teams = teamRepository.findAllByCompanyId(companyId);
        return teams.stream().map(teamMapper::toDto).collect(Collectors.toList());

    }

    /**
     * Update a team.
     *
     * @param teamDTO the entity to save.
     * @return the persisted entity.
     */
    public TeamDTO update(TeamDTO teamDTO) {
        log.debug("Request to update Team : {}", teamDTO);
        Team team = teamMapper.toEntity(teamDTO);
        team = teamRepository.save(team);
        return teamMapper.toDto(team);
    }

    /**
     * Partially update a team.
     *
     * @param teamDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TeamDTO> partialUpdate(TeamDTO teamDTO) {
        log.debug("Request to partially update Team : {}", teamDTO);

        return teamRepository
            .findById(teamDTO.getId())
            .map(existingTeam -> {
                teamMapper.partialUpdate(existingTeam, teamDTO);

                return existingTeam;
            })
            .map(teamRepository::save)
            .map(teamMapper::toDto);
    }

    /**
     * Get all the teams.
     *
     * @return the list of entities.
     */
    public List<TeamDTO> findAll() {
        log.debug("Request to get all Teams");
        return teamRepository.findAll().stream().map(teamMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one team by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<TeamDTO> findOne(String id) {
        log.debug("Request to get Team : {}", id);
        return teamRepository.findById(id).map(teamMapper::toDto);
    }

    /**
     * Delete the team by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.deleteById(id);
    }
}
