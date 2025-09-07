package com.readytoplanbe.myapp.domain;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A Favorite linking User and TrainingCourse.
 */
@Document(collection = "course_evaluation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseEvaluation {

    @Id
    private String id;

    @DBRef
    private TrainingCourse trainingCourse;

    @DBRef
    private User user;

    // 3 = Satisfied, 1 = Not Satisfied
    private Integer satisfaction;

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public TrainingCourse getTrainingCourse() { return trainingCourse; }
    public void setTrainingCourse(TrainingCourse trainingCourse) { this.trainingCourse = trainingCourse; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Integer getSatisfaction() { return satisfaction; }
    public void setSatisfaction(Integer satisfaction) { this.satisfaction = satisfaction; }
}
