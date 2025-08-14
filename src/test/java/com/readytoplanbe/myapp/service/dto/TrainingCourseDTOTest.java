package com.readytoplanbe.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.readytoplanbe.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainingCourseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingCourseDTO.class);
        TrainingCourseDTO trainingCourseDTO1 = new TrainingCourseDTO();
        trainingCourseDTO1.setId("id1");
        TrainingCourseDTO trainingCourseDTO2 = new TrainingCourseDTO();
        assertThat(trainingCourseDTO1).isNotEqualTo(trainingCourseDTO2);
        trainingCourseDTO2.setId(trainingCourseDTO1.getId());
        assertThat(trainingCourseDTO1).isEqualTo(trainingCourseDTO2);
        trainingCourseDTO2.setId("id2");
        assertThat(trainingCourseDTO1).isNotEqualTo(trainingCourseDTO2);
        trainingCourseDTO1.setId(null);
        assertThat(trainingCourseDTO1).isNotEqualTo(trainingCourseDTO2);
    }
}
