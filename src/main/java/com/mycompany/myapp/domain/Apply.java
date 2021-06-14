package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Apply.
 */
@Entity
@Table(name = "apply")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "apply")
public class Apply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "mobile_no", nullable = false)
    private Long mobileNo;

    @OneToMany(mappedBy = "apply")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobDetails", "apply" }, allowSetters = true)
    private Set<BasicDetails> basicDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Apply id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Apply name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMobileNo() {
        return this.mobileNo;
    }

    public Apply mobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
        return this;
    }

    public void setMobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Set<BasicDetails> getBasicDetails() {
        return this.basicDetails;
    }

    public Apply basicDetails(Set<BasicDetails> basicDetails) {
        this.setBasicDetails(basicDetails);
        return this;
    }

    public Apply addBasicDetails(BasicDetails basicDetails) {
        this.basicDetails.add(basicDetails);
        basicDetails.setApply(this);
        return this;
    }

    public Apply removeBasicDetails(BasicDetails basicDetails) {
        this.basicDetails.remove(basicDetails);
        basicDetails.setApply(null);
        return this;
    }

    public void setBasicDetails(Set<BasicDetails> basicDetails) {
        if (this.basicDetails != null) {
            this.basicDetails.forEach(i -> i.setApply(null));
        }
        if (basicDetails != null) {
            basicDetails.forEach(i -> i.setApply(this));
        }
        this.basicDetails = basicDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Apply)) {
            return false;
        }
        return id != null && id.equals(((Apply) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Apply{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mobileNo=" + getMobileNo() +
            "}";
    }
}
