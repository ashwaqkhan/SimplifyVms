package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.BasicDetails;
import com.mycompany.myapp.repository.BasicDetailsRepository;
import com.mycompany.myapp.repository.search.BasicDetailsSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BasicDetails}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BasicDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BasicDetailsResource.class);

    private static final String ENTITY_NAME = "basicDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BasicDetailsRepository basicDetailsRepository;

    private final BasicDetailsSearchRepository basicDetailsSearchRepository;

    public BasicDetailsResource(BasicDetailsRepository basicDetailsRepository, BasicDetailsSearchRepository basicDetailsSearchRepository) {
        this.basicDetailsRepository = basicDetailsRepository;
        this.basicDetailsSearchRepository = basicDetailsSearchRepository;
    }

    /**
     * {@code POST  /basic-details} : Create a new basicDetails.
     *
     * @param basicDetails the basicDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new basicDetails, or with status {@code 400 (Bad Request)} if the basicDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/basic-details")
    public ResponseEntity<BasicDetails> createBasicDetails(@Valid @RequestBody BasicDetails basicDetails) throws URISyntaxException {
        log.debug("REST request to save BasicDetails : {}", basicDetails);
        if (basicDetails.getId() != null) {
            throw new BadRequestAlertException("A new basicDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BasicDetails result = basicDetailsRepository.save(basicDetails);
        basicDetailsSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/basic-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /basic-details/:id} : Updates an existing basicDetails.
     *
     * @param id the id of the basicDetails to save.
     * @param basicDetails the basicDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated basicDetails,
     * or with status {@code 400 (Bad Request)} if the basicDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the basicDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/basic-details/{id}")
    public ResponseEntity<BasicDetails> updateBasicDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BasicDetails basicDetails
    ) throws URISyntaxException {
        log.debug("REST request to update BasicDetails : {}, {}", id, basicDetails);
        if (basicDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, basicDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!basicDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BasicDetails result = basicDetailsRepository.save(basicDetails);
        basicDetailsSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, basicDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /basic-details/:id} : Partial updates given fields of an existing basicDetails, field will ignore if it is null
     *
     * @param id the id of the basicDetails to save.
     * @param basicDetails the basicDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated basicDetails,
     * or with status {@code 400 (Bad Request)} if the basicDetails is not valid,
     * or with status {@code 404 (Not Found)} if the basicDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the basicDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/basic-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BasicDetails> partialUpdateBasicDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BasicDetails basicDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update BasicDetails partially : {}, {}", id, basicDetails);
        if (basicDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, basicDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!basicDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BasicDetails> result = basicDetailsRepository
            .findById(basicDetails.getId())
            .map(
                existingBasicDetails -> {
                    if (basicDetails.getJobRole() != null) {
                        existingBasicDetails.setJobRole(basicDetails.getJobRole());
                    }
                    if (basicDetails.getWorkFromHome() != null) {
                        existingBasicDetails.setWorkFromHome(basicDetails.getWorkFromHome());
                    }
                    if (basicDetails.getType() != null) {
                        existingBasicDetails.setType(basicDetails.getType());
                    }
                    if (basicDetails.getShift() != null) {
                        existingBasicDetails.setShift(basicDetails.getShift());
                    }
                    if (basicDetails.getMinSalary() != null) {
                        existingBasicDetails.setMinSalary(basicDetails.getMinSalary());
                    }
                    if (basicDetails.getMaxSalRY() != null) {
                        existingBasicDetails.setMaxSalRY(basicDetails.getMaxSalRY());
                    }
                    if (basicDetails.getOpenings() != null) {
                        existingBasicDetails.setOpenings(basicDetails.getOpenings());
                    }
                    if (basicDetails.getWorkingDays() != null) {
                        existingBasicDetails.setWorkingDays(basicDetails.getWorkingDays());
                    }
                    if (basicDetails.getWorkTimings() != null) {
                        existingBasicDetails.setWorkTimings(basicDetails.getWorkTimings());
                    }
                    if (basicDetails.getMinEducation() != null) {
                        existingBasicDetails.setMinEducation(basicDetails.getMinEducation());
                    }
                    if (basicDetails.getExperience() != null) {
                        existingBasicDetails.setExperience(basicDetails.getExperience());
                    }
                    if (basicDetails.getGender() != null) {
                        existingBasicDetails.setGender(basicDetails.getGender());
                    }

                    return existingBasicDetails;
                }
            )
            .map(basicDetailsRepository::save)
            .map(
                savedBasicDetails -> {
                    basicDetailsSearchRepository.save(savedBasicDetails);

                    return savedBasicDetails;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, basicDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /basic-details} : get all the basicDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of basicDetails in body.
     */
    @GetMapping("/basic-details")
    public List<BasicDetails> getAllBasicDetails() {
        log.debug("REST request to get all BasicDetails");
        return basicDetailsRepository.findAll();
    }

    /**
     * {@code GET  /basic-details/:id} : get the "id" basicDetails.
     *
     * @param id the id of the basicDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the basicDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/basic-details/{id}")
    public ResponseEntity<BasicDetails> getBasicDetails(@PathVariable Long id) {
        log.debug("REST request to get BasicDetails : {}", id);
        Optional<BasicDetails> basicDetails = basicDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(basicDetails);
    }

    /**
     * {@code DELETE  /basic-details/:id} : delete the "id" basicDetails.
     *
     * @param id the id of the basicDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/basic-details/{id}")
    public ResponseEntity<Void> deleteBasicDetails(@PathVariable Long id) {
        log.debug("REST request to delete BasicDetails : {}", id);
        basicDetailsRepository.deleteById(id);
        basicDetailsSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/basic-details?query=:query} : search for the basicDetails corresponding
     * to the query.
     *
     * @param query the query of the basicDetails search.
     * @return the result of the search.
     */
    @GetMapping("/_search/basic-details")
    public List<BasicDetails> searchBasicDetails(@RequestParam String query) {
        log.debug("REST request to search BasicDetails for query {}", query);
        return StreamSupport
            .stream(basicDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
