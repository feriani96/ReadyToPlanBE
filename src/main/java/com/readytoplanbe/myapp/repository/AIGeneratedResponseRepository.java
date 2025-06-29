package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.AIGeneratedResponse;
import com.readytoplanbe.myapp.domain.ProductOrService;
import com.readytoplanbe.myapp.domain.enumeration.EntityType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the AIGeneratedResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AIGeneratedResponseRepository extends MongoRepository<AIGeneratedResponse, String> {
    Optional<AIGeneratedResponse> findByEntityTypeAndEntityId(EntityType entityType, String entityId);
    List<AIGeneratedResponse> findAllByEntityId(String companyID);

    List<AIGeneratedResponse> findAllByEntityIdIn(List<String> entityIds);

    List<AIGeneratedResponse> findByEntityIdAndEntityType(String entityId, EntityType entityType);

}
