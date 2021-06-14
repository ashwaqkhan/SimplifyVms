package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.JobDetails;
import com.mycompany.myapp.domain.enumeration.DepositCharged;
import com.mycompany.myapp.domain.enumeration.ReqEnglish;
import com.mycompany.myapp.domain.enumeration.SkillReq;
import com.mycompany.myapp.repository.JobDetailsRepository;
import com.mycompany.myapp.repository.search.JobDetailsSearchRepository;
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
 * Integration tests for the {@link JobDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobDetailsResourceIT {

    private static final SkillReq DEFAULT_REQUIRED_SKILLS = SkillReq.Computer_or_Laptop_Ownersip;
    private static final SkillReq UPDATED_REQUIRED_SKILLS = SkillReq.Wifi_Or_Internet;

    private static final ReqEnglish DEFAULT_ENGLISH = ReqEnglish.NOEnglish;
    private static final ReqEnglish UPDATED_ENGLISH = ReqEnglish.ThodaEnglish;

    private static final String DEFAULT_JOB_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_JOB_DESCRIPTION = "BBBBBBBBBB";

    private static final DepositCharged DEFAULT_SECURITY_DEPOSIT_CHARGED = DepositCharged.Yes;
    private static final DepositCharged UPDATED_SECURITY_DEPOSIT_CHARGED = DepositCharged.No;

    private static final String ENTITY_API_URL = "/api/job-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/job-details";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobDetailsRepository jobDetailsRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.JobDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private JobDetailsSearchRepository mockJobDetailsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobDetailsMockMvc;

    private JobDetails jobDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobDetails createEntity(EntityManager em) {
        JobDetails jobDetails = new JobDetails()
            .requiredSkills(DEFAULT_REQUIRED_SKILLS)
            .english(DEFAULT_ENGLISH)
            .jobDescription(DEFAULT_JOB_DESCRIPTION)
            .securityDepositCharged(DEFAULT_SECURITY_DEPOSIT_CHARGED);
        return jobDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobDetails createUpdatedEntity(EntityManager em) {
        JobDetails jobDetails = new JobDetails()
            .requiredSkills(UPDATED_REQUIRED_SKILLS)
            .english(UPDATED_ENGLISH)
            .jobDescription(UPDATED_JOB_DESCRIPTION)
            .securityDepositCharged(UPDATED_SECURITY_DEPOSIT_CHARGED);
        return jobDetails;
    }

    @BeforeEach
    public void initTest() {
        jobDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createJobDetails() throws Exception {
        int databaseSizeBeforeCreate = jobDetailsRepository.findAll().size();
        // Create the JobDetails
        restJobDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isCreated());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        JobDetails testJobDetails = jobDetailsList.get(jobDetailsList.size() - 1);
        assertThat(testJobDetails.getRequiredSkills()).isEqualTo(DEFAULT_REQUIRED_SKILLS);
        assertThat(testJobDetails.getEnglish()).isEqualTo(DEFAULT_ENGLISH);
        assertThat(testJobDetails.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
        assertThat(testJobDetails.getSecurityDepositCharged()).isEqualTo(DEFAULT_SECURITY_DEPOSIT_CHARGED);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository, times(1)).save(testJobDetails);
    }

    @Test
    @Transactional
    void createJobDetailsWithExistingId() throws Exception {
        // Create the JobDetails with an existing ID
        jobDetails.setId(1L);

        int databaseSizeBeforeCreate = jobDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository, times(0)).save(jobDetails);
    }

    @Test
    @Transactional
    void checkRequiredSkillsIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobDetailsRepository.findAll().size();
        // set the field null
        jobDetails.setRequiredSkills(null);

        // Create the JobDetails, which fails.

        restJobDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnglishIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobDetailsRepository.findAll().size();
        // set the field null
        jobDetails.setEnglish(null);

        // Create the JobDetails, which fails.

        restJobDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkJobDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobDetailsRepository.findAll().size();
        // set the field null
        jobDetails.setJobDescription(null);

        // Create the JobDetails, which fails.

        restJobDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSecurityDepositChargedIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobDetailsRepository.findAll().size();
        // set the field null
        jobDetails.setSecurityDepositCharged(null);

        // Create the JobDetails, which fails.

        restJobDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isBadRequest());

        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get all the jobDetailsList
        restJobDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].requiredSkills").value(hasItem(DEFAULT_REQUIRED_SKILLS.toString())))
            .andExpect(jsonPath("$.[*].english").value(hasItem(DEFAULT_ENGLISH.toString())))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].securityDepositCharged").value(hasItem(DEFAULT_SECURITY_DEPOSIT_CHARGED.toString())));
    }

    @Test
    @Transactional
    void getJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        // Get the jobDetails
        restJobDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, jobDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobDetails.getId().intValue()))
            .andExpect(jsonPath("$.requiredSkills").value(DEFAULT_REQUIRED_SKILLS.toString()))
            .andExpect(jsonPath("$.english").value(DEFAULT_ENGLISH.toString()))
            .andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION))
            .andExpect(jsonPath("$.securityDepositCharged").value(DEFAULT_SECURITY_DEPOSIT_CHARGED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingJobDetails() throws Exception {
        // Get the jobDetails
        restJobDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();

        // Update the jobDetails
        JobDetails updatedJobDetails = jobDetailsRepository.findById(jobDetails.getId()).get();
        // Disconnect from session so that the updates on updatedJobDetails are not directly saved in db
        em.detach(updatedJobDetails);
        updatedJobDetails
            .requiredSkills(UPDATED_REQUIRED_SKILLS)
            .english(UPDATED_ENGLISH)
            .jobDescription(UPDATED_JOB_DESCRIPTION)
            .securityDepositCharged(UPDATED_SECURITY_DEPOSIT_CHARGED);

        restJobDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJobDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJobDetails))
            )
            .andExpect(status().isOk());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);
        JobDetails testJobDetails = jobDetailsList.get(jobDetailsList.size() - 1);
        assertThat(testJobDetails.getRequiredSkills()).isEqualTo(UPDATED_REQUIRED_SKILLS);
        assertThat(testJobDetails.getEnglish()).isEqualTo(UPDATED_ENGLISH);
        assertThat(testJobDetails.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
        assertThat(testJobDetails.getSecurityDepositCharged()).isEqualTo(UPDATED_SECURITY_DEPOSIT_CHARGED);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository).save(testJobDetails);
    }

    @Test
    @Transactional
    void putNonExistingJobDetails() throws Exception {
        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();
        jobDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository, times(0)).save(jobDetails);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobDetails() throws Exception {
        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();
        jobDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository, times(0)).save(jobDetails);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobDetails() throws Exception {
        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();
        jobDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository, times(0)).save(jobDetails);
    }

    @Test
    @Transactional
    void partialUpdateJobDetailsWithPatch() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();

        // Update the jobDetails using partial update
        JobDetails partialUpdatedJobDetails = new JobDetails();
        partialUpdatedJobDetails.setId(jobDetails.getId());

        partialUpdatedJobDetails.requiredSkills(UPDATED_REQUIRED_SKILLS).jobDescription(UPDATED_JOB_DESCRIPTION);

        restJobDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobDetails))
            )
            .andExpect(status().isOk());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);
        JobDetails testJobDetails = jobDetailsList.get(jobDetailsList.size() - 1);
        assertThat(testJobDetails.getRequiredSkills()).isEqualTo(UPDATED_REQUIRED_SKILLS);
        assertThat(testJobDetails.getEnglish()).isEqualTo(DEFAULT_ENGLISH);
        assertThat(testJobDetails.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
        assertThat(testJobDetails.getSecurityDepositCharged()).isEqualTo(DEFAULT_SECURITY_DEPOSIT_CHARGED);
    }

    @Test
    @Transactional
    void fullUpdateJobDetailsWithPatch() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();

        // Update the jobDetails using partial update
        JobDetails partialUpdatedJobDetails = new JobDetails();
        partialUpdatedJobDetails.setId(jobDetails.getId());

        partialUpdatedJobDetails
            .requiredSkills(UPDATED_REQUIRED_SKILLS)
            .english(UPDATED_ENGLISH)
            .jobDescription(UPDATED_JOB_DESCRIPTION)
            .securityDepositCharged(UPDATED_SECURITY_DEPOSIT_CHARGED);

        restJobDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobDetails))
            )
            .andExpect(status().isOk());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);
        JobDetails testJobDetails = jobDetailsList.get(jobDetailsList.size() - 1);
        assertThat(testJobDetails.getRequiredSkills()).isEqualTo(UPDATED_REQUIRED_SKILLS);
        assertThat(testJobDetails.getEnglish()).isEqualTo(UPDATED_ENGLISH);
        assertThat(testJobDetails.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
        assertThat(testJobDetails.getSecurityDepositCharged()).isEqualTo(UPDATED_SECURITY_DEPOSIT_CHARGED);
    }

    @Test
    @Transactional
    void patchNonExistingJobDetails() throws Exception {
        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();
        jobDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository, times(0)).save(jobDetails);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobDetails() throws Exception {
        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();
        jobDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository, times(0)).save(jobDetails);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobDetails() throws Exception {
        int databaseSizeBeforeUpdate = jobDetailsRepository.findAll().size();
        jobDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobDetails in the database
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository, times(0)).save(jobDetails);
    }

    @Test
    @Transactional
    void deleteJobDetails() throws Exception {
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);

        int databaseSizeBeforeDelete = jobDetailsRepository.findAll().size();

        // Delete the jobDetails
        restJobDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobDetails> jobDetailsList = jobDetailsRepository.findAll();
        assertThat(jobDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the JobDetails in Elasticsearch
        verify(mockJobDetailsSearchRepository, times(1)).deleteById(jobDetails.getId());
    }

    @Test
    @Transactional
    void searchJobDetails() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        jobDetailsRepository.saveAndFlush(jobDetails);
        when(mockJobDetailsSearchRepository.search(queryStringQuery("id:" + jobDetails.getId())))
            .thenReturn(Collections.singletonList(jobDetails));

        // Search the jobDetails
        restJobDetailsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + jobDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].requiredSkills").value(hasItem(DEFAULT_REQUIRED_SKILLS.toString())))
            .andExpect(jsonPath("$.[*].english").value(hasItem(DEFAULT_ENGLISH.toString())))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].securityDepositCharged").value(hasItem(DEFAULT_SECURITY_DEPOSIT_CHARGED.toString())));
    }
}
