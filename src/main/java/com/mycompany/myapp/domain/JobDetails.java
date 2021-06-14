package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.DepositCharged;
import com.mycompany.myapp.domain.enumeration.ReqEnglish;
import com.mycompany.myapp.domain.enumeration.SkillReq;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A JobDetails.
 */
@Entity
@Table(name = "job_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "jobdetails")
public class JobDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "required_skills", nullable = false)
    private SkillReq requiredSkills;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "english", nullable = false)
    private ReqEnglish english;

    @NotNull
    @Column(name = "job_description", nullable = false)
    private String jobDescription;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "security_deposit_charged", nullable = false)
    private DepositCharged securityDepositCharged;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobDetails id(Long id) {
        this.id = id;
        return this;
    }

    public SkillReq getRequiredSkills() {
        return this.requiredSkills;
    }

    public JobDetails requiredSkills(SkillReq requiredSkills) {
        this.requiredSkills = requiredSkills;
        return this;
    }

    public void setRequiredSkills(SkillReq requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public ReqEnglish getEnglish() {
        return this.english;
    }

    public JobDetails english(ReqEnglish english) {
        this.english = english;
        return this;
    }

    public void setEnglish(ReqEnglish english) {
        this.english = english;
    }

    public String getJobDescription() {
        return this.jobDescription;
    }

    public JobDetails jobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
        return this;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public DepositCharged getSecurityDepositCharged() {
        return this.securityDepositCharged;
    }

    public JobDetails securityDepositCharged(DepositCharged securityDepositCharged) {
        this.securityDepositCharged = securityDepositCharged;
        return this;
    }

    public void setSecurityDepositCharged(DepositCharged securityDepositCharged) {
        this.securityDepositCharged = securityDepositCharged;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobDetails)) {
            return false;
        }
        return id != null && id.equals(((JobDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobDetails{" +
            "id=" + getId() +
            ", requiredSkills='" + getRequiredSkills() + "'" +
            ", english='" + getEnglish() + "'" +
            ", jobDescription='" + getJobDescription() + "'" +
            ", securityDepositCharged='" + getSecurityDepositCharged() + "'" +
            "}";
    }
}
