package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BasicDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BasicDetails.class);
        BasicDetails basicDetails1 = new BasicDetails();
        basicDetails1.setId(1L);
        BasicDetails basicDetails2 = new BasicDetails();
        basicDetails2.setId(basicDetails1.getId());
        assertThat(basicDetails1).isEqualTo(basicDetails2);
        basicDetails2.setId(2L);
        assertThat(basicDetails1).isNotEqualTo(basicDetails2);
        basicDetails1.setId(null);
        assertThat(basicDetails1).isNotEqualTo(basicDetails2);
    }
}
