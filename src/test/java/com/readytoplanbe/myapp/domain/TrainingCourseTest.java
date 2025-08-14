package com.readytoplanbe.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.readytoplanbe.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrainingCourseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingCourse.class);
        TrainingCourse trainingCourse1 = new TrainingCourse();
        trainingCourse1.setId("id1");
        TrainingCourse trainingCourse2 = new TrainingCourse();
        trainingCourse2.setId(trainingCourse1.getId());
        assertThat(trainingCourse1).isEqualTo(trainingCourse2);
        trainingCourse2.setId("id2");
        assertThat(trainingCourse1).isNotEqualTo(trainingCourse2);
        trainingCourse1.setId(null);
        assertThat(trainingCourse1).isNotEqualTo(trainingCourse2);
    }
}
