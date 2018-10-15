package com.bytatech.ayoos.doctor.service;

import com.bytatech.ayoos.doctor.service.dto.DoctorSessionInfoDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Interface for managing DoctorSessionInfo.
 */
public interface DoctorSessionInfoService {

    /**
     * Save a doctorSessionInfo.
     *
     * @param doctorSessionInfoDTO the entity to save
     * @return the persisted entity
     */
    DoctorSessionInfoDTO save(DoctorSessionInfoDTO doctorSessionInfoDTO);

    /**
     * Get all the doctorSessionInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DoctorSessionInfoDTO> findAll(Pageable pageable);


    /**
     * Get the "id" doctorSessionInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DoctorSessionInfoDTO> findOne(Long id);

    /**
     * Delete the "id" doctorSessionInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the doctorSessionInfo corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DoctorSessionInfoDTO> search(String query, Pageable pageable);
    
    
    void setBusySession(String profileName,LocalDate date);
    
    
    
    
    
    
    
}
