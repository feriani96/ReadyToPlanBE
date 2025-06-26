package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.Marketing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Spring Data MongoDB repository for the Marketing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketingRepository extends MongoRepository<Marketing, String> {
    List<Marketing> findAllByCompanyId(String companyId);
    Set<Marketing> findAllByCompany_Id(String companyId);
}
