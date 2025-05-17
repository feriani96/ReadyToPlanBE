package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {}
