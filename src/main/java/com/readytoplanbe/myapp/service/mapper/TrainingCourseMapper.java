package com.readytoplanbe.myapp.service.mapper;

import com.readytoplanbe.myapp.domain.TrainingCourse;
import com.readytoplanbe.myapp.service.dto.TrainingCourseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrainingCourse} and its DTO {@link TrainingCourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrainingCourseMapper extends EntityMapper<TrainingCourseDTO, TrainingCourse> {}
