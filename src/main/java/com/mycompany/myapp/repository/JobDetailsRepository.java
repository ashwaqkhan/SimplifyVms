package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.JobDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the JobDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobDetailsRepository extends JpaRepository<JobDetails, Long> {}
