package com.arue.aruetime.web.rest;

import com.arue.aruetime.domain.GamingSession;
import com.arue.aruetime.repository.GamingSessionRepository;
import com.arue.aruetime.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.arue.aruetime.domain.GamingSession}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GamingSessionResource {

    private final Logger log = LoggerFactory.getLogger(GamingSessionResource.class);

    private static final String ENTITY_NAME = "gamingSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GamingSessionRepository gamingSessionRepository;

    public GamingSessionResource(GamingSessionRepository gamingSessionRepository) {
        this.gamingSessionRepository = gamingSessionRepository;
    }

    /**
     * {@code POST  /gaming-sessions} : Create a new gamingSession.
     *
     * @param gamingSession the gamingSession to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gamingSession, or with status {@code 400 (Bad Request)} if the gamingSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gaming-sessions")
    public ResponseEntity<GamingSession> createGamingSession(@RequestBody GamingSession gamingSession) throws URISyntaxException {
        log.debug("REST request to save GamingSession : {}", gamingSession);
        if (gamingSession.getId() != null) {
            throw new BadRequestAlertException("A new gamingSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GamingSession result = gamingSessionRepository.save(gamingSession);
        return ResponseEntity
            .created(new URI("/api/gaming-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gaming-sessions/:id} : Updates an existing gamingSession.
     *
     * @param id the id of the gamingSession to save.
     * @param gamingSession the gamingSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gamingSession,
     * or with status {@code 400 (Bad Request)} if the gamingSession is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gamingSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gaming-sessions/{id}")
    public ResponseEntity<GamingSession> updateGamingSession(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GamingSession gamingSession
    ) throws URISyntaxException {
        log.debug("REST request to update GamingSession : {}, {}", id, gamingSession);
        if (gamingSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gamingSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gamingSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GamingSession result = gamingSessionRepository.save(gamingSession);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gamingSession.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gaming-sessions/:id} : Partial updates given fields of an existing gamingSession, field will ignore if it is null
     *
     * @param id the id of the gamingSession to save.
     * @param gamingSession the gamingSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gamingSession,
     * or with status {@code 400 (Bad Request)} if the gamingSession is not valid,
     * or with status {@code 404 (Not Found)} if the gamingSession is not found,
     * or with status {@code 500 (Internal Server Error)} if the gamingSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gaming-sessions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GamingSession> partialUpdateGamingSession(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GamingSession gamingSession
    ) throws URISyntaxException {
        log.debug("REST request to partial update GamingSession partially : {}, {}", id, gamingSession);
        if (gamingSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gamingSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gamingSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GamingSession> result = gamingSessionRepository
            .findById(gamingSession.getId())
            .map(existingGamingSession -> {
                if (gamingSession.getSessionName() != null) {
                    existingGamingSession.setSessionName(gamingSession.getSessionName());
                }
                if (gamingSession.getStartTime() != null) {
                    existingGamingSession.setStartTime(gamingSession.getStartTime());
                }
                if (gamingSession.getEndTime() != null) {
                    existingGamingSession.setEndTime(gamingSession.getEndTime());
                }
                if (gamingSession.getSuccess() != null) {
                    existingGamingSession.setSuccess(gamingSession.getSuccess());
                }
                if (gamingSession.getTheme() != null) {
                    existingGamingSession.setTheme(gamingSession.getTheme());
                }

                return existingGamingSession;
            })
            .map(gamingSessionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gamingSession.getId().toString())
        );
    }

    /**
     * {@code GET  /gaming-sessions} : get all the gamingSessions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gamingSessions in body.
     */
    @GetMapping("/gaming-sessions")
    public List<GamingSession> getAllGamingSessions() {
        log.debug("REST request to get all GamingSessions");
        return gamingSessionRepository.findAll();
    }

    /**
     * {@code GET  /gaming-sessions/:id} : get the "id" gamingSession.
     *
     * @param id the id of the gamingSession to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gamingSession, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gaming-sessions/{id}")
    public ResponseEntity<GamingSession> getGamingSession(@PathVariable Long id) {
        log.debug("REST request to get GamingSession : {}", id);
        Optional<GamingSession> gamingSession = gamingSessionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gamingSession);
    }

    /**
     * {@code DELETE  /gaming-sessions/:id} : delete the "id" gamingSession.
     *
     * @param id the id of the gamingSession to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gaming-sessions/{id}")
    public ResponseEntity<Void> deleteGamingSession(@PathVariable Long id) {
        log.debug("REST request to delete GamingSession : {}", id);
        gamingSessionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
