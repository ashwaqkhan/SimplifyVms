package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InterviewInformationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterviewInformation.class);
        InterviewInformation interviewInformation1 = new InterviewInformation();
        interviewInformation1.setId(1L);
        InterviewInformation interviewInformation2 = new InterviewInformation();
        interviewInformation2.setId(interviewInformation1.getId());
        assertThat(interviewInformation1).isEqualTo(interviewInformation2);
        interviewInformation2.setId(2L);
        assertThat(interviewInformation1).isNotEqualTo(interviewInformation2);
        interviewInformation1.setId(null);
        assertThat(interviewInformation1).isNotEqualTo(interviewInformation2);
    }
}
