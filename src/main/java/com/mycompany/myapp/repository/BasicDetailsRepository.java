package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BasicDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BasicDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BasicDetailsRepository extends JpaRepository<BasicDetails, Long> {}
