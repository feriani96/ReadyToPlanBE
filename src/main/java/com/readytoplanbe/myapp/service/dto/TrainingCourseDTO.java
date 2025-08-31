package com.readytoplanbe.myapp.service.dto;

import com.readytoplanbe.myapp.domain.enumeration.Languages;
import com.readytoplanbe.myapp.domain.enumeration.Level;
import com.readytoplanbe.myapp.domain.enumeration.LocationType;
import com.readytoplanbe.myapp.domain.enumeration.StudyClass;
import com.readytoplanbe.myapp.domain.enumeration.TargetAudience;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.readytoplanbe.myapp.domain.TrainingCourse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrainingCourseDTO implements Serializable {

    private String id;

    @NotNull
    private String title;

    private String summary;

    @NotNull
    private TargetAudience targetAudience;

    private String instructor;

    @NotNull
    private StudyClass studyClass;

    @NotNull
    private Level level;

    private LocationType locationType;

    private String duration;

    @NotNull
    private Languages languages;

    private String presentation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public TargetAudience getTargetAudience() {
        return targetAudience;
    }

    public void setTargetAudience(TargetAudience targetAudience) {
        this.targetAudience = targetAudience;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public StudyClass getStudyClass() {
        return studyClass;
    }

    public void setStudyClass(StudyClass studyClass) {
        this.studyClass = studyClass;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public String getPresentation() { return presentation; }

    public void setPresentation(String presentation) { this.presentation = presentation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingCourseDTO)) {
            return false;
        }

        TrainingCourseDTO trainingCourseDTO = (TrainingCourseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trainingCourseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingCourseDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", summary='" + getSummary() + "'" +
            ", targetAudience='" + getTargetAudience() + "'" +
            ", instructor='" + getInstructor() + "'" +
            ", studyClass='" + getStudyClass() + "'" +
            ", level='" + getLevel() + "'" +
            ", locationType='" + getLocationType() + "'" +
            ", duration='" + getDuration() + "'" +
            ", languages='" + getLanguages() + "'" +
            ", presentation='" + getPresentation() +
            "}";
    }
}
