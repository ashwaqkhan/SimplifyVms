package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.Application;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A InterviewInformation.
 */
@Entity
@Table(name = "interview_information")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "interviewinformation")
public class InterviewInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "recruiters_name")
    private String recruitersName;

    @Column(name = "h_rwhatsapp_number")
    private Long hRwhatsappNumber;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "city")
    private String city;

    @Column(name = "area")
    private String area;

    @Enumerated(EnumType.STRING)
    @Column(name = "recieve_applications_from")
    private Application recieveApplicationsFrom;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterviewInformation id(Long id) {
        this.id = id;
        return this;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public InterviewInformation companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRecruitersName() {
        return this.recruitersName;
    }

    public InterviewInformation recruitersName(String recruitersName) {
        this.recruitersName = recruitersName;
        return this;
    }

    public void setRecruitersName(String recruitersName) {
        this.recruitersName = recruitersName;
    }

    public Long gethRwhatsappNumber() {
        return this.hRwhatsappNumber;
    }

    public InterviewInformation hRwhatsappNumber(Long hRwhatsappNumber) {
        this.hRwhatsappNumber = hRwhatsappNumber;
        return this;
    }

    public void sethRwhatsappNumber(Long hRwhatsappNumber) {
        this.hRwhatsappNumber = hRwhatsappNumber;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public InterviewInformation contactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getBuildingName() {
        return this.buildingName;
    }

    public InterviewInformation buildingName(String buildingName) {
        this.buildingName = buildingName;
        return this;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getCity() {
        return this.city;
    }

    public InterviewInformation city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return this.area;
    }

    public InterviewInformation area(String area) {
        this.area = area;
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Application getRecieveApplicationsFrom() {
        return this.recieveApplicationsFrom;
    }

    public InterviewInformation recieveApplicationsFrom(Application recieveApplicationsFrom) {
        this.recieveApplicationsFrom = recieveApplicationsFrom;
        return this;
    }

    public void setRecieveApplicationsFrom(Application recieveApplicationsFrom) {
        this.recieveApplicationsFrom = recieveApplicationsFrom;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterviewInformation)) {
            return false;
        }
        return id != null && id.equals(((InterviewInformation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterviewInformation{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", recruitersName='" + getRecruitersName() + "'" +
            ", hRwhatsappNumber=" + gethRwhatsappNumber() +
            ", contactEmail='" + getContactEmail() + "'" +
            ", buildingName='" + getBuildingName() + "'" +
            ", city='" + getCity() + "'" +
            ", area='" + getArea() + "'" +
            ", recieveApplicationsFrom='" + getRecieveApplicationsFrom() + "'" +
            "}";
    }
}
