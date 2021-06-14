package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Apply;
import com.mycompany.myapp.repository.ApplyRepository;
import com.mycompany.myapp.repository.search.ApplySearchRepository;
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
 * Integration tests for the {@link ApplyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ApplyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_MOBILE_NO = 1L;
    private static final Long UPDATED_MOBILE_NO = 2L;

    private static final String ENTITY_API_URL = "/api/applies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/applies";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplyRepository applyRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ApplySearchRepositoryMockConfiguration
     */
    @Autowired
    private ApplySearchRepository mockApplySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplyMockMvc;

    private Apply apply;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apply createEntity(EntityManager em) {
        Apply apply = new Apply().name(DEFAULT_NAME).mobileNo(DEFAULT_MOBILE_NO);
        return apply;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apply createUpdatedEntity(EntityManager em) {
        Apply apply = new Apply().name(UPDATED_NAME).mobileNo(UPDATED_MOBILE_NO);
        return apply;
    }

    @BeforeEach
    public void initTest() {
        apply = createEntity(em);
    }

    @Test
    @Transactional
    void createApply() throws Exception {
        int databaseSizeBeforeCreate = applyRepository.findAll().size();
        // Create the Apply
        restApplyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apply)))
            .andExpect(status().isCreated());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeCreate + 1);
        Apply testApply = applyList.get(applyList.size() - 1);
        assertThat(testApply.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApply.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository, times(1)).save(testApply);
    }

    @Test
    @Transactional
    void createApplyWithExistingId() throws Exception {
        // Create the Apply with an existing ID
        apply.setId(1L);

        int databaseSizeBeforeCreate = applyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apply)))
            .andExpect(status().isBadRequest());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeCreate);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository, times(0)).save(apply);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applyRepository.findAll().size();
        // set the field null
        apply.setName(null);

        // Create the Apply, which fails.

        restApplyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apply)))
            .andExpect(status().isBadRequest());

        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = applyRepository.findAll().size();
        // set the field null
        apply.setMobileNo(null);

        // Create the Apply, which fails.

        restApplyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apply)))
            .andExpect(status().isBadRequest());

        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplies() throws Exception {
        // Initialize the database
        applyRepository.saveAndFlush(apply);

        // Get all the applyList
        restApplyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apply.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO.intValue())));
    }

    @Test
    @Transactional
    void getApply() throws Exception {
        // Initialize the database
        applyRepository.saveAndFlush(apply);

        // Get the apply
        restApplyMockMvc
            .perform(get(ENTITY_API_URL_ID, apply.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apply.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingApply() throws Exception {
        // Get the apply
        restApplyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApply() throws Exception {
        // Initialize the database
        applyRepository.saveAndFlush(apply);

        int databaseSizeBeforeUpdate = applyRepository.findAll().size();

        // Update the apply
        Apply updatedApply = applyRepository.findById(apply.getId()).get();
        // Disconnect from session so that the updates on updatedApply are not directly saved in db
        em.detach(updatedApply);
        updatedApply.name(UPDATED_NAME).mobileNo(UPDATED_MOBILE_NO);

        restApplyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApply.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApply))
            )
            .andExpect(status().isOk());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeUpdate);
        Apply testApply = applyList.get(applyList.size() - 1);
        assertThat(testApply.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApply.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository).save(testApply);
    }

    @Test
    @Transactional
    void putNonExistingApply() throws Exception {
        int databaseSizeBeforeUpdate = applyRepository.findAll().size();
        apply.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apply.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apply))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository, times(0)).save(apply);
    }

    @Test
    @Transactional
    void putWithIdMismatchApply() throws Exception {
        int databaseSizeBeforeUpdate = applyRepository.findAll().size();
        apply.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apply))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository, times(0)).save(apply);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApply() throws Exception {
        int databaseSizeBeforeUpdate = applyRepository.findAll().size();
        apply.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apply)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository, times(0)).save(apply);
    }

    @Test
    @Transactional
    void partialUpdateApplyWithPatch() throws Exception {
        // Initialize the database
        applyRepository.saveAndFlush(apply);

        int databaseSizeBeforeUpdate = applyRepository.findAll().size();

        // Update the apply using partial update
        Apply partialUpdatedApply = new Apply();
        partialUpdatedApply.setId(apply.getId());

        partialUpdatedApply.name(UPDATED_NAME);

        restApplyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApply.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApply))
            )
            .andExpect(status().isOk());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeUpdate);
        Apply testApply = applyList.get(applyList.size() - 1);
        assertThat(testApply.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApply.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
    }

    @Test
    @Transactional
    void fullUpdateApplyWithPatch() throws Exception {
        // Initialize the database
        applyRepository.saveAndFlush(apply);

        int databaseSizeBeforeUpdate = applyRepository.findAll().size();

        // Update the apply using partial update
        Apply partialUpdatedApply = new Apply();
        partialUpdatedApply.setId(apply.getId());

        partialUpdatedApply.name(UPDATED_NAME).mobileNo(UPDATED_MOBILE_NO);

        restApplyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApply.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApply))
            )
            .andExpect(status().isOk());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeUpdate);
        Apply testApply = applyList.get(applyList.size() - 1);
        assertThat(testApply.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApply.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    void patchNonExistingApply() throws Exception {
        int databaseSizeBeforeUpdate = applyRepository.findAll().size();
        apply.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apply.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apply))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository, times(0)).save(apply);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApply() throws Exception {
        int databaseSizeBeforeUpdate = applyRepository.findAll().size();
        apply.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apply))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository, times(0)).save(apply);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApply() throws Exception {
        int databaseSizeBeforeUpdate = applyRepository.findAll().size();
        apply.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apply)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apply in the database
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository, times(0)).save(apply);
    }

    @Test
    @Transactional
    void deleteApply() throws Exception {
        // Initialize the database
        applyRepository.saveAndFlush(apply);

        int databaseSizeBeforeDelete = applyRepository.findAll().size();

        // Delete the apply
        restApplyMockMvc
            .perform(delete(ENTITY_API_URL_ID, apply.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Apply> applyList = applyRepository.findAll();
        assertThat(applyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Apply in Elasticsearch
        verify(mockApplySearchRepository, times(1)).deleteById(apply.getId());
    }

    @Test
    @Transactional
    void searchApply() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        applyRepository.saveAndFlush(apply);
        when(mockApplySearchRepository.search(queryStringQuery("id:" + apply.getId()))).thenReturn(Collections.singletonList(apply));

        // Search the apply
        restApplyMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + apply.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apply.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO.intValue())));
    }
}
