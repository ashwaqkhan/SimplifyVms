package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JobDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JobDetails.class);
        JobDetails jobDetails1 = new JobDetails();
        jobDetails1.setId(1L);
        JobDetails jobDetails2 = new JobDetails();
        jobDetails2.setId(jobDetails1.getId());
        assertThat(jobDetails1).isEqualTo(jobDetails2);
        jobDetails2.setId(2L);
        assertThat(jobDetails1).isNotEqualTo(jobDetails2);
        jobDetails1.setId(null);
        assertThat(jobDetails1).isNotEqualTo(jobDetails2);
    }
}
