package com.arue.aruetime.repository;

import com.arue.aruetime.domain.GamingSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GamingSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GamingSessionRepository extends JpaRepository<GamingSession, Long> {}
