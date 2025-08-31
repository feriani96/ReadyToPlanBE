package com.readytoplanbe.myapp.domain;

import com.readytoplanbe.myapp.domain.enumeration.Languages;
import com.readytoplanbe.myapp.domain.enumeration.Level;
import com.readytoplanbe.myapp.domain.enumeration.LocationType;
import com.readytoplanbe.myapp.domain.enumeration.StudyClass;
import com.readytoplanbe.myapp.domain.enumeration.TargetAudience;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A TrainingCourse.
 */
@Document(collection = "training_course")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("title")
    private String title;

    @Field("summary")
    private String summary;

    @NotNull
    @Field("target_audience")
    private TargetAudience targetAudience;

    @Field("instructor")
    private String instructor;

    @NotNull
    @Field("study_class")
    private StudyClass studyClass;

    @NotNull
    @Field("level")
    private Level level;

    @Field("location_type")
    private LocationType locationType;

    @Field("duration")
    private String duration;

    @NotNull
    @Field("languages")
    private Languages languages;

    @Field("presentation")
    private String presentation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public TrainingCourse id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public TrainingCourse title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return this.summary;
    }

    public TrainingCourse summary(String summary) {
        this.setSummary(summary);
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public TargetAudience getTargetAudience() {
        return this.targetAudience;
    }

    public TrainingCourse targetAudience(TargetAudience targetAudience) {
        this.setTargetAudience(targetAudience);
        return this;
    }

    public void setTargetAudience(TargetAudience targetAudience) {
        this.targetAudience = targetAudience;
    }

    public String getInstructor() {
        return this.instructor;
    }

    public TrainingCourse instructor(String instructor) {
        this.setInstructor(instructor);
        return this;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public StudyClass getStudyClass() {
        return this.studyClass;
    }

    public TrainingCourse studyClass(StudyClass studyClass) {
        this.setStudyClass(studyClass);
        return this;
    }

    public void setStudyClass(StudyClass studyClass) {
        this.studyClass = studyClass;
    }

    public Level getLevel() {
        return this.level;
    }

    public TrainingCourse level(Level level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public LocationType getLocationType() {
        return this.locationType;
    }

    public TrainingCourse locationType(LocationType locationType) {
        this.setLocationType(locationType);
        return this;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getDuration() {
        return this.duration;
    }

    public TrainingCourse duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Languages getLanguages() {
        return this.languages;
    }

    public TrainingCourse languages(Languages languages) {
        this.setLanguages(languages);
        return this;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public String getPresentation() {
        return this.presentation;
    }

    public TrainingCourse presentation(String presentation) {
        this.setPresentation(presentation);
        return this;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingCourse)) {
            return false;
        }
        return id != null && id.equals(((TrainingCourse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingCourse{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", summary='" + getSummary() + "'" +
            ", targetAudience='" + getTargetAudience() + "'" +
            ", instructor='" + getInstructor() + "'" +
            ", studyClass='" + getStudyClass() + "'" +
            ", level='" + getLevel() + "'" +
            ", locationType='" + getLocationType() + "'" +
            ", duration='" + getDuration() + "'" +
            ", languages='" + getLanguages() + "'" +
            "}";
    }
}
