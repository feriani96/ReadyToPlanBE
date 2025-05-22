package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.RevenueForecast;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the RevenueForecast entity.
 */
@Repository
public interface RevenueForecastRepository extends MongoRepository<RevenueForecast, String> {
    @Query("{}")
    Page<RevenueForecast> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<RevenueForecast> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<RevenueForecast> findOneWithEagerRelationships(String id);
}
