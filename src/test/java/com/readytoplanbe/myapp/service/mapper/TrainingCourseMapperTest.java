package com.readytoplanbe.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrainingCourseMapperTest {

    private TrainingCourseMapper trainingCourseMapper;

    @BeforeEach
    public void setUp() {
        trainingCourseMapper = new TrainingCourseMapperImpl();
    }
}
