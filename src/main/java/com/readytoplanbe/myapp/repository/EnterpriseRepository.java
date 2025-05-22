package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.Enterprise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Enterprise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnterpriseRepository extends MongoRepository<Enterprise, String> {}
