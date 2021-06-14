package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.JobDetails;
import com.mycompany.myapp.repository.JobDetailsRepository;
import com.mycompany.myapp.repository.search.JobDetailsSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.JobDetails}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JobDetailsResource {

    private final Logger log = LoggerFactory.getLogger(JobDetailsResource.class);

    private static final String ENTITY_NAME = "jobDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobDetailsRepository jobDetailsRepository;

    private final JobDetailsSearchRepository jobDetailsSearchRepository;

    public JobDetailsResource(JobDetailsRepository jobDetailsRepository, JobDetailsSearchRepository jobDetailsSearchRepository) {
        this.jobDetailsRepository = jobDetailsRepository;
        this.jobDetailsSearchRepository = jobDetailsSearchRepository;
    }

    /**
     * {@code POST  /job-details} : Create a new jobDetails.
     *
     * @param jobDetails the jobDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobDetails, or with status {@code 400 (Bad Request)} if the jobDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-details")
    public ResponseEntity<JobDetails> createJobDetails(@Valid @RequestBody JobDetails jobDetails) throws URISyntaxException {
        log.debug("REST request to save JobDetails : {}", jobDetails);
        if (jobDetails.getId() != null) {
            throw new BadRequestAlertException("A new jobDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobDetails result = jobDetailsRepository.save(jobDetails);
        jobDetailsSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/job-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-details/:id} : Updates an existing jobDetails.
     *
     * @param id the id of the jobDetails to save.
     * @param jobDetails the jobDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobDetails,
     * or with status {@code 400 (Bad Request)} if the jobDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-details/{id}")
    public ResponseEntity<JobDetails> updateJobDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobDetails jobDetails
    ) throws URISyntaxException {
        log.debug("REST request to update JobDetails : {}, {}", id, jobDetails);
        if (jobDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JobDetails result = jobDetailsRepository.save(jobDetails);
        jobDetailsSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /job-details/:id} : Partial updates given fields of an existing jobDetails, field will ignore if it is null
     *
     * @param id the id of the jobDetails to save.
     * @param jobDetails the jobDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobDetails,
     * or with status {@code 400 (Bad Request)} if the jobDetails is not valid,
     * or with status {@code 404 (Not Found)} if the jobDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<JobDetails> partialUpdateJobDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobDetails jobDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobDetails partially : {}, {}", id, jobDetails);
        if (jobDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobDetails> result = jobDetailsRepository
            .findById(jobDetails.getId())
            .map(
                existingJobDetails -> {
                    if (jobDetails.getRequiredSkills() != null) {
                        existingJobDetails.setRequiredSkills(jobDetails.getRequiredSkills());
                    }
                    if (jobDetails.getEnglish() != null) {
                        existingJobDetails.setEnglish(jobDetails.getEnglish());
                    }
                    if (jobDetails.getJobDescription() != null) {
                        existingJobDetails.setJobDescription(jobDetails.getJobDescription());
                    }
                    if (jobDetails.getSecurityDepositCharged() != null) {
                        existingJobDetails.setSecurityDepositCharged(jobDetails.getSecurityDepositCharged());
                    }

                    return existingJobDetails;
                }
            )
            .map(jobDetailsRepository::save)
            .map(
                savedJobDetails -> {
                    jobDetailsSearchRepository.save(savedJobDetails);

                    return savedJobDetails;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /job-details} : get all the jobDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobDetails in body.
     */
    @GetMapping("/job-details")
    public List<JobDetails> getAllJobDetails() {
        log.debug("REST request to get all JobDetails");
        return jobDetailsRepository.findAll();
    }

    /**
     * {@code GET  /job-details/:id} : get the "id" jobDetails.
     *
     * @param id the id of the jobDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-details/{id}")
    public ResponseEntity<JobDetails> getJobDetails(@PathVariable Long id) {
        log.debug("REST request to get JobDetails : {}", id);
        Optional<JobDetails> jobDetails = jobDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jobDetails);
    }

    /**
     * {@code DELETE  /job-details/:id} : delete the "id" jobDetails.
     *
     * @param id the id of the jobDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-details/{id}")
    public ResponseEntity<Void> deleteJobDetails(@PathVariable Long id) {
        log.debug("REST request to delete JobDetails : {}", id);
        jobDetailsRepository.deleteById(id);
        jobDetailsSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/job-details?query=:query} : search for the jobDetails corresponding
     * to the query.
     *
     * @param query the query of the jobDetails search.
     * @return the result of the search.
     */
    @GetMapping("/_search/job-details")
    public List<JobDetails> searchJobDetails(@RequestParam String query) {
        log.debug("REST request to search JobDetails for query {}", query);
        return StreamSupport
            .stream(jobDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
