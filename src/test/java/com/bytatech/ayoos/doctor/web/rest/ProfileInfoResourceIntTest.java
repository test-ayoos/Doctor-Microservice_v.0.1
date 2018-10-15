package com.bytatech.ayoos.doctor.web.rest;

import com.bytatech.ayoos.doctor.DoctorApp;

import com.bytatech.ayoos.doctor.domain.ProfileInfo;
import com.bytatech.ayoos.doctor.repository.ProfileInfoRepository;
import com.bytatech.ayoos.doctor.repository.search.ProfileInfoSearchRepository;
import com.bytatech.ayoos.doctor.service.ProfileInfoService;
import com.bytatech.ayoos.doctor.service.dto.ProfileInfoDTO;
import com.bytatech.ayoos.doctor.service.mapper.ProfileInfoMapper;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the ProfileInfoResource REST controller.
 *
 * @see ProfileInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DoctorApp.class)
public class ProfileInfoResourceIntTest {

    private static final String DEFAULT_PROFILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ProfileInfoRepository profileInfoRepository;

    @Autowired
    private ProfileInfoMapper profileInfoMapper;
    
    @Autowired
    private ProfileInfoService profileInfoService;

    /**
     * This repository is mocked in the com.bytatech.ayoos.doctor.repository.search test package.
     *
     * @see com.bytatech.ayoos.doctor.repository.search.ProfileInfoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfileInfoSearchRepository mockProfileInfoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfileInfoMockMvc;

    private ProfileInfo profileInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileInfoResource profileInfoResource = new ProfileInfoResource(profileInfoService);
        this.restProfileInfoMockMvc = MockMvcBuilders.standaloneSetup(profileInfoResource)
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
    public static ProfileInfo createEntity(EntityManager em) {
        ProfileInfo profileInfo = new ProfileInfo()
            .profileName(DEFAULT_PROFILE_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .imageUrl(DEFAULT_IMAGE_URL)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return profileInfo;
    }

    @Before
    public void initTest() {
        profileInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfileInfo() throws Exception {
        int databaseSizeBeforeCreate = profileInfoRepository.findAll().size();

        // Create the ProfileInfo
        ProfileInfoDTO profileInfoDTO = profileInfoMapper.toDto(profileInfo);
        restProfileInfoMockMvc.perform(post("/api/profile-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the ProfileInfo in the database
        List<ProfileInfo> profileInfoList = profileInfoRepository.findAll();
        assertThat(profileInfoList).hasSize(databaseSizeBeforeCreate + 1);
        ProfileInfo testProfileInfo = profileInfoList.get(profileInfoList.size() - 1);
        assertThat(testProfileInfo.getProfileName()).isEqualTo(DEFAULT_PROFILE_NAME);
        assertThat(testProfileInfo.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testProfileInfo.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testProfileInfo.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testProfileInfo.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfileInfo.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testProfileInfo.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProfileInfo.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);

        // Validate the ProfileInfo in Elasticsearch
        verify(mockProfileInfoSearchRepository, times(1)).save(testProfileInfo);
    }

    @Test
    @Transactional
    public void createProfileInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileInfoRepository.findAll().size();

        // Create the ProfileInfo with an existing ID
        profileInfo.setId(1L);
        ProfileInfoDTO profileInfoDTO = profileInfoMapper.toDto(profileInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileInfoMockMvc.perform(post("/api/profile-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfileInfo in the database
        List<ProfileInfo> profileInfoList = profileInfoRepository.findAll();
        assertThat(profileInfoList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProfileInfo in Elasticsearch
        verify(mockProfileInfoSearchRepository, times(0)).save(profileInfo);
    }

    @Test
    @Transactional
    public void getAllProfileInfos() throws Exception {
        // Initialize the database
        profileInfoRepository.saveAndFlush(profileInfo);

        // Get all the profileInfoList
        restProfileInfoMockMvc.perform(get("/api/profile-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profileInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].profileName").value(hasItem(DEFAULT_PROFILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getProfileInfo() throws Exception {
        // Initialize the database
        profileInfoRepository.saveAndFlush(profileInfo);

        // Get the profileInfo
        restProfileInfoMockMvc.perform(get("/api/profile-infos/{id}", profileInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profileInfo.getId().intValue()))
            .andExpect(jsonPath("$.profileName").value(DEFAULT_PROFILE_NAME.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingProfileInfo() throws Exception {
        // Get the profileInfo
        restProfileInfoMockMvc.perform(get("/api/profile-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfileInfo() throws Exception {
        // Initialize the database
        profileInfoRepository.saveAndFlush(profileInfo);

        int databaseSizeBeforeUpdate = profileInfoRepository.findAll().size();

        // Update the profileInfo
        ProfileInfo updatedProfileInfo = profileInfoRepository.findById(profileInfo.getId()).get();
        // Disconnect from session so that the updates on updatedProfileInfo are not directly saved in db
        em.detach(updatedProfileInfo);
        updatedProfileInfo
            .profileName(UPDATED_PROFILE_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .imageUrl(UPDATED_IMAGE_URL)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ProfileInfoDTO profileInfoDTO = profileInfoMapper.toDto(updatedProfileInfo);

        restProfileInfoMockMvc.perform(put("/api/profile-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileInfoDTO)))
            .andExpect(status().isOk());

        // Validate the ProfileInfo in the database
        List<ProfileInfo> profileInfoList = profileInfoRepository.findAll();
        assertThat(profileInfoList).hasSize(databaseSizeBeforeUpdate);
        ProfileInfo testProfileInfo = profileInfoList.get(profileInfoList.size() - 1);
        assertThat(testProfileInfo.getProfileName()).isEqualTo(UPDATED_PROFILE_NAME);
        assertThat(testProfileInfo.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testProfileInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testProfileInfo.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testProfileInfo.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfileInfo.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testProfileInfo.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProfileInfo.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);

        // Validate the ProfileInfo in Elasticsearch
        verify(mockProfileInfoSearchRepository, times(1)).save(testProfileInfo);
    }

    @Test
    @Transactional
    public void updateNonExistingProfileInfo() throws Exception {
        int databaseSizeBeforeUpdate = profileInfoRepository.findAll().size();

        // Create the ProfileInfo
        ProfileInfoDTO profileInfoDTO = profileInfoMapper.toDto(profileInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileInfoMockMvc.perform(put("/api/profile-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfileInfo in the database
        List<ProfileInfo> profileInfoList = profileInfoRepository.findAll();
        assertThat(profileInfoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProfileInfo in Elasticsearch
        verify(mockProfileInfoSearchRepository, times(0)).save(profileInfo);
    }

    @Test
    @Transactional
    public void deleteProfileInfo() throws Exception {
        // Initialize the database
        profileInfoRepository.saveAndFlush(profileInfo);

        int databaseSizeBeforeDelete = profileInfoRepository.findAll().size();

        // Get the profileInfo
        restProfileInfoMockMvc.perform(delete("/api/profile-infos/{id}", profileInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProfileInfo> profileInfoList = profileInfoRepository.findAll();
        assertThat(profileInfoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProfileInfo in Elasticsearch
        verify(mockProfileInfoSearchRepository, times(1)).deleteById(profileInfo.getId());
    }

    @Test
    @Transactional
    public void searchProfileInfo() throws Exception {
        // Initialize the database
        profileInfoRepository.saveAndFlush(profileInfo);
        when(mockProfileInfoSearchRepository.search(queryStringQuery("id:" + profileInfo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(profileInfo), PageRequest.of(0, 1), 1));
        // Search the profileInfo
        restProfileInfoMockMvc.perform(get("/api/_search/profile-infos?query=id:" + profileInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profileInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].profileName").value(hasItem(DEFAULT_PROFILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileInfo.class);
        ProfileInfo profileInfo1 = new ProfileInfo();
        profileInfo1.setId(1L);
        ProfileInfo profileInfo2 = new ProfileInfo();
        profileInfo2.setId(profileInfo1.getId());
        assertThat(profileInfo1).isEqualTo(profileInfo2);
        profileInfo2.setId(2L);
        assertThat(profileInfo1).isNotEqualTo(profileInfo2);
        profileInfo1.setId(null);
        assertThat(profileInfo1).isNotEqualTo(profileInfo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileInfoDTO.class);
        ProfileInfoDTO profileInfoDTO1 = new ProfileInfoDTO();
        profileInfoDTO1.setId(1L);
        ProfileInfoDTO profileInfoDTO2 = new ProfileInfoDTO();
        assertThat(profileInfoDTO1).isNotEqualTo(profileInfoDTO2);
        profileInfoDTO2.setId(profileInfoDTO1.getId());
        assertThat(profileInfoDTO1).isEqualTo(profileInfoDTO2);
        profileInfoDTO2.setId(2L);
        assertThat(profileInfoDTO1).isNotEqualTo(profileInfoDTO2);
        profileInfoDTO1.setId(null);
        assertThat(profileInfoDTO1).isNotEqualTo(profileInfoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(profileInfoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(profileInfoMapper.fromId(null)).isNull();
    }
}
