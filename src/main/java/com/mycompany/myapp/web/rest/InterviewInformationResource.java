package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.InterviewInformation;
import com.mycompany.myapp.repository.InterviewInformationRepository;
import com.mycompany.myapp.repository.search.InterviewInformationSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.InterviewInformation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InterviewInformationResource {

    private final Logger log = LoggerFactory.getLogger(InterviewInformationResource.class);

    private static final String ENTITY_NAME = "interviewInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterviewInformationRepository interviewInformationRepository;

    private final InterviewInformationSearchRepository interviewInformationSearchRepository;

    public InterviewInformationResource(
        InterviewInformationRepository interviewInformationRepository,
        InterviewInformationSearchRepository interviewInformationSearchRepository
    ) {
        this.interviewInformationRepository = interviewInformationRepository;
        this.interviewInformationSearchRepository = interviewInformationSearchRepository;
    }

    /**
     * {@code POST  /interview-informations} : Create a new interviewInformation.
     *
     * @param interviewInformation the interviewInformation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interviewInformation, or with status {@code 400 (Bad Request)} if the interviewInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interview-informations")
    public ResponseEntity<InterviewInformation> createInterviewInformation(@Valid @RequestBody InterviewInformation interviewInformation)
        throws URISyntaxException {
        log.debug("REST request to save InterviewInformation : {}", interviewInformation);
        if (interviewInformation.getId() != null) {
            throw new BadRequestAlertException("A new interviewInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterviewInformation result = interviewInformationRepository.save(interviewInformation);
        interviewInformationSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/interview-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interview-informations/:id} : Updates an existing interviewInformation.
     *
     * @param id the id of the interviewInformation to save.
     * @param interviewInformation the interviewInformation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interviewInformation,
     * or with status {@code 400 (Bad Request)} if the interviewInformation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interviewInformation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interview-informations/{id}")
    public ResponseEntity<InterviewInformation> updateInterviewInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InterviewInformation interviewInformation
    ) throws URISyntaxException {
        log.debug("REST request to update InterviewInformation : {}, {}", id, interviewInformation);
        if (interviewInformation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interviewInformation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interviewInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InterviewInformation result = interviewInformationRepository.save(interviewInformation);
        interviewInformationSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interviewInformation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /interview-informations/:id} : Partial updates given fields of an existing interviewInformation, field will ignore if it is null
     *
     * @param id the id of the interviewInformation to save.
     * @param interviewInformation the interviewInformation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interviewInformation,
     * or with status {@code 400 (Bad Request)} if the interviewInformation is not valid,
     * or with status {@code 404 (Not Found)} if the interviewInformation is not found,
     * or with status {@code 500 (Internal Server Error)} if the interviewInformation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/interview-informations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<InterviewInformation> partialUpdateInterviewInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InterviewInformation interviewInformation
    ) throws URISyntaxException {
        log.debug("REST request to partial update InterviewInformation partially : {}, {}", id, interviewInformation);
        if (interviewInformation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interviewInformation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interviewInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InterviewInformation> result = interviewInformationRepository
            .findById(interviewInformation.getId())
            .map(
                existingInterviewInformation -> {
                    if (interviewInformation.getCompanyName() != null) {
                        existingInterviewInformation.setCompanyName(interviewInformation.getCompanyName());
                    }
                    if (interviewInformation.getRecruitersName() != null) {
                        existingInterviewInformation.setRecruitersName(interviewInformation.getRecruitersName());
                    }
                    if (interviewInformation.gethRwhatsappNumber() != null) {
                        existingInterviewInformation.sethRwhatsappNumber(interviewInformation.gethRwhatsappNumber());
                    }
                    if (interviewInformation.getContactEmail() != null) {
                        existingInterviewInformation.setContactEmail(interviewInformation.getContactEmail());
                    }
                    if (interviewInformation.getBuildingName() != null) {
                        existingInterviewInformation.setBuildingName(interviewInformation.getBuildingName());
                    }
                    if (interviewInformation.getCity() != null) {
                        existingInterviewInformation.setCity(interviewInformation.getCity());
                    }
                    if (interviewInformation.getArea() != null) {
                        existingInterviewInformation.setArea(interviewInformation.getArea());
                    }
                    if (interviewInformation.getRecieveApplicationsFrom() != null) {
                        existingInterviewInformation.setRecieveApplicationsFrom(interviewInformation.getRecieveApplicationsFrom());
                    }

                    return existingInterviewInformation;
                }
            )
            .map(interviewInformationRepository::save)
            .map(
                savedInterviewInformation -> {
                    interviewInformationSearchRepository.save(savedInterviewInformation);

                    return savedInterviewInformation;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interviewInformation.getId().toString())
        );
    }

    /**
     * {@code GET  /interview-informations} : get all the interviewInformations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interviewInformations in body.
     */
    @GetMapping("/interview-informations")
    public List<InterviewInformation> getAllInterviewInformations() {
        log.debug("REST request to get all InterviewInformations");
        return interviewInformationRepository.findAll();
    }

    /**
     * {@code GET  /interview-informations/:id} : get the "id" interviewInformation.
     *
     * @param id the id of the interviewInformation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interviewInformation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interview-informations/{id}")
    public ResponseEntity<InterviewInformation> getInterviewInformation(@PathVariable Long id) {
        log.debug("REST request to get InterviewInformation : {}", id);
        Optional<InterviewInformation> interviewInformation = interviewInformationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(interviewInformation);
    }

    /**
     * {@code DELETE  /interview-informations/:id} : delete the "id" interviewInformation.
     *
     * @param id the id of the interviewInformation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interview-informations/{id}")
    public ResponseEntity<Void> deleteInterviewInformation(@PathVariable Long id) {
        log.debug("REST request to delete InterviewInformation : {}", id);
        interviewInformationRepository.deleteById(id);
        interviewInformationSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/interview-informations?query=:query} : search for the interviewInformation corresponding
     * to the query.
     *
     * @param query the query of the interviewInformation search.
     * @return the result of the search.
     */
    @GetMapping("/_search/interview-informations")
    public List<InterviewInformation> searchInterviewInformations(@RequestParam String query) {
        log.debug("REST request to search InterviewInformations for query {}", query);
        return StreamSupport
            .stream(interviewInformationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
