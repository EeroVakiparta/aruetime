package com.arue.aruetime.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.arue.aruetime.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GamingSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GamingSession.class);
        GamingSession gamingSession1 = new GamingSession();
        gamingSession1.setId(1L);
        GamingSession gamingSession2 = new GamingSession();
        gamingSession2.setId(gamingSession1.getId());
        assertThat(gamingSession1).isEqualTo(gamingSession2);
        gamingSession2.setId(2L);
        assertThat(gamingSession1).isNotEqualTo(gamingSession2);
        gamingSession1.setId(null);
        assertThat(gamingSession1).isNotEqualTo(gamingSession2);
    }
}
