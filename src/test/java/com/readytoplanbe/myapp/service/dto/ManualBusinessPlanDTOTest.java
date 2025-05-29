package com.readytoplanbe.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.readytoplanbe.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManualBusinessPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ManualBusinessPlanDTO.class);
        ManualBusinessPlanDTO manualBusinessPlanDTO1 = new ManualBusinessPlanDTO();
        manualBusinessPlanDTO1.setId("id1");
        ManualBusinessPlanDTO manualBusinessPlanDTO2 = new ManualBusinessPlanDTO();
        assertThat(manualBusinessPlanDTO1).isNotEqualTo(manualBusinessPlanDTO2);
        manualBusinessPlanDTO2.setId(manualBusinessPlanDTO1.getId());
        assertThat(manualBusinessPlanDTO1).isEqualTo(manualBusinessPlanDTO2);
        manualBusinessPlanDTO2.setId("id2");
        assertThat(manualBusinessPlanDTO1).isNotEqualTo(manualBusinessPlanDTO2);
        manualBusinessPlanDTO1.setId(null);
        assertThat(manualBusinessPlanDTO1).isNotEqualTo(manualBusinessPlanDTO2);
    }
}
