package com.arue.aruetime.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.arue.aruetime.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArueManTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArueMan.class);
        ArueMan arueMan1 = new ArueMan();
        arueMan1.setId(1L);
        ArueMan arueMan2 = new ArueMan();
        arueMan2.setId(arueMan1.getId());
        assertThat(arueMan1).isEqualTo(arueMan2);
        arueMan2.setId(2L);
        assertThat(arueMan1).isNotEqualTo(arueMan2);
        arueMan1.setId(null);
        assertThat(arueMan1).isNotEqualTo(arueMan2);
    }
}
