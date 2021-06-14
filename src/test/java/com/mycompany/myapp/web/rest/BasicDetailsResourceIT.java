package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BasicDetails;
import com.mycompany.myapp.domain.enumeration.GenderReq;
import com.mycompany.myapp.domain.enumeration.JobType;
import com.mycompany.myapp.domain.enumeration.Qualification;
import com.mycompany.myapp.domain.enumeration.RequiredExp;
import com.mycompany.myapp.repository.BasicDetailsRepository;
import com.mycompany.myapp.repository.search.BasicDetailsSearchRepository;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BasicDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BasicDetailsResourceIT {

    private static final String DEFAULT_JOB_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_ROLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WORK_FROM_HOME = false;
    private static final Boolean UPDATED_WORK_FROM_HOME = true;

    private static final JobType DEFAULT_TYPE = JobType.PartTime;
    private static final JobType UPDATED_TYPE = JobType.FullTime;

    private static final Long DEFAULT_MIN_SALARY = 1L;
    private static final Long UPDATED_MIN_SALARY = 2L;

    private static final Long DEFAULT_MAX_SAL_RY = 1L;
    private static final Long UPDATED_MAX_SAL_RY = 2L;

    private static final Integer DEFAULT_OPENINGS = 1;
    private static final Integer UPDATED_OPENINGS = 2;

    private static final String DEFAULT_WORKING_DAYS = "AAAAAAAAAA";
    private static final String UPDATED_WORKING_DAYS = "BBBBBBBBBB";

    private static final String DEFAULT_WORK_TIMINGS = "AAAAAAAAAA";
    private static final String UPDATED_WORK_TIMINGS = "BBBBBBBBBB";

    private static final Qualification DEFAULT_MIN_EDUCATION = Qualification.BelowTenth;
    private static final Qualification UPDATED_MIN_EDUCATION = Qualification.Tenth;

    private static final RequiredExp DEFAULT_EXPERIENCE = RequiredExp.Fresher;
    private static final RequiredExp UPDATED_EXPERIENCE = RequiredExp.Experienced;

    private static final GenderReq DEFAULT_GENDER = GenderReq.Male;
    private static final GenderReq UPDATED_GENDER = GenderReq.Female;

    private static final String ENTITY_API_URL = "/api/basic-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/basic-details";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BasicDetailsRepository basicDetailsRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.BasicDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private BasicDetailsSearchRepository mockBasicDetailsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBasicDetailsMockMvc;

    private BasicDetails basicDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BasicDetails createEntity(EntityManager em) {
        BasicDetails basicDetails = new BasicDetails()
            .jobRole(DEFAULT_JOB_ROLE)
            .workFromHome(DEFAULT_WORK_FROM_HOME)
            .type(DEFAULT_TYPE)
            .minSalary(DEFAULT_MIN_SALARY)
            .maxSalRY(DEFAULT_MAX_SAL_RY)
            .openings(DEFAULT_OPENINGS)
            .workingDays(DEFAULT_WORKING_DAYS)
            .workTimings(DEFAULT_WORK_TIMINGS)
            .minEducation(DEFAULT_MIN_EDUCATION)
            .experience(DEFAULT_EXPERIENCE)
            .gender(DEFAULT_GENDER);
        return basicDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BasicDetails createUpdatedEntity(EntityManager em) {
        BasicDetails basicDetails = new BasicDetails()
            .jobRole(UPDATED_JOB_ROLE)
            .workFromHome(UPDATED_WORK_FROM_HOME)
            .type(UPDATED_TYPE)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalRY(UPDATED_MAX_SAL_RY)
            .openings(UPDATED_OPENINGS)
            .workingDays(UPDATED_WORKING_DAYS)
            .workTimings(UPDATED_WORK_TIMINGS)
            .minEducation(UPDATED_MIN_EDUCATION)
            .experience(UPDATED_EXPERIENCE)
            .gender(UPDATED_GENDER);
        return basicDetails;
    }

    @BeforeEach
    public void initTest() {
        basicDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createBasicDetails() throws Exception {
        int databaseSizeBeforeCreate = basicDetailsRepository.findAll().size();
        // Create the BasicDetails
        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isCreated());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        BasicDetails testBasicDetails = basicDetailsList.get(basicDetailsList.size() - 1);
        assertThat(testBasicDetails.getJobRole()).isEqualTo(DEFAULT_JOB_ROLE);
        assertThat(testBasicDetails.getWorkFromHome()).isEqualTo(DEFAULT_WORK_FROM_HOME);
        assertThat(testBasicDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBasicDetails.getMinSalary()).isEqualTo(DEFAULT_MIN_SALARY);
        assertThat(testBasicDetails.getMaxSalRY()).isEqualTo(DEFAULT_MAX_SAL_RY);
        assertThat(testBasicDetails.getOpenings()).isEqualTo(DEFAULT_OPENINGS);
        assertThat(testBasicDetails.getWorkingDays()).isEqualTo(DEFAULT_WORKING_DAYS);
        assertThat(testBasicDetails.getWorkTimings()).isEqualTo(DEFAULT_WORK_TIMINGS);
        assertThat(testBasicDetails.getMinEducation()).isEqualTo(DEFAULT_MIN_EDUCATION);
        assertThat(testBasicDetails.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testBasicDetails.getGender()).isEqualTo(DEFAULT_GENDER);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository, times(1)).save(testBasicDetails);
    }

    @Test
    @Transactional
    void createBasicDetailsWithExistingId() throws Exception {
        // Create the BasicDetails with an existing ID
        basicDetails.setId(1L);

        int databaseSizeBeforeCreate = basicDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository, times(0)).save(basicDetails);
    }

    @Test
    @Transactional
    void checkJobRoleIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicDetailsRepository.findAll().size();
        // set the field null
        basicDetails.setJobRole(null);

        // Create the BasicDetails, which fails.

        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorkFromHomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicDetailsRepository.findAll().size();
        // set the field null
        basicDetails.setWorkFromHome(null);

        // Create the BasicDetails, which fails.

        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicDetailsRepository.findAll().size();
        // set the field null
        basicDetails.setType(null);

        // Create the BasicDetails, which fails.

        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOpeningsIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicDetailsRepository.findAll().size();
        // set the field null
        basicDetails.setOpenings(null);

        // Create the BasicDetails, which fails.

        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorkingDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicDetailsRepository.findAll().size();
        // set the field null
        basicDetails.setWorkingDays(null);

        // Create the BasicDetails, which fails.

        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorkTimingsIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicDetailsRepository.findAll().size();
        // set the field null
        basicDetails.setWorkTimings(null);

        // Create the BasicDetails, which fails.

        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinEducationIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicDetailsRepository.findAll().size();
        // set the field null
        basicDetails.setMinEducation(null);

        // Create the BasicDetails, which fails.

        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExperienceIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicDetailsRepository.findAll().size();
        // set the field null
        basicDetails.setExperience(null);

        // Create the BasicDetails, which fails.

        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = basicDetailsRepository.findAll().size();
        // set the field null
        basicDetails.setGender(null);

        // Create the BasicDetails, which fails.

        restBasicDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isBadRequest());

        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBasicDetails() throws Exception {
        // Initialize the database
        basicDetailsRepository.saveAndFlush(basicDetails);

        // Get all the basicDetailsList
        restBasicDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(basicDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobRole").value(hasItem(DEFAULT_JOB_ROLE)))
            .andExpect(jsonPath("$.[*].workFromHome").value(hasItem(DEFAULT_WORK_FROM_HOME.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].maxSalRY").value(hasItem(DEFAULT_MAX_SAL_RY.intValue())))
            .andExpect(jsonPath("$.[*].openings").value(hasItem(DEFAULT_OPENINGS)))
            .andExpect(jsonPath("$.[*].workingDays").value(hasItem(DEFAULT_WORKING_DAYS)))
            .andExpect(jsonPath("$.[*].workTimings").value(hasItem(DEFAULT_WORK_TIMINGS)))
            .andExpect(jsonPath("$.[*].minEducation").value(hasItem(DEFAULT_MIN_EDUCATION.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }

    @Test
    @Transactional
    void getBasicDetails() throws Exception {
        // Initialize the database
        basicDetailsRepository.saveAndFlush(basicDetails);

        // Get the basicDetails
        restBasicDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, basicDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(basicDetails.getId().intValue()))
            .andExpect(jsonPath("$.jobRole").value(DEFAULT_JOB_ROLE))
            .andExpect(jsonPath("$.workFromHome").value(DEFAULT_WORK_FROM_HOME.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.minSalary").value(DEFAULT_MIN_SALARY.intValue()))
            .andExpect(jsonPath("$.maxSalRY").value(DEFAULT_MAX_SAL_RY.intValue()))
            .andExpect(jsonPath("$.openings").value(DEFAULT_OPENINGS))
            .andExpect(jsonPath("$.workingDays").value(DEFAULT_WORKING_DAYS))
            .andExpect(jsonPath("$.workTimings").value(DEFAULT_WORK_TIMINGS))
            .andExpect(jsonPath("$.minEducation").value(DEFAULT_MIN_EDUCATION.toString()))
            .andExpect(jsonPath("$.experience").value(DEFAULT_EXPERIENCE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBasicDetails() throws Exception {
        // Get the basicDetails
        restBasicDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBasicDetails() throws Exception {
        // Initialize the database
        basicDetailsRepository.saveAndFlush(basicDetails);

        int databaseSizeBeforeUpdate = basicDetailsRepository.findAll().size();

        // Update the basicDetails
        BasicDetails updatedBasicDetails = basicDetailsRepository.findById(basicDetails.getId()).get();
        // Disconnect from session so that the updates on updatedBasicDetails are not directly saved in db
        em.detach(updatedBasicDetails);
        updatedBasicDetails
            .jobRole(UPDATED_JOB_ROLE)
            .workFromHome(UPDATED_WORK_FROM_HOME)
            .type(UPDATED_TYPE)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalRY(UPDATED_MAX_SAL_RY)
            .openings(UPDATED_OPENINGS)
            .workingDays(UPDATED_WORKING_DAYS)
            .workTimings(UPDATED_WORK_TIMINGS)
            .minEducation(UPDATED_MIN_EDUCATION)
            .experience(UPDATED_EXPERIENCE)
            .gender(UPDATED_GENDER);

        restBasicDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBasicDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBasicDetails))
            )
            .andExpect(status().isOk());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeUpdate);
        BasicDetails testBasicDetails = basicDetailsList.get(basicDetailsList.size() - 1);
        assertThat(testBasicDetails.getJobRole()).isEqualTo(UPDATED_JOB_ROLE);
        assertThat(testBasicDetails.getWorkFromHome()).isEqualTo(UPDATED_WORK_FROM_HOME);
        assertThat(testBasicDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBasicDetails.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testBasicDetails.getMaxSalRY()).isEqualTo(UPDATED_MAX_SAL_RY);
        assertThat(testBasicDetails.getOpenings()).isEqualTo(UPDATED_OPENINGS);
        assertThat(testBasicDetails.getWorkingDays()).isEqualTo(UPDATED_WORKING_DAYS);
        assertThat(testBasicDetails.getWorkTimings()).isEqualTo(UPDATED_WORK_TIMINGS);
        assertThat(testBasicDetails.getMinEducation()).isEqualTo(UPDATED_MIN_EDUCATION);
        assertThat(testBasicDetails.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testBasicDetails.getGender()).isEqualTo(UPDATED_GENDER);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository).save(testBasicDetails);
    }

    @Test
    @Transactional
    void putNonExistingBasicDetails() throws Exception {
        int databaseSizeBeforeUpdate = basicDetailsRepository.findAll().size();
        basicDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBasicDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, basicDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basicDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository, times(0)).save(basicDetails);
    }

    @Test
    @Transactional
    void putWithIdMismatchBasicDetails() throws Exception {
        int databaseSizeBeforeUpdate = basicDetailsRepository.findAll().size();
        basicDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasicDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basicDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository, times(0)).save(basicDetails);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBasicDetails() throws Exception {
        int databaseSizeBeforeUpdate = basicDetailsRepository.findAll().size();
        basicDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasicDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basicDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository, times(0)).save(basicDetails);
    }

    @Test
    @Transactional
    void partialUpdateBasicDetailsWithPatch() throws Exception {
        // Initialize the database
        basicDetailsRepository.saveAndFlush(basicDetails);

        int databaseSizeBeforeUpdate = basicDetailsRepository.findAll().size();

        // Update the basicDetails using partial update
        BasicDetails partialUpdatedBasicDetails = new BasicDetails();
        partialUpdatedBasicDetails.setId(basicDetails.getId());

        partialUpdatedBasicDetails
            .jobRole(UPDATED_JOB_ROLE)
            .type(UPDATED_TYPE)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalRY(UPDATED_MAX_SAL_RY)
            .openings(UPDATED_OPENINGS)
            .workingDays(UPDATED_WORKING_DAYS)
            .minEducation(UPDATED_MIN_EDUCATION)
            .gender(UPDATED_GENDER);

        restBasicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBasicDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBasicDetails))
            )
            .andExpect(status().isOk());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeUpdate);
        BasicDetails testBasicDetails = basicDetailsList.get(basicDetailsList.size() - 1);
        assertThat(testBasicDetails.getJobRole()).isEqualTo(UPDATED_JOB_ROLE);
        assertThat(testBasicDetails.getWorkFromHome()).isEqualTo(DEFAULT_WORK_FROM_HOME);
        assertThat(testBasicDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBasicDetails.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testBasicDetails.getMaxSalRY()).isEqualTo(UPDATED_MAX_SAL_RY);
        assertThat(testBasicDetails.getOpenings()).isEqualTo(UPDATED_OPENINGS);
        assertThat(testBasicDetails.getWorkingDays()).isEqualTo(UPDATED_WORKING_DAYS);
        assertThat(testBasicDetails.getWorkTimings()).isEqualTo(DEFAULT_WORK_TIMINGS);
        assertThat(testBasicDetails.getMinEducation()).isEqualTo(UPDATED_MIN_EDUCATION);
        assertThat(testBasicDetails.getExperience()).isEqualTo(DEFAULT_EXPERIENCE);
        assertThat(testBasicDetails.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void fullUpdateBasicDetailsWithPatch() throws Exception {
        // Initialize the database
        basicDetailsRepository.saveAndFlush(basicDetails);

        int databaseSizeBeforeUpdate = basicDetailsRepository.findAll().size();

        // Update the basicDetails using partial update
        BasicDetails partialUpdatedBasicDetails = new BasicDetails();
        partialUpdatedBasicDetails.setId(basicDetails.getId());

        partialUpdatedBasicDetails
            .jobRole(UPDATED_JOB_ROLE)
            .workFromHome(UPDATED_WORK_FROM_HOME)
            .type(UPDATED_TYPE)
            .minSalary(UPDATED_MIN_SALARY)
            .maxSalRY(UPDATED_MAX_SAL_RY)
            .openings(UPDATED_OPENINGS)
            .workingDays(UPDATED_WORKING_DAYS)
            .workTimings(UPDATED_WORK_TIMINGS)
            .minEducation(UPDATED_MIN_EDUCATION)
            .experience(UPDATED_EXPERIENCE)
            .gender(UPDATED_GENDER);

        restBasicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBasicDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBasicDetails))
            )
            .andExpect(status().isOk());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeUpdate);
        BasicDetails testBasicDetails = basicDetailsList.get(basicDetailsList.size() - 1);
        assertThat(testBasicDetails.getJobRole()).isEqualTo(UPDATED_JOB_ROLE);
        assertThat(testBasicDetails.getWorkFromHome()).isEqualTo(UPDATED_WORK_FROM_HOME);
        assertThat(testBasicDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBasicDetails.getMinSalary()).isEqualTo(UPDATED_MIN_SALARY);
        assertThat(testBasicDetails.getMaxSalRY()).isEqualTo(UPDATED_MAX_SAL_RY);
        assertThat(testBasicDetails.getOpenings()).isEqualTo(UPDATED_OPENINGS);
        assertThat(testBasicDetails.getWorkingDays()).isEqualTo(UPDATED_WORKING_DAYS);
        assertThat(testBasicDetails.getWorkTimings()).isEqualTo(UPDATED_WORK_TIMINGS);
        assertThat(testBasicDetails.getMinEducation()).isEqualTo(UPDATED_MIN_EDUCATION);
        assertThat(testBasicDetails.getExperience()).isEqualTo(UPDATED_EXPERIENCE);
        assertThat(testBasicDetails.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void patchNonExistingBasicDetails() throws Exception {
        int databaseSizeBeforeUpdate = basicDetailsRepository.findAll().size();
        basicDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBasicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, basicDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basicDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository, times(0)).save(basicDetails);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBasicDetails() throws Exception {
        int databaseSizeBeforeUpdate = basicDetailsRepository.findAll().size();
        basicDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basicDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository, times(0)).save(basicDetails);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBasicDetails() throws Exception {
        int databaseSizeBeforeUpdate = basicDetailsRepository.findAll().size();
        basicDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(basicDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BasicDetails in the database
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository, times(0)).save(basicDetails);
    }

    @Test
    @Transactional
    void deleteBasicDetails() throws Exception {
        // Initialize the database
        basicDetailsRepository.saveAndFlush(basicDetails);

        int databaseSizeBeforeDelete = basicDetailsRepository.findAll().size();

        // Delete the basicDetails
        restBasicDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, basicDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BasicDetails> basicDetailsList = basicDetailsRepository.findAll();
        assertThat(basicDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BasicDetails in Elasticsearch
        verify(mockBasicDetailsSearchRepository, times(1)).deleteById(basicDetails.getId());
    }

    @Test
    @Transactional
    void searchBasicDetails() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        basicDetailsRepository.saveAndFlush(basicDetails);
        when(mockBasicDetailsSearchRepository.search(queryStringQuery("id:" + basicDetails.getId())))
            .thenReturn(Collections.singletonList(basicDetails));

        // Search the basicDetails
        restBasicDetailsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + basicDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(basicDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobRole").value(hasItem(DEFAULT_JOB_ROLE)))
            .andExpect(jsonPath("$.[*].workFromHome").value(hasItem(DEFAULT_WORK_FROM_HOME.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].minSalary").value(hasItem(DEFAULT_MIN_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].maxSalRY").value(hasItem(DEFAULT_MAX_SAL_RY.intValue())))
            .andExpect(jsonPath("$.[*].openings").value(hasItem(DEFAULT_OPENINGS)))
            .andExpect(jsonPath("$.[*].workingDays").value(hasItem(DEFAULT_WORKING_DAYS)))
            .andExpect(jsonPath("$.[*].workTimings").value(hasItem(DEFAULT_WORK_TIMINGS)))
            .andExpect(jsonPath("$.[*].minEducation").value(hasItem(DEFAULT_MIN_EDUCATION.toString())))
            .andExpect(jsonPath("$.[*].experience").value(hasItem(DEFAULT_EXPERIENCE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }
}
