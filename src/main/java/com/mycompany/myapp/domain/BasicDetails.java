package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.GenderReq;
import com.mycompany.myapp.domain.enumeration.JobType;
import com.mycompany.myapp.domain.enumeration.Qualification;
import com.mycompany.myapp.domain.enumeration.RequiredExp;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A BasicDetails.
 */
@Entity
@Table(name = "basic_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "basicdetails")
public class BasicDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "job_role", nullable = false)
    private String jobRole;

    @NotNull
    @Column(name = "work_from_home", nullable = false)
    private Boolean workFromHome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private JobType type;

    @Column(name = "min_salary")
    private Long minSalary;

    @Column(name = "max_sal_ry")
    private Long maxSalRY;

    @NotNull
    @Column(name = "openings", nullable = false)
    private Integer openings;

    @NotNull
    @Column(name = "working_days", nullable = false)
    private String workingDays;

    @NotNull
    @Column(name = "work_timings", nullable = false)
    private String workTimings;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "min_education", nullable = false)
    private Qualification minEducation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "experience", nullable = false)
    private RequiredExp experience;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private GenderReq gender;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BasicDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getJobRole() {
        return this.jobRole;
    }

    public BasicDetails jobRole(String jobRole) {
        this.jobRole = jobRole;
        return this;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public Boolean getWorkFromHome() {
        return this.workFromHome;
    }

    public BasicDetails workFromHome(Boolean workFromHome) {
        this.workFromHome = workFromHome;
        return this;
    }

    public void setWorkFromHome(Boolean workFromHome) {
        this.workFromHome = workFromHome;
    }

    public JobType getType() {
        return this.type;
    }

    public BasicDetails type(JobType type) {
        this.type = type;
        return this;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public Long getMinSalary() {
        return this.minSalary;
    }

    public BasicDetails minSalary(Long minSalary) {
        this.minSalary = minSalary;
        return this;
    }

    public void setMinSalary(Long minSalary) {
        this.minSalary = minSalary;
    }

    public Long getMaxSalRY() {
        return this.maxSalRY;
    }

    public BasicDetails maxSalRY(Long maxSalRY) {
        this.maxSalRY = maxSalRY;
        return this;
    }

    public void setMaxSalRY(Long maxSalRY) {
        this.maxSalRY = maxSalRY;
    }

    public Integer getOpenings() {
        return this.openings;
    }

    public BasicDetails openings(Integer openings) {
        this.openings = openings;
        return this;
    }

    public void setOpenings(Integer openings) {
        this.openings = openings;
    }

    public String getWorkingDays() {
        return this.workingDays;
    }

    public BasicDetails workingDays(String workingDays) {
        this.workingDays = workingDays;
        return this;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getWorkTimings() {
        return this.workTimings;
    }

    public BasicDetails workTimings(String workTimings) {
        this.workTimings = workTimings;
        return this;
    }

    public void setWorkTimings(String workTimings) {
        this.workTimings = workTimings;
    }

    public Qualification getMinEducation() {
        return this.minEducation;
    }

    public BasicDetails minEducation(Qualification minEducation) {
        this.minEducation = minEducation;
        return this;
    }

    public void setMinEducation(Qualification minEducation) {
        this.minEducation = minEducation;
    }

    public RequiredExp getExperience() {
        return this.experience;
    }

    public BasicDetails experience(RequiredExp experience) {
        this.experience = experience;
        return this;
    }

    public void setExperience(RequiredExp experience) {
        this.experience = experience;
    }

    public GenderReq getGender() {
        return this.gender;
    }

    public BasicDetails gender(GenderReq gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(GenderReq gender) {
        this.gender = gender;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BasicDetails)) {
            return false;
        }
        return id != null && id.equals(((BasicDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BasicDetails{" +
            "id=" + getId() +
            ", jobRole='" + getJobRole() + "'" +
            ", workFromHome='" + getWorkFromHome() + "'" +
            ", type='" + getType() + "'" +
            ", minSalary=" + getMinSalary() +
            ", maxSalRY=" + getMaxSalRY() +
            ", openings=" + getOpenings() +
            ", workingDays='" + getWorkingDays() + "'" +
            ", workTimings='" + getWorkTimings() + "'" +
            ", minEducation='" + getMinEducation() + "'" +
            ", experience='" + getExperience() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
