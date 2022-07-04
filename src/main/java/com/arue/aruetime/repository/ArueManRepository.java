package com.arue.aruetime.repository;

import com.arue.aruetime.domain.ArueMan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ArueMan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArueManRepository extends JpaRepository<ArueMan, Long> {}
