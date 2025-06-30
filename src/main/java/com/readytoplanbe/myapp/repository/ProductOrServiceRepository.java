package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.ProductOrService;
import io.netty.handler.codec.http2.Http2Connection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data MongoDB repository for the ProductOrService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOrServiceRepository extends MongoRepository<ProductOrService, String> {
    List<ProductOrService> findAllByCompanyId(String companyId);
    Set<ProductOrService> findAllByCompany_Id(String companyId);
}
