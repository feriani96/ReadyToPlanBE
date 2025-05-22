package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.FinancialForecast;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FinancialForecast entity.
 */
@Repository
public interface FinancialForecastRepository extends MongoRepository<FinancialForecast, String> {
    @Query("{}")
    Page<FinancialForecast> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<FinancialForecast> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<FinancialForecast> findOneWithEagerRelationships(String id);
}
