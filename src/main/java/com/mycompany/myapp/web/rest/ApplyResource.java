package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Apply;
import com.mycompany.myapp.repository.ApplyRepository;
import com.mycompany.myapp.repository.search.ApplySearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Apply}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ApplyResource {

    private final Logger log = LoggerFactory.getLogger(ApplyResource.class);

    private static final String ENTITY_NAME = "apply";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplyRepository applyRepository;

    private final ApplySearchRepository applySearchRepository;

    public ApplyResource(ApplyRepository applyRepository, ApplySearchRepository applySearchRepository) {
        this.applyRepository = applyRepository;
        this.applySearchRepository = applySearchRepository;
    }

    /**
     * {@code POST  /applies} : Create a new apply.
     *
     * @param apply the apply to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apply, or with status {@code 400 (Bad Request)} if the apply has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/applies")
    public ResponseEntity<Apply> createApply(@Valid @RequestBody Apply apply) throws URISyntaxException {
        log.debug("REST request to save Apply : {}", apply);
        if (apply.getId() != null) {
            throw new BadRequestAlertException("A new apply cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Apply result = applyRepository.save(apply);
        applySearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/applies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /applies/:id} : Updates an existing apply.
     *
     * @param id the id of the apply to save.
     * @param apply the apply to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apply,
     * or with status {@code 400 (Bad Request)} if the apply is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apply couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/applies/{id}")
    public ResponseEntity<Apply> updateApply(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Apply apply)
        throws URISyntaxException {
        log.debug("REST request to update Apply : {}, {}", id, apply);
        if (apply.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apply.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Apply result = applyRepository.save(apply);
        applySearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apply.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /applies/:id} : Partial updates given fields of an existing apply, field will ignore if it is null
     *
     * @param id the id of the apply to save.
     * @param apply the apply to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apply,
     * or with status {@code 400 (Bad Request)} if the apply is not valid,
     * or with status {@code 404 (Not Found)} if the apply is not found,
     * or with status {@code 500 (Internal Server Error)} if the apply couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/applies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Apply> partialUpdateApply(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Apply apply
    ) throws URISyntaxException {
        log.debug("REST request to partial update Apply partially : {}, {}", id, apply);
        if (apply.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apply.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Apply> result = applyRepository
            .findById(apply.getId())
            .map(
                existingApply -> {
                    if (apply.getName() != null) {
                        existingApply.setName(apply.getName());
                    }
                    if (apply.getMobileNo() != null) {
                        existingApply.setMobileNo(apply.getMobileNo());
                    }

                    return existingApply;
                }
            )
            .map(applyRepository::save)
            .map(
                savedApply -> {
                    applySearchRepository.save(savedApply);

                    return savedApply;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apply.getId().toString())
        );
    }

    /**
     * {@code GET  /applies} : get all the applies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applies in body.
     */
    @GetMapping("/applies")
    public List<Apply> getAllApplies() {
        log.debug("REST request to get all Applies");
        return applyRepository.findAll();
    }

    /**
     * {@code GET  /applies/:id} : get the "id" apply.
     *
     * @param id the id of the apply to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apply, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/applies/{id}")
    public ResponseEntity<Apply> getApply(@PathVariable Long id) {
        log.debug("REST request to get Apply : {}", id);
        Optional<Apply> apply = applyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(apply);
    }

    /**
     * {@code DELETE  /applies/:id} : delete the "id" apply.
     *
     * @param id the id of the apply to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/applies/{id}")
    public ResponseEntity<Void> deleteApply(@PathVariable Long id) {
        log.debug("REST request to delete Apply : {}", id);
        applyRepository.deleteById(id);
        applySearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/applies?query=:query} : search for the apply corresponding
     * to the query.
     *
     * @param query the query of the apply search.
     * @return the result of the search.
     */
    @GetMapping("/_search/applies")
    public List<Apply> searchApplies(@RequestParam String query) {
        log.debug("REST request to search Applies for query {}", query);
        return StreamSupport
            .stream(applySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
