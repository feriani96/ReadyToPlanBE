package com.readytoplanbe.myapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Favorite linking User and TrainingCourse.
 */
@Document(collection = "favorite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Favorite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("user_id")
    private String userId;

    @NotNull
    @Field("training_course_id")
    private String trainingCourseId;

    @Field("created_date")
    private Instant createdDate = Instant.now();

    public String getId() {
        return id;
    }

    public Favorite id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public Favorite userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrainingCourseId() {
        return trainingCourseId;
    }

    public Favorite trainingCourseId(String trainingCourseId) {
        this.setTrainingCourseId(trainingCourseId);
        return this;
    }

    public void setTrainingCourseId(String trainingCourseId) {
        this.trainingCourseId = trainingCourseId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Favorite createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Favorite)) return false;
        return id != null && id.equals(((Favorite) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Favorite{" +
            "id='" + id + '\'' +
            ", userId='" + userId + '\'' +
            ", trainingCourseId='" + trainingCourseId + '\'' +
            ", createdDate=" + createdDate +
            '}';
    }

}
