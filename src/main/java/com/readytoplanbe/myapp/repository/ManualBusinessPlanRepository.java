package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.ManualBusinessPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ManualBusinessPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManualBusinessPlanRepository extends MongoRepository<ManualBusinessPlan, String> {}
