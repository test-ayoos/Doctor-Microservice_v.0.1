package com.bytatech.ayoos.doctor.service.impl;

import com.bytatech.ayoos.doctor.service.ProfileInfoService;
import com.bytatech.ayoos.doctor.domain.ProfileInfo;
import com.bytatech.ayoos.doctor.repository.ProfileInfoRepository;
import com.bytatech.ayoos.doctor.repository.search.ProfileInfoSearchRepository;
import com.bytatech.ayoos.doctor.service.dto.ProfileInfoDTO;
import com.bytatech.ayoos.doctor.service.mapper.ProfileInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProfileInfo.
 */
@Service
@Transactional
public class ProfileInfoServiceImpl implements ProfileInfoService {

    private final Logger log = LoggerFactory.getLogger(ProfileInfoServiceImpl.class);

    private final ProfileInfoRepository profileInfoRepository;

    private final ProfileInfoMapper profileInfoMapper;

    private final ProfileInfoSearchRepository profileInfoSearchRepository;

    public ProfileInfoServiceImpl(ProfileInfoRepository profileInfoRepository, ProfileInfoMapper profileInfoMapper, ProfileInfoSearchRepository profileInfoSearchRepository) {
        this.profileInfoRepository = profileInfoRepository;
        this.profileInfoMapper = profileInfoMapper;
        this.profileInfoSearchRepository = profileInfoSearchRepository;
    }

    /**
     * Save a profileInfo.
     *
     * @param profileInfoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProfileInfoDTO save(ProfileInfoDTO profileInfoDTO) {
        log.debug("Request to save ProfileInfo : {}", profileInfoDTO);
        ProfileInfo profileInfo = profileInfoMapper.toEntity(profileInfoDTO);
        profileInfo = profileInfoRepository.save(profileInfo);
        ProfileInfoDTO result = profileInfoMapper.toDto(profileInfo);
        profileInfoSearchRepository.save(profileInfo);
        return result;
    }

    /**
     * Get all the profileInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProfileInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProfileInfos");
        return profileInfoRepository.findAll(pageable)
            .map(profileInfoMapper::toDto);
    }


    /**
     * Get one profileInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileInfoDTO> findOne(Long id) {
        log.debug("Request to get ProfileInfo : {}", id);
        return profileInfoRepository.findById(id)
            .map(profileInfoMapper::toDto);
    }

    /**
     * Delete the profileInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProfileInfo : {}", id);
        profileInfoRepository.deleteById(id);
        profileInfoSearchRepository.deleteById(id);
    }

    /**
     * Search for the profileInfo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProfileInfoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProfileInfos for query {}", query);
        return profileInfoSearchRepository.search(queryStringQuery(query), pageable)
            .map(profileInfoMapper::toDto);
    }
}
