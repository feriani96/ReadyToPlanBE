package com.readytoplanbe.myapp.repository;

import com.readytoplanbe.myapp.domain.TrainingCourse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the TrainingCourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingCourseRepository extends MongoRepository<TrainingCourse, String> {}
