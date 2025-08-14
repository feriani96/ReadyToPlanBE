package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.ProductOrService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ProductOrService entity.
 */
@Repository
public interface ProductOrServiceRepository extends MongoRepository<ProductOrService, String> {
    @Query("{}")
    Page<ProductOrService> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<ProductOrService> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<ProductOrService> findOneWithEagerRelationships(String id);

    // Méthode existante
    List<ProductOrService> findAllByCompanyId(String companyId);

    // Ajoutez cette méthode avec une requête explicite
    @Query("{'company_id': ?0}")
    List<ProductOrService> findAllByCompany_Id(String companyId);
}



