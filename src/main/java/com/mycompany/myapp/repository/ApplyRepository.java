package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Apply;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Apply entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {}
