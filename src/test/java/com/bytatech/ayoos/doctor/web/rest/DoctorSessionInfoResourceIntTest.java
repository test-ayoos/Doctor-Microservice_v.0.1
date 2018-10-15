package com.bytatech.ayoos.doctor.web.rest;

import com.bytatech.ayoos.doctor.DoctorApp;

import com.bytatech.ayoos.doctor.domain.DoctorSessionInfo;
import com.bytatech.ayoos.doctor.repository.DoctorSessionInfoRepository;
import com.bytatech.ayoos.doctor.repository.search.DoctorSessionInfoSearchRepository;
import com.bytatech.ayoos.doctor.service.DoctorSessionInfoService;
import com.bytatech.ayoos.doctor.service.dto.DoctorSessionInfoDTO;
import com.bytatech.ayoos.doctor.service.mapper.DoctorSessionInfoMapper;
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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.bytatech.ayoos.doctor.web.rest.TestUtil.sameInstant;
import static com.bytatech.ayoos.doctor.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DoctorSessionInfoResource REST controller.
 *
 * @see DoctorSessionInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoctorApp.class)
public class DoctorSessionInfoResourceIntTest {

    private static final String DEFAULT_SESSION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_INTERVAL = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INTERVAL = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private DoctorSessionInfoRepository doctorSessionInfoRepository;

    @Autowired
    private DoctorSessionInfoMapper doctorSessionInfoMapper;
    
    @Autowired
    private DoctorSessionInfoService doctorSessionInfoService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.doctor.repository.search test package.
     *
     * @see com.bytatech.ayoos.doctor.repository.search.DoctorSessionInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private DoctorSessionInfoSearchRepository mockDoctorSessionInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDoctorSessionInfoMockMvc;

    private DoctorSessionInfo doctorSessionInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoctorSessionInfoResource doctorSessionInfoResource = new DoctorSessionInfoResource(doctorSessionInfoService);
        this.restDoctorSessionInfoMockMvc = MockMvcBuilders.standaloneSetup(doctorSessionInfoResource)
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
    public static DoctorSessionInfo createEntity(EntityManager em) {
        DoctorSessionInfo doctorSessionInfo = new DoctorSessionInfo()
            .sessionName(DEFAULT_SESSION_NAME)
            .date(DEFAULT_DATE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .interval(DEFAULT_INTERVAL);
        return doctorSessionInfo;
    }

    @Before
    public void initTest() {
        doctorSessionInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctorSessionInfo() throws Exception {
        int databaseSizeBeforeCreate = doctorSessionInfoRepository.findAll().size();

        // Create the DoctorSessionInfo
        DoctorSessionInfoDTO doctorSessionInfoDTO = doctorSessionInfoMapper.toDto(doctorSessionInfo);
        restDoctorSessionInfoMockMvc.perform(post("/api/doctor-session-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSessionInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the DoctorSessionInfo in the database
        List<DoctorSessionInfo> doctorSessionInfoList = doctorSessionInfoRepository.findAll();
        assertThat(doctorSessionInfoList).hasSize(databaseSizeBeforeCreate + 1);
        DoctorSessionInfo testDoctorSessionInfo = doctorSessionInfoList.get(doctorSessionInfoList.size() - 1);
        assertThat(testDoctorSessionInfo.getSessionName()).isEqualTo(DEFAULT_SESSION_NAME);
        assertThat(testDoctorSessionInfo.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDoctorSessionInfo.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testDoctorSessionInfo.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testDoctorSessionInfo.getInterval()).isEqualTo(DEFAULT_INTERVAL);

        // Validate the DoctorSessionInfo in Elasticsearch
        verify(mockDoctorSessionInfoSearchRepository, times(1)).save(testDoctorSessionInfo);
    }

    @Test
    @Transactional
    public void createDoctorSessionInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorSessionInfoRepository.findAll().size();

        // Create the DoctorSessionInfo with an existing ID
        doctorSessionInfo.setId(1L);
        DoctorSessionInfoDTO doctorSessionInfoDTO = doctorSessionInfoMapper.toDto(doctorSessionInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorSessionInfoMockMvc.perform(post("/api/doctor-session-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSessionInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorSessionInfo in the database
        List<DoctorSessionInfo> doctorSessionInfoList = doctorSessionInfoRepository.findAll();
        assertThat(doctorSessionInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the DoctorSessionInfo in Elasticsearch
        verify(mockDoctorSessionInfoSearchRepository, times(0)).save(doctorSessionInfo);
    }

    @Test
    @Transactional
    public void getAllDoctorSessionInfos() throws Exception {
        // Initialize the database
        doctorSessionInfoRepository.saveAndFlush(doctorSessionInfo);

        // Get all the doctorSessionInfoList
        restDoctorSessionInfoMockMvc.perform(get("/api/doctor-session-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorSessionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionName").value(hasItem(DEFAULT_SESSION_NAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].interval").value(hasItem(sameInstant(DEFAULT_INTERVAL))));
    }
    
    @Test
    @Transactional
    public void getDoctorSessionInfo() throws Exception {
        // Initialize the database
        doctorSessionInfoRepository.saveAndFlush(doctorSessionInfo);

        // Get the doctorSessionInfo
        restDoctorSessionInfoMockMvc.perform(get("/api/doctor-session-infos/{id}", doctorSessionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doctorSessionInfo.getId().intValue()))
            .andExpect(jsonPath("$.sessionName").value(DEFAULT_SESSION_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.interval").value(sameInstant(DEFAULT_INTERVAL)));
    }

    @Test
    @Transactional
    public void getNonExistingDoctorSessionInfo() throws Exception {
        // Get the doctorSessionInfo
        restDoctorSessionInfoMockMvc.perform(get("/api/doctor-session-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctorSessionInfo() throws Exception {
        // Initialize the database
        doctorSessionInfoRepository.saveAndFlush(doctorSessionInfo);

        int databaseSizeBeforeUpdate = doctorSessionInfoRepository.findAll().size();

        // Update the doctorSessionInfo
        DoctorSessionInfo updatedDoctorSessionInfo = doctorSessionInfoRepository.findById(doctorSessionInfo.getId()).get();
        // Disconnect from session so that the updates on updatedDoctorSessionInfo are not directly saved in db
        em.detach(updatedDoctorSessionInfo);
        updatedDoctorSessionInfo
            .sessionName(UPDATED_SESSION_NAME)
            .date(UPDATED_DATE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .interval(UPDATED_INTERVAL);
        DoctorSessionInfoDTO doctorSessionInfoDTO = doctorSessionInfoMapper.toDto(updatedDoctorSessionInfo);

        restDoctorSessionInfoMockMvc.perform(put("/api/doctor-session-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSessionInfoDTO)))
            .andExpect(status().isOk());

        // Validate the DoctorSessionInfo in the database
        List<DoctorSessionInfo> doctorSessionInfoList = doctorSessionInfoRepository.findAll();
        assertThat(doctorSessionInfoList).hasSize(databaseSizeBeforeUpdate);
        DoctorSessionInfo testDoctorSessionInfo = doctorSessionInfoList.get(doctorSessionInfoList.size() - 1);
        assertThat(testDoctorSessionInfo.getSessionName()).isEqualTo(UPDATED_SESSION_NAME);
        assertThat(testDoctorSessionInfo.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDoctorSessionInfo.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testDoctorSessionInfo.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testDoctorSessionInfo.getInterval()).isEqualTo(UPDATED_INTERVAL);

        // Validate the DoctorSessionInfo in Elasticsearch
        verify(mockDoctorSessionInfoSearchRepository, times(1)).save(testDoctorSessionInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctorSessionInfo() throws Exception {
        int databaseSizeBeforeUpdate = doctorSessionInfoRepository.findAll().size();

        // Create the DoctorSessionInfo
        DoctorSessionInfoDTO doctorSessionInfoDTO = doctorSessionInfoMapper.toDto(doctorSessionInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorSessionInfoMockMvc.perform(put("/api/doctor-session-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doctorSessionInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DoctorSessionInfo in the database
        List<DoctorSessionInfo> doctorSessionInfoList = doctorSessionInfoRepository.findAll();
        assertThat(doctorSessionInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DoctorSessionInfo in Elasticsearch
        verify(mockDoctorSessionInfoSearchRepository, times(0)).save(doctorSessionInfo);
    }

    @Test
    @Transactional
    public void deleteDoctorSessionInfo() throws Exception {
        // Initialize the database
        doctorSessionInfoRepository.saveAndFlush(doctorSessionInfo);

        int databaseSizeBeforeDelete = doctorSessionInfoRepository.findAll().size();

        // Get the doctorSessionInfo
        restDoctorSessionInfoMockMvc.perform(delete("/api/doctor-session-infos/{id}", doctorSessionInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<DoctorSessionInfo> doctorSessionInfoList = doctorSessionInfoRepository.findAll();
        assertThat(doctorSessionInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DoctorSessionInfo in Elasticsearch
        verify(mockDoctorSessionInfoSearchRepository, times(1)).deleteById(doctorSessionInfo.getId());
    }

    @Test
    @Transactional
    public void searchDoctorSessionInfo() throws Exception {
        // Initialize the database
        doctorSessionInfoRepository.saveAndFlush(doctorSessionInfo);
        when(mockDoctorSessionInfoSearchRepository.search(queryStringQuery("id:" + doctorSessionInfo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(doctorSessionInfo), PageRequest.of(0, 1), 1));
        // Search the doctorSessionInfo
        restDoctorSessionInfoMockMvc.perform(get("/api/_search/doctor-session-infos?query=id:" + doctorSessionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctorSessionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionName").value(hasItem(DEFAULT_SESSION_NAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].interval").value(hasItem(sameInstant(DEFAULT_INTERVAL))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorSessionInfo.class);
        DoctorSessionInfo doctorSessionInfo1 = new DoctorSessionInfo();
        doctorSessionInfo1.setId(1L);
        DoctorSessionInfo doctorSessionInfo2 = new DoctorSessionInfo();
        doctorSessionInfo2.setId(doctorSessionInfo1.getId());
        assertThat(doctorSessionInfo1).isEqualTo(doctorSessionInfo2);
        doctorSessionInfo2.setId(2L);
        assertThat(doctorSessionInfo1).isNotEqualTo(doctorSessionInfo2);
        doctorSessionInfo1.setId(null);
        assertThat(doctorSessionInfo1).isNotEqualTo(doctorSessionInfo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DoctorSessionInfoDTO.class);
        DoctorSessionInfoDTO doctorSessionInfoDTO1 = new DoctorSessionInfoDTO();
        doctorSessionInfoDTO1.setId(1L);
        DoctorSessionInfoDTO doctorSessionInfoDTO2 = new DoctorSessionInfoDTO();
        assertThat(doctorSessionInfoDTO1).isNotEqualTo(doctorSessionInfoDTO2);
        doctorSessionInfoDTO2.setId(doctorSessionInfoDTO1.getId());
        assertThat(doctorSessionInfoDTO1).isEqualTo(doctorSessionInfoDTO2);
        doctorSessionInfoDTO2.setId(2L);
        assertThat(doctorSessionInfoDTO1).isNotEqualTo(doctorSessionInfoDTO2);
        doctorSessionInfoDTO1.setId(null);
        assertThat(doctorSessionInfoDTO1).isNotEqualTo(doctorSessionInfoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(doctorSessionInfoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(doctorSessionInfoMapper.fromId(null)).isNull();
    }
}
