package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.InterviewInformation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InterviewInformation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterviewInformationRepository extends JpaRepository<InterviewInformation, Long> {}
