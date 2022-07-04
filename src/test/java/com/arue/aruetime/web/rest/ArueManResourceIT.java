package com.arue.aruetime.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.arue.aruetime.IntegrationTest;
import com.arue.aruetime.domain.ArueMan;
import com.arue.aruetime.repository.ArueManRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArueManResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArueManResourceIT {

    private static final String DEFAULT_NAME_TAG = "AAAAAAAAAA";
    private static final String UPDATED_NAME_TAG = "BBBBBBBBBB";

    private static final Long DEFAULT_SCORE = 1L;
    private static final Long UPDATED_SCORE = 2L;

    private static final String ENTITY_API_URL = "/api/arue-men";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArueManRepository arueManRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArueManMockMvc;

    private ArueMan arueMan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArueMan createEntity(EntityManager em) {
        ArueMan arueMan = new ArueMan().nameTag(DEFAULT_NAME_TAG).score(DEFAULT_SCORE);
        return arueMan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArueMan createUpdatedEntity(EntityManager em) {
        ArueMan arueMan = new ArueMan().nameTag(UPDATED_NAME_TAG).score(UPDATED_SCORE);
        return arueMan;
    }

    @BeforeEach
    public void initTest() {
        arueMan = createEntity(em);
    }

    @Test
    @Transactional
    void createArueMan() throws Exception {
        int databaseSizeBeforeCreate = arueManRepository.findAll().size();
        // Create the ArueMan
        restArueManMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arueMan)))
            .andExpect(status().isCreated());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeCreate + 1);
        ArueMan testArueMan = arueManList.get(arueManList.size() - 1);
        assertThat(testArueMan.getNameTag()).isEqualTo(DEFAULT_NAME_TAG);
        assertThat(testArueMan.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    @Transactional
    void createArueManWithExistingId() throws Exception {
        // Create the ArueMan with an existing ID
        arueMan.setId(1L);

        int databaseSizeBeforeCreate = arueManRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArueManMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arueMan)))
            .andExpect(status().isBadRequest());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = arueManRepository.findAll().size();
        // set the field null
        arueMan.setNameTag(null);

        // Create the ArueMan, which fails.

        restArueManMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arueMan)))
            .andExpect(status().isBadRequest());

        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArueMen() throws Exception {
        // Initialize the database
        arueManRepository.saveAndFlush(arueMan);

        // Get all the arueManList
        restArueManMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arueMan.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameTag").value(hasItem(DEFAULT_NAME_TAG)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.intValue())));
    }

    @Test
    @Transactional
    void getArueMan() throws Exception {
        // Initialize the database
        arueManRepository.saveAndFlush(arueMan);

        // Get the arueMan
        restArueManMockMvc
            .perform(get(ENTITY_API_URL_ID, arueMan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arueMan.getId().intValue()))
            .andExpect(jsonPath("$.nameTag").value(DEFAULT_NAME_TAG))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingArueMan() throws Exception {
        // Get the arueMan
        restArueManMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewArueMan() throws Exception {
        // Initialize the database
        arueManRepository.saveAndFlush(arueMan);

        int databaseSizeBeforeUpdate = arueManRepository.findAll().size();

        // Update the arueMan
        ArueMan updatedArueMan = arueManRepository.findById(arueMan.getId()).get();
        // Disconnect from session so that the updates on updatedArueMan are not directly saved in db
        em.detach(updatedArueMan);
        updatedArueMan.nameTag(UPDATED_NAME_TAG).score(UPDATED_SCORE);

        restArueManMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArueMan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArueMan))
            )
            .andExpect(status().isOk());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeUpdate);
        ArueMan testArueMan = arueManList.get(arueManList.size() - 1);
        assertThat(testArueMan.getNameTag()).isEqualTo(UPDATED_NAME_TAG);
        assertThat(testArueMan.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void putNonExistingArueMan() throws Exception {
        int databaseSizeBeforeUpdate = arueManRepository.findAll().size();
        arueMan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArueManMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arueMan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arueMan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArueMan() throws Exception {
        int databaseSizeBeforeUpdate = arueManRepository.findAll().size();
        arueMan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArueManMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(arueMan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArueMan() throws Exception {
        int databaseSizeBeforeUpdate = arueManRepository.findAll().size();
        arueMan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArueManMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(arueMan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArueManWithPatch() throws Exception {
        // Initialize the database
        arueManRepository.saveAndFlush(arueMan);

        int databaseSizeBeforeUpdate = arueManRepository.findAll().size();

        // Update the arueMan using partial update
        ArueMan partialUpdatedArueMan = new ArueMan();
        partialUpdatedArueMan.setId(arueMan.getId());

        partialUpdatedArueMan.score(UPDATED_SCORE);

        restArueManMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArueMan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArueMan))
            )
            .andExpect(status().isOk());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeUpdate);
        ArueMan testArueMan = arueManList.get(arueManList.size() - 1);
        assertThat(testArueMan.getNameTag()).isEqualTo(DEFAULT_NAME_TAG);
        assertThat(testArueMan.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void fullUpdateArueManWithPatch() throws Exception {
        // Initialize the database
        arueManRepository.saveAndFlush(arueMan);

        int databaseSizeBeforeUpdate = arueManRepository.findAll().size();

        // Update the arueMan using partial update
        ArueMan partialUpdatedArueMan = new ArueMan();
        partialUpdatedArueMan.setId(arueMan.getId());

        partialUpdatedArueMan.nameTag(UPDATED_NAME_TAG).score(UPDATED_SCORE);

        restArueManMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArueMan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArueMan))
            )
            .andExpect(status().isOk());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeUpdate);
        ArueMan testArueMan = arueManList.get(arueManList.size() - 1);
        assertThat(testArueMan.getNameTag()).isEqualTo(UPDATED_NAME_TAG);
        assertThat(testArueMan.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void patchNonExistingArueMan() throws Exception {
        int databaseSizeBeforeUpdate = arueManRepository.findAll().size();
        arueMan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArueManMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arueMan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arueMan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArueMan() throws Exception {
        int databaseSizeBeforeUpdate = arueManRepository.findAll().size();
        arueMan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArueManMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(arueMan))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArueMan() throws Exception {
        int databaseSizeBeforeUpdate = arueManRepository.findAll().size();
        arueMan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArueManMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(arueMan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArueMan in the database
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArueMan() throws Exception {
        // Initialize the database
        arueManRepository.saveAndFlush(arueMan);

        int databaseSizeBeforeDelete = arueManRepository.findAll().size();

        // Delete the arueMan
        restArueManMockMvc
            .perform(delete(ENTITY_API_URL_ID, arueMan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArueMan> arueManList = arueManRepository.findAll();
        assertThat(arueManList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
