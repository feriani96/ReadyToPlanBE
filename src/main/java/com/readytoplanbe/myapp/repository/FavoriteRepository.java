package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Favorite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteRepository extends MongoRepository<Favorite, String> {

    List<Favorite> findByUserId(String userId);
    Optional<Favorite> findByUserIdAndTrainingCourseId(String userId, String trainingCourseId);
    void deleteByUserIdAndTrainingCourseId(String userId, String trainingCourseId);
}
