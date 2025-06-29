package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data MongoDB repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends MongoRepository<Team, String> {
    List<Team> findAllByCompanyId(String companyId);
    Set<Team> findAllByCompany_Id(String companyId);
}
