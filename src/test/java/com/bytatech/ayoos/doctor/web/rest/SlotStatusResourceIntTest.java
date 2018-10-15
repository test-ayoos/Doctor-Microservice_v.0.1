package com.bytatech.ayoos.doctor.web.rest;

import com.bytatech.ayoos.doctor.DoctorApp;

import com.bytatech.ayoos.doctor.domain.SlotStatus;
import com.bytatech.ayoos.doctor.repository.SlotStatusRepository;
import com.bytatech.ayoos.doctor.repository.search.SlotStatusSearchRepository;
import com.bytatech.ayoos.doctor.service.SlotStatusService;
import com.bytatech.ayoos.doctor.service.dto.SlotStatusDTO;
import com.bytatech.ayoos.doctor.service.mapper.SlotStatusMapper;
import com.bytatech.ayoos.doctor.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.bytatech.ayoos.doctor.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SlotStatusResource REST controller.
 *
 * @see SlotStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoctorApp.class)
public class SlotStatusResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private SlotStatusRepository slotStatusRepository;

    @Autowired
    private SlotStatusMapper slotStatusMapper;
    
    @Autowired
    private SlotStatusService slotStatusService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.doctor.repository.search test package.
     *
     * @see com.bytatech.ayoos.doctor.repository.search.SlotStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private SlotStatusSearchRepository mockSlotStatusSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSlotStatusMockMvc;

    private SlotStatus slotStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SlotStatusResource slotStatusResource = new SlotStatusResource(slotStatusService);
        this.restSlotStatusMockMvc = MockMvcBuilders.standaloneSetup(slotStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SlotStatus createEntity(EntityManager em) {
        SlotStatus slotStatus = new SlotStatus()
            .status(DEFAULT_STATUS);
        return slotStatus;
    }

    @Before
    public void initTest() {
        slotStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createSlotStatus() throws Exception {
        int databaseSizeBeforeCreate = slotStatusRepository.findAll().size();

        // Create the SlotStatus
        SlotStatusDTO slotStatusDTO = slotStatusMapper.toDto(slotStatus);
        restSlotStatusMockMvc.perform(post("/api/slot-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slotStatusDTO)))
            .andExpect(status().isCreated());

        // Validate the SlotStatus in the database
        List<SlotStatus> slotStatusList = slotStatusRepository.findAll();
        assertThat(slotStatusList).hasSize(databaseSizeBeforeCreate + 1);
        SlotStatus testSlotStatus = slotStatusList.get(slotStatusList.size() - 1);
        assertThat(testSlotStatus.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the SlotStatus in Elasticsearch
        verify(mockSlotStatusSearchRepository, times(1)).save(testSlotStatus);
    }

    @Test
    @Transactional
    public void createSlotStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slotStatusRepository.findAll().size();

        // Create the SlotStatus with an existing ID
        slotStatus.setId(1L);
        SlotStatusDTO slotStatusDTO = slotStatusMapper.toDto(slotStatus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlotStatusMockMvc.perform(post("/api/slot-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slotStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SlotStatus in the database
        List<SlotStatus> slotStatusList = slotStatusRepository.findAll();
        assertThat(slotStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the SlotStatus in Elasticsearch
        verify(mockSlotStatusSearchRepository, times(0)).save(slotStatus);
    }

    @Test
    @Transactional
    public void getAllSlotStatuses() throws Exception {
        // Initialize the database
        slotStatusRepository.saveAndFlush(slotStatus);

        // Get all the slotStatusList
        restSlotStatusMockMvc.perform(get("/api/slot-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slotStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getSlotStatus() throws Exception {
        // Initialize the database
        slotStatusRepository.saveAndFlush(slotStatus);

        // Get the slotStatus
        restSlotStatusMockMvc.perform(get("/api/slot-statuses/{id}", slotStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(slotStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSlotStatus() throws Exception {
        // Get the slotStatus
        restSlotStatusMockMvc.perform(get("/api/slot-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSlotStatus() throws Exception {
        // Initialize the database
        slotStatusRepository.saveAndFlush(slotStatus);

        int databaseSizeBeforeUpdate = slotStatusRepository.findAll().size();

        // Update the slotStatus
        SlotStatus updatedSlotStatus = slotStatusRepository.findById(slotStatus.getId()).get();
        // Disconnect from session so that the updates on updatedSlotStatus are not directly saved in db
        em.detach(updatedSlotStatus);
        updatedSlotStatus
            .status(UPDATED_STATUS);
        SlotStatusDTO slotStatusDTO = slotStatusMapper.toDto(updatedSlotStatus);

        restSlotStatusMockMvc.perform(put("/api/slot-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slotStatusDTO)))
            .andExpect(status().isOk());

        // Validate the SlotStatus in the database
        List<SlotStatus> slotStatusList = slotStatusRepository.findAll();
        assertThat(slotStatusList).hasSize(databaseSizeBeforeUpdate);
        SlotStatus testSlotStatus = slotStatusList.get(slotStatusList.size() - 1);
        assertThat(testSlotStatus.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the SlotStatus in Elasticsearch
        verify(mockSlotStatusSearchRepository, times(1)).save(testSlotStatus);
    }

    @Test
    @Transactional
    public void updateNonExistingSlotStatus() throws Exception {
        int databaseSizeBeforeUpdate = slotStatusRepository.findAll().size();

        // Create the SlotStatus
        SlotStatusDTO slotStatusDTO = slotStatusMapper.toDto(slotStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotStatusMockMvc.perform(put("/api/slot-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slotStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SlotStatus in the database
        List<SlotStatus> slotStatusList = slotStatusRepository.findAll();
        assertThat(slotStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SlotStatus in Elasticsearch
        verify(mockSlotStatusSearchRepository, times(0)).save(slotStatus);
    }

    @Test
    @Transactional
    public void deleteSlotStatus() throws Exception {
        // Initialize the database
        slotStatusRepository.saveAndFlush(slotStatus);

        int databaseSizeBeforeDelete = slotStatusRepository.findAll().size();

        // Get the slotStatus
        restSlotStatusMockMvc.perform(delete("/api/slot-statuses/{id}", slotStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SlotStatus> slotStatusList = slotStatusRepository.findAll();
        assertThat(slotStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SlotStatus in Elasticsearch
        verify(mockSlotStatusSearchRepository, times(1)).deleteById(slotStatus.getId());
    }

    @Test
    @Transactional
    public void searchSlotStatus() throws Exception {
        // Initialize the database
        slotStatusRepository.saveAndFlush(slotStatus);
        when(mockSlotStatusSearchRepository.search(queryStringQuery("id:" + slotStatus.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(slotStatus), PageRequest.of(0, 1), 1));
        // Search the slotStatus
        restSlotStatusMockMvc.perform(get("/api/_search/slot-statuses?query=id:" + slotStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slotStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotStatus.class);
        SlotStatus slotStatus1 = new SlotStatus();
        slotStatus1.setId(1L);
        SlotStatus slotStatus2 = new SlotStatus();
        slotStatus2.setId(slotStatus1.getId());
        assertThat(slotStatus1).isEqualTo(slotStatus2);
        slotStatus2.setId(2L);
        assertThat(slotStatus1).isNotEqualTo(slotStatus2);
        slotStatus1.setId(null);
        assertThat(slotStatus1).isNotEqualTo(slotStatus2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotStatusDTO.class);
        SlotStatusDTO slotStatusDTO1 = new SlotStatusDTO();
        slotStatusDTO1.setId(1L);
        SlotStatusDTO slotStatusDTO2 = new SlotStatusDTO();
        assertThat(slotStatusDTO1).isNotEqualTo(slotStatusDTO2);
        slotStatusDTO2.setId(slotStatusDTO1.getId());
        assertThat(slotStatusDTO1).isEqualTo(slotStatusDTO2);
        slotStatusDTO2.setId(2L);
        assertThat(slotStatusDTO1).isNotEqualTo(slotStatusDTO2);
        slotStatusDTO1.setId(null);
        assertThat(slotStatusDTO1).isNotEqualTo(slotStatusDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(slotStatusMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(slotStatusMapper.fromId(null)).isNull();
    }
}
