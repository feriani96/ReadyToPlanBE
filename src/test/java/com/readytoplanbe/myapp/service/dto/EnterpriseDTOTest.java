package com.readytoplanbe.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.readytoplanbe.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnterpriseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnterpriseDTO.class);
        EnterpriseDTO enterpriseDTO1 = new EnterpriseDTO();
        enterpriseDTO1.setId("id1");
        EnterpriseDTO enterpriseDTO2 = new EnterpriseDTO();
        assertThat(enterpriseDTO1).isNotEqualTo(enterpriseDTO2);
        enterpriseDTO2.setId(enterpriseDTO1.getId());
        assertThat(enterpriseDTO1).isEqualTo(enterpriseDTO2);
        enterpriseDTO2.setId("id2");
        assertThat(enterpriseDTO1).isNotEqualTo(enterpriseDTO2);
        enterpriseDTO1.setId(null);
        assertThat(enterpriseDTO1).isNotEqualTo(enterpriseDTO2);
    }
}
