package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.InterviewInformation;
import com.mycompany.myapp.domain.enumeration.Application;
import com.mycompany.myapp.repository.InterviewInformationRepository;
import com.mycompany.myapp.repository.search.InterviewInformationSearchRepository;
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
 * Integration tests for the {@link InterviewInformationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InterviewInformationResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECRUITERS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECRUITERS_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_H_RWHATSAPP_NUMBER = 1L;
    private static final Long UPDATED_H_RWHATSAPP_NUMBER = 2L;

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_BUILDING_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUILDING_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_AREA = "AAAAAAAAAA";
    private static final String UPDATED_AREA = "BBBBBBBBBB";

    private static final Application DEFAULT_RECIEVE_APPLICATIONS_FROM = Application.EntireCity;
    private static final Application UPDATED_RECIEVE_APPLICATIONS_FROM = Application.EntireIndia;

    private static final String ENTITY_API_URL = "/api/interview-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/interview-informations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterviewInformationRepository interviewInformationRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.InterviewInformationSearchRepositoryMockConfiguration
     */
    @Autowired
    private InterviewInformationSearchRepository mockInterviewInformationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterviewInformationMockMvc;

    private InterviewInformation interviewInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterviewInformation createEntity(EntityManager em) {
        InterviewInformation interviewInformation = new InterviewInformation()
            .companyName(DEFAULT_COMPANY_NAME)
            .recruitersName(DEFAULT_RECRUITERS_NAME)
            .hRwhatsappNumber(DEFAULT_H_RWHATSAPP_NUMBER)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .buildingName(DEFAULT_BUILDING_NAME)
            .city(DEFAULT_CITY)
            .area(DEFAULT_AREA)
            .recieveApplicationsFrom(DEFAULT_RECIEVE_APPLICATIONS_FROM);
        return interviewInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterviewInformation createUpdatedEntity(EntityManager em) {
        InterviewInformation interviewInformation = new InterviewInformation()
            .companyName(UPDATED_COMPANY_NAME)
            .recruitersName(UPDATED_RECRUITERS_NAME)
            .hRwhatsappNumber(UPDATED_H_RWHATSAPP_NUMBER)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .buildingName(UPDATED_BUILDING_NAME)
            .city(UPDATED_CITY)
            .area(UPDATED_AREA)
            .recieveApplicationsFrom(UPDATED_RECIEVE_APPLICATIONS_FROM);
        return interviewInformation;
    }

    @BeforeEach
    public void initTest() {
        interviewInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createInterviewInformation() throws Exception {
        int databaseSizeBeforeCreate = interviewInformationRepository.findAll().size();
        // Create the InterviewInformation
        restInterviewInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interviewInformation))
            )
            .andExpect(status().isCreated());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeCreate + 1);
        InterviewInformation testInterviewInformation = interviewInformationList.get(interviewInformationList.size() - 1);
        assertThat(testInterviewInformation.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testInterviewInformation.getRecruitersName()).isEqualTo(DEFAULT_RECRUITERS_NAME);
        assertThat(testInterviewInformation.gethRwhatsappNumber()).isEqualTo(DEFAULT_H_RWHATSAPP_NUMBER);
        assertThat(testInterviewInformation.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testInterviewInformation.getBuildingName()).isEqualTo(DEFAULT_BUILDING_NAME);
        assertThat(testInterviewInformation.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testInterviewInformation.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testInterviewInformation.getRecieveApplicationsFrom()).isEqualTo(DEFAULT_RECIEVE_APPLICATIONS_FROM);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository, times(1)).save(testInterviewInformation);
    }

    @Test
    @Transactional
    void createInterviewInformationWithExistingId() throws Exception {
        // Create the InterviewInformation with an existing ID
        interviewInformation.setId(1L);

        int databaseSizeBeforeCreate = interviewInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterviewInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interviewInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeCreate);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository, times(0)).save(interviewInformation);
    }

    @Test
    @Transactional
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = interviewInformationRepository.findAll().size();
        // set the field null
        interviewInformation.setCompanyName(null);

        // Create the InterviewInformation, which fails.

        restInterviewInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interviewInformation))
            )
            .andExpect(status().isBadRequest());

        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterviewInformations() throws Exception {
        // Initialize the database
        interviewInformationRepository.saveAndFlush(interviewInformation);

        // Get all the interviewInformationList
        restInterviewInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interviewInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].recruitersName").value(hasItem(DEFAULT_RECRUITERS_NAME)))
            .andExpect(jsonPath("$.[*].hRwhatsappNumber").value(hasItem(DEFAULT_H_RWHATSAPP_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].buildingName").value(hasItem(DEFAULT_BUILDING_NAME)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].recieveApplicationsFrom").value(hasItem(DEFAULT_RECIEVE_APPLICATIONS_FROM.toString())));
    }

    @Test
    @Transactional
    void getInterviewInformation() throws Exception {
        // Initialize the database
        interviewInformationRepository.saveAndFlush(interviewInformation);

        // Get the interviewInformation
        restInterviewInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, interviewInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interviewInformation.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.recruitersName").value(DEFAULT_RECRUITERS_NAME))
            .andExpect(jsonPath("$.hRwhatsappNumber").value(DEFAULT_H_RWHATSAPP_NUMBER.intValue()))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL))
            .andExpect(jsonPath("$.buildingName").value(DEFAULT_BUILDING_NAME))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA))
            .andExpect(jsonPath("$.recieveApplicationsFrom").value(DEFAULT_RECIEVE_APPLICATIONS_FROM.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInterviewInformation() throws Exception {
        // Get the interviewInformation
        restInterviewInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInterviewInformation() throws Exception {
        // Initialize the database
        interviewInformationRepository.saveAndFlush(interviewInformation);

        int databaseSizeBeforeUpdate = interviewInformationRepository.findAll().size();

        // Update the interviewInformation
        InterviewInformation updatedInterviewInformation = interviewInformationRepository.findById(interviewInformation.getId()).get();
        // Disconnect from session so that the updates on updatedInterviewInformation are not directly saved in db
        em.detach(updatedInterviewInformation);
        updatedInterviewInformation
            .companyName(UPDATED_COMPANY_NAME)
            .recruitersName(UPDATED_RECRUITERS_NAME)
            .hRwhatsappNumber(UPDATED_H_RWHATSAPP_NUMBER)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .buildingName(UPDATED_BUILDING_NAME)
            .city(UPDATED_CITY)
            .area(UPDATED_AREA)
            .recieveApplicationsFrom(UPDATED_RECIEVE_APPLICATIONS_FROM);

        restInterviewInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInterviewInformation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInterviewInformation))
            )
            .andExpect(status().isOk());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeUpdate);
        InterviewInformation testInterviewInformation = interviewInformationList.get(interviewInformationList.size() - 1);
        assertThat(testInterviewInformation.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testInterviewInformation.getRecruitersName()).isEqualTo(UPDATED_RECRUITERS_NAME);
        assertThat(testInterviewInformation.gethRwhatsappNumber()).isEqualTo(UPDATED_H_RWHATSAPP_NUMBER);
        assertThat(testInterviewInformation.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testInterviewInformation.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testInterviewInformation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testInterviewInformation.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testInterviewInformation.getRecieveApplicationsFrom()).isEqualTo(UPDATED_RECIEVE_APPLICATIONS_FROM);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository).save(testInterviewInformation);
    }

    @Test
    @Transactional
    void putNonExistingInterviewInformation() throws Exception {
        int databaseSizeBeforeUpdate = interviewInformationRepository.findAll().size();
        interviewInformation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interviewInformation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interviewInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository, times(0)).save(interviewInformation);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterviewInformation() throws Exception {
        int databaseSizeBeforeUpdate = interviewInformationRepository.findAll().size();
        interviewInformation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interviewInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository, times(0)).save(interviewInformation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterviewInformation() throws Exception {
        int databaseSizeBeforeUpdate = interviewInformationRepository.findAll().size();
        interviewInformation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewInformationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interviewInformation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository, times(0)).save(interviewInformation);
    }

    @Test
    @Transactional
    void partialUpdateInterviewInformationWithPatch() throws Exception {
        // Initialize the database
        interviewInformationRepository.saveAndFlush(interviewInformation);

        int databaseSizeBeforeUpdate = interviewInformationRepository.findAll().size();

        // Update the interviewInformation using partial update
        InterviewInformation partialUpdatedInterviewInformation = new InterviewInformation();
        partialUpdatedInterviewInformation.setId(interviewInformation.getId());

        partialUpdatedInterviewInformation.buildingName(UPDATED_BUILDING_NAME).area(UPDATED_AREA);

        restInterviewInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterviewInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterviewInformation))
            )
            .andExpect(status().isOk());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeUpdate);
        InterviewInformation testInterviewInformation = interviewInformationList.get(interviewInformationList.size() - 1);
        assertThat(testInterviewInformation.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testInterviewInformation.getRecruitersName()).isEqualTo(DEFAULT_RECRUITERS_NAME);
        assertThat(testInterviewInformation.gethRwhatsappNumber()).isEqualTo(DEFAULT_H_RWHATSAPP_NUMBER);
        assertThat(testInterviewInformation.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testInterviewInformation.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testInterviewInformation.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testInterviewInformation.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testInterviewInformation.getRecieveApplicationsFrom()).isEqualTo(DEFAULT_RECIEVE_APPLICATIONS_FROM);
    }

    @Test
    @Transactional
    void fullUpdateInterviewInformationWithPatch() throws Exception {
        // Initialize the database
        interviewInformationRepository.saveAndFlush(interviewInformation);

        int databaseSizeBeforeUpdate = interviewInformationRepository.findAll().size();

        // Update the interviewInformation using partial update
        InterviewInformation partialUpdatedInterviewInformation = new InterviewInformation();
        partialUpdatedInterviewInformation.setId(interviewInformation.getId());

        partialUpdatedInterviewInformation
            .companyName(UPDATED_COMPANY_NAME)
            .recruitersName(UPDATED_RECRUITERS_NAME)
            .hRwhatsappNumber(UPDATED_H_RWHATSAPP_NUMBER)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .buildingName(UPDATED_BUILDING_NAME)
            .city(UPDATED_CITY)
            .area(UPDATED_AREA)
            .recieveApplicationsFrom(UPDATED_RECIEVE_APPLICATIONS_FROM);

        restInterviewInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterviewInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterviewInformation))
            )
            .andExpect(status().isOk());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeUpdate);
        InterviewInformation testInterviewInformation = interviewInformationList.get(interviewInformationList.size() - 1);
        assertThat(testInterviewInformation.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testInterviewInformation.getRecruitersName()).isEqualTo(UPDATED_RECRUITERS_NAME);
        assertThat(testInterviewInformation.gethRwhatsappNumber()).isEqualTo(UPDATED_H_RWHATSAPP_NUMBER);
        assertThat(testInterviewInformation.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testInterviewInformation.getBuildingName()).isEqualTo(UPDATED_BUILDING_NAME);
        assertThat(testInterviewInformation.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testInterviewInformation.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testInterviewInformation.getRecieveApplicationsFrom()).isEqualTo(UPDATED_RECIEVE_APPLICATIONS_FROM);
    }

    @Test
    @Transactional
    void patchNonExistingInterviewInformation() throws Exception {
        int databaseSizeBeforeUpdate = interviewInformationRepository.findAll().size();
        interviewInformation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interviewInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interviewInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository, times(0)).save(interviewInformation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterviewInformation() throws Exception {
        int databaseSizeBeforeUpdate = interviewInformationRepository.findAll().size();
        interviewInformation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interviewInformation))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository, times(0)).save(interviewInformation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterviewInformation() throws Exception {
        int databaseSizeBeforeUpdate = interviewInformationRepository.findAll().size();
        interviewInformation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interviewInformation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterviewInformation in the database
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository, times(0)).save(interviewInformation);
    }

    @Test
    @Transactional
    void deleteInterviewInformation() throws Exception {
        // Initialize the database
        interviewInformationRepository.saveAndFlush(interviewInformation);

        int databaseSizeBeforeDelete = interviewInformationRepository.findAll().size();

        // Delete the interviewInformation
        restInterviewInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, interviewInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterviewInformation> interviewInformationList = interviewInformationRepository.findAll();
        assertThat(interviewInformationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InterviewInformation in Elasticsearch
        verify(mockInterviewInformationSearchRepository, times(1)).deleteById(interviewInformation.getId());
    }

    @Test
    @Transactional
    void searchInterviewInformation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        interviewInformationRepository.saveAndFlush(interviewInformation);
        when(mockInterviewInformationSearchRepository.search(queryStringQuery("id:" + interviewInformation.getId())))
            .thenReturn(Collections.singletonList(interviewInformation));

        // Search the interviewInformation
        restInterviewInformationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + interviewInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interviewInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].recruitersName").value(hasItem(DEFAULT_RECRUITERS_NAME)))
            .andExpect(jsonPath("$.[*].hRwhatsappNumber").value(hasItem(DEFAULT_H_RWHATSAPP_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)))
            .andExpect(jsonPath("$.[*].buildingName").value(hasItem(DEFAULT_BUILDING_NAME)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA)))
            .andExpect(jsonPath("$.[*].recieveApplicationsFrom").value(hasItem(DEFAULT_RECIEVE_APPLICATIONS_FROM.toString())));
    }
}
