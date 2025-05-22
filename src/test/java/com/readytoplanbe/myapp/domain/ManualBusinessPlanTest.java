package com.readytoplanbe.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.readytoplanbe.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualBusinessPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualBusinessPlan.class);
        ManualBusinessPlan manualBusinessPlan1 = new ManualBusinessPlan();
        manualBusinessPlan1.setId("id1");
        ManualBusinessPlan manualBusinessPlan2 = new ManualBusinessPlan();
        manualBusinessPlan2.setId(manualBusinessPlan1.getId());
        assertThat(manualBusinessPlan1).isEqualTo(manualBusinessPlan2);
        manualBusinessPlan2.setId("id2");
        assertThat(manualBusinessPlan1).isNotEqualTo(manualBusinessPlan2);
        manualBusinessPlan1.setId(null);
        assertThat(manualBusinessPlan1).isNotEqualTo(manualBusinessPlan2);
    }
}
