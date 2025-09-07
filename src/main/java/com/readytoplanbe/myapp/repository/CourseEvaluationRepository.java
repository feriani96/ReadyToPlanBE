package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.CourseEvaluation;
import com.readytoplanbe.myapp.domain.TrainingCourse;
import com.readytoplanbe.myapp.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the CourseEvaluation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseEvaluationRepository extends MongoRepository<CourseEvaluation, String> {
    Optional<CourseEvaluation> findByTrainingCourseAndUser(TrainingCourse course, User user);
    long countByTrainingCourseAndSatisfaction(TrainingCourse course, Integer satisfaction);

    long countBySatisfaction(Integer satisfaction);
}
