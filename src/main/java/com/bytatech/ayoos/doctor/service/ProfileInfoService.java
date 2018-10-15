package com.bytatech.ayoos.doctor.service;

import com.bytatech.ayoos.doctor.service.dto.ProfileInfoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ProfileInfo.
 */
public interface ProfileInfoService {

    /**
     * Save a profileInfo.
     *
     * @param profileInfoDTO the entity to save
     * @return the persisted entity
     */
    ProfileInfoDTO save(ProfileInfoDTO profileInfoDTO);

    /**
     * Get all the profileInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProfileInfoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" profileInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ProfileInfoDTO> findOne(Long id);

    /**
     * Delete the "id" profileInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the profileInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProfileInfoDTO> search(String query, Pageable pageable);
}
