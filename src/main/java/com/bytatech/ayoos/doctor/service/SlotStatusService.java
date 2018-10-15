package com.bytatech.ayoos.doctor.service;

import com.bytatech.ayoos.doctor.service.dto.SlotStatusDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SlotStatus.
 */
public interface SlotStatusService {

    /**
     * Save a slotStatus.
     *
     * @param slotStatusDTO the entity to save
     * @return the persisted entity
     */
    SlotStatusDTO save(SlotStatusDTO slotStatusDTO);

    /**
     * Get all the slotStatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SlotStatusDTO> findAll(Pageable pageable);


    /**
     * Get the "id" slotStatus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SlotStatusDTO> findOne(Long id);

    /**
     * Delete the "id" slotStatus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the slotStatus corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SlotStatusDTO> search(String query, Pageable pageable);
}
