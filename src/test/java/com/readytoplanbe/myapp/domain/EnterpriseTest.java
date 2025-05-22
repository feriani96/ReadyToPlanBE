package com.readytoplanbe.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.readytoplanbe.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnterpriseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enterprise.class);
        Enterprise enterprise1 = new Enterprise();
        enterprise1.setId("id1");
        Enterprise enterprise2 = new Enterprise();
        enterprise2.setId(enterprise1.getId());
        assertThat(enterprise1).isEqualTo(enterprise2);
        enterprise2.setId("id2");
        assertThat(enterprise1).isNotEqualTo(enterprise2);
        enterprise1.setId(null);
        assertThat(enterprise1).isNotEqualTo(enterprise2);
    }
}
