package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.TrainingCourse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the TrainingCourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingCourseRepository extends MongoRepository<TrainingCourse, String> {
    List<TrainingCourse> findByCreatedByLogin(String login);

    List<TrainingCourse> findByPublicPresentationTrue();
}
