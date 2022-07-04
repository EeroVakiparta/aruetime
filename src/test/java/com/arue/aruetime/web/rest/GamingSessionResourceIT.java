package com.arue.aruetime.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.arue.aruetime.IntegrationTest;
import com.arue.aruetime.domain.GamingSession;
import com.arue.aruetime.domain.enumeration.Theme;
import com.arue.aruetime.repository.GamingSessionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link GamingSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GamingSessionResourceIT {

    private static final String DEFAULT_SESSION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_SUCCESS = false;
    private static final Boolean UPDATED_SUCCESS = true;

    private static final Theme DEFAULT_THEME = Theme.HARDCORE;
    private static final Theme UPDATED_THEME = Theme.ARUE;

    private static final String ENTITY_API_URL = "/api/gaming-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GamingSessionRepository gamingSessionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGamingSessionMockMvc;

    private GamingSession gamingSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GamingSession createEntity(EntityManager em) {
        GamingSession gamingSession = new GamingSession()
            .sessionName(DEFAULT_SESSION_NAME)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .success(DEFAULT_SUCCESS)
            .theme(DEFAULT_THEME);
        return gamingSession;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GamingSession createUpdatedEntity(EntityManager em) {
        GamingSession gamingSession = new GamingSession()
            .sessionName(UPDATED_SESSION_NAME)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .success(UPDATED_SUCCESS)
            .theme(UPDATED_THEME);
        return gamingSession;
    }

    @BeforeEach
    public void initTest() {
        gamingSession = createEntity(em);
    }

    @Test
    @Transactional
    void createGamingSession() throws Exception {
        int databaseSizeBeforeCreate = gamingSessionRepository.findAll().size();
        // Create the GamingSession
        restGamingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gamingSession)))
            .andExpect(status().isCreated());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeCreate + 1);
        GamingSession testGamingSession = gamingSessionList.get(gamingSessionList.size() - 1);
        assertThat(testGamingSession.getSessionName()).isEqualTo(DEFAULT_SESSION_NAME);
        assertThat(testGamingSession.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testGamingSession.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testGamingSession.getSuccess()).isEqualTo(DEFAULT_SUCCESS);
        assertThat(testGamingSession.getTheme()).isEqualTo(DEFAULT_THEME);
    }

    @Test
    @Transactional
    void createGamingSessionWithExistingId() throws Exception {
        // Create the GamingSession with an existing ID
        gamingSession.setId(1L);

        int databaseSizeBeforeCreate = gamingSessionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGamingSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gamingSession)))
            .andExpect(status().isBadRequest());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGamingSessions() throws Exception {
        // Initialize the database
        gamingSessionRepository.saveAndFlush(gamingSession);

        // Get all the gamingSessionList
        restGamingSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gamingSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionName").value(hasItem(DEFAULT_SESSION_NAME)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].success").value(hasItem(DEFAULT_SUCCESS.booleanValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME.toString())));
    }

    @Test
    @Transactional
    void getGamingSession() throws Exception {
        // Initialize the database
        gamingSessionRepository.saveAndFlush(gamingSession);

        // Get the gamingSession
        restGamingSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, gamingSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gamingSession.getId().intValue()))
            .andExpect(jsonPath("$.sessionName").value(DEFAULT_SESSION_NAME))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.success").value(DEFAULT_SUCCESS.booleanValue()))
            .andExpect(jsonPath("$.theme").value(DEFAULT_THEME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGamingSession() throws Exception {
        // Get the gamingSession
        restGamingSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGamingSession() throws Exception {
        // Initialize the database
        gamingSessionRepository.saveAndFlush(gamingSession);

        int databaseSizeBeforeUpdate = gamingSessionRepository.findAll().size();

        // Update the gamingSession
        GamingSession updatedGamingSession = gamingSessionRepository.findById(gamingSession.getId()).get();
        // Disconnect from session so that the updates on updatedGamingSession are not directly saved in db
        em.detach(updatedGamingSession);
        updatedGamingSession
            .sessionName(UPDATED_SESSION_NAME)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .success(UPDATED_SUCCESS)
            .theme(UPDATED_THEME);

        restGamingSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGamingSession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGamingSession))
            )
            .andExpect(status().isOk());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeUpdate);
        GamingSession testGamingSession = gamingSessionList.get(gamingSessionList.size() - 1);
        assertThat(testGamingSession.getSessionName()).isEqualTo(UPDATED_SESSION_NAME);
        assertThat(testGamingSession.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testGamingSession.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testGamingSession.getSuccess()).isEqualTo(UPDATED_SUCCESS);
        assertThat(testGamingSession.getTheme()).isEqualTo(UPDATED_THEME);
    }

    @Test
    @Transactional
    void putNonExistingGamingSession() throws Exception {
        int databaseSizeBeforeUpdate = gamingSessionRepository.findAll().size();
        gamingSession.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGamingSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gamingSession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gamingSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGamingSession() throws Exception {
        int databaseSizeBeforeUpdate = gamingSessionRepository.findAll().size();
        gamingSession.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGamingSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gamingSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGamingSession() throws Exception {
        int databaseSizeBeforeUpdate = gamingSessionRepository.findAll().size();
        gamingSession.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGamingSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gamingSession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGamingSessionWithPatch() throws Exception {
        // Initialize the database
        gamingSessionRepository.saveAndFlush(gamingSession);

        int databaseSizeBeforeUpdate = gamingSessionRepository.findAll().size();

        // Update the gamingSession using partial update
        GamingSession partialUpdatedGamingSession = new GamingSession();
        partialUpdatedGamingSession.setId(gamingSession.getId());

        partialUpdatedGamingSession.success(UPDATED_SUCCESS).theme(UPDATED_THEME);

        restGamingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGamingSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGamingSession))
            )
            .andExpect(status().isOk());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeUpdate);
        GamingSession testGamingSession = gamingSessionList.get(gamingSessionList.size() - 1);
        assertThat(testGamingSession.getSessionName()).isEqualTo(DEFAULT_SESSION_NAME);
        assertThat(testGamingSession.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testGamingSession.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testGamingSession.getSuccess()).isEqualTo(UPDATED_SUCCESS);
        assertThat(testGamingSession.getTheme()).isEqualTo(UPDATED_THEME);
    }

    @Test
    @Transactional
    void fullUpdateGamingSessionWithPatch() throws Exception {
        // Initialize the database
        gamingSessionRepository.saveAndFlush(gamingSession);

        int databaseSizeBeforeUpdate = gamingSessionRepository.findAll().size();

        // Update the gamingSession using partial update
        GamingSession partialUpdatedGamingSession = new GamingSession();
        partialUpdatedGamingSession.setId(gamingSession.getId());

        partialUpdatedGamingSession
            .sessionName(UPDATED_SESSION_NAME)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .success(UPDATED_SUCCESS)
            .theme(UPDATED_THEME);

        restGamingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGamingSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGamingSession))
            )
            .andExpect(status().isOk());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeUpdate);
        GamingSession testGamingSession = gamingSessionList.get(gamingSessionList.size() - 1);
        assertThat(testGamingSession.getSessionName()).isEqualTo(UPDATED_SESSION_NAME);
        assertThat(testGamingSession.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testGamingSession.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testGamingSession.getSuccess()).isEqualTo(UPDATED_SUCCESS);
        assertThat(testGamingSession.getTheme()).isEqualTo(UPDATED_THEME);
    }

    @Test
    @Transactional
    void patchNonExistingGamingSession() throws Exception {
        int databaseSizeBeforeUpdate = gamingSessionRepository.findAll().size();
        gamingSession.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGamingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gamingSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gamingSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGamingSession() throws Exception {
        int databaseSizeBeforeUpdate = gamingSessionRepository.findAll().size();
        gamingSession.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGamingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gamingSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGamingSession() throws Exception {
        int databaseSizeBeforeUpdate = gamingSessionRepository.findAll().size();
        gamingSession.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGamingSessionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gamingSession))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GamingSession in the database
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGamingSession() throws Exception {
        // Initialize the database
        gamingSessionRepository.saveAndFlush(gamingSession);

        int databaseSizeBeforeDelete = gamingSessionRepository.findAll().size();

        // Delete the gamingSession
        restGamingSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, gamingSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GamingSession> gamingSessionList = gamingSessionRepository.findAll();
        assertThat(gamingSessionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
