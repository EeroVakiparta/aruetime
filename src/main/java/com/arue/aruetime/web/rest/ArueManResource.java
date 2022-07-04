package com.arue.aruetime.web.rest;

import com.arue.aruetime.domain.ArueMan;
import com.arue.aruetime.repository.ArueManRepository;
import com.arue.aruetime.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.arue.aruetime.domain.ArueMan}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ArueManResource {

    private final Logger log = LoggerFactory.getLogger(ArueManResource.class);

    private static final String ENTITY_NAME = "arueMan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArueManRepository arueManRepository;

    public ArueManResource(ArueManRepository arueManRepository) {
        this.arueManRepository = arueManRepository;
    }

    /**
     * {@code POST  /arue-men} : Create a new arueMan.
     *
     * @param arueMan the arueMan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arueMan, or with status {@code 400 (Bad Request)} if the arueMan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arue-men")
    public ResponseEntity<ArueMan> createArueMan(@Valid @RequestBody ArueMan arueMan) throws URISyntaxException {
        log.debug("REST request to save ArueMan : {}", arueMan);
        if (arueMan.getId() != null) {
            throw new BadRequestAlertException("A new arueMan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArueMan result = arueManRepository.save(arueMan);
        return ResponseEntity
            .created(new URI("/api/arue-men/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arue-men/:id} : Updates an existing arueMan.
     *
     * @param id the id of the arueMan to save.
     * @param arueMan the arueMan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arueMan,
     * or with status {@code 400 (Bad Request)} if the arueMan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arueMan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arue-men/{id}")
    public ResponseEntity<ArueMan> updateArueMan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArueMan arueMan
    ) throws URISyntaxException {
        log.debug("REST request to update ArueMan : {}, {}", id, arueMan);
        if (arueMan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arueMan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arueManRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ArueMan result = arueManRepository.save(arueMan);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arueMan.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /arue-men/:id} : Partial updates given fields of an existing arueMan, field will ignore if it is null
     *
     * @param id the id of the arueMan to save.
     * @param arueMan the arueMan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arueMan,
     * or with status {@code 400 (Bad Request)} if the arueMan is not valid,
     * or with status {@code 404 (Not Found)} if the arueMan is not found,
     * or with status {@code 500 (Internal Server Error)} if the arueMan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/arue-men/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArueMan> partialUpdateArueMan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArueMan arueMan
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArueMan partially : {}, {}", id, arueMan);
        if (arueMan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arueMan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arueManRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArueMan> result = arueManRepository
            .findById(arueMan.getId())
            .map(existingArueMan -> {
                if (arueMan.getNameTag() != null) {
                    existingArueMan.setNameTag(arueMan.getNameTag());
                }
                if (arueMan.getScore() != null) {
                    existingArueMan.setScore(arueMan.getScore());
                }

                return existingArueMan;
            })
            .map(arueManRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arueMan.getId().toString())
        );
    }

    /**
     * {@code GET  /arue-men} : get all the arueMen.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arueMen in body.
     */
    @GetMapping("/arue-men")
    public List<ArueMan> getAllArueMen() {
        log.debug("REST request to get all ArueMen");
        return arueManRepository.findAll();
    }

    /**
     * {@code GET  /arue-men/:id} : get the "id" arueMan.
     *
     * @param id the id of the arueMan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arueMan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arue-men/{id}")
    public ResponseEntity<ArueMan> getArueMan(@PathVariable Long id) {
        log.debug("REST request to get ArueMan : {}", id);
        Optional<ArueMan> arueMan = arueManRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(arueMan);
    }

    /**
     * {@code DELETE  /arue-men/:id} : delete the "id" arueMan.
     *
     * @param id the id of the arueMan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arue-men/{id}")
    public ResponseEntity<Void> deleteArueMan(@PathVariable Long id) {
        log.debug("REST request to delete ArueMan : {}", id);
        arueManRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
