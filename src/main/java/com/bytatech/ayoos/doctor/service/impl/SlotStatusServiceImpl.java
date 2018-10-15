package com.bytatech.ayoos.doctor.service.impl;

import com.bytatech.ayoos.doctor.service.SlotStatusService;
import com.bytatech.ayoos.doctor.domain.SlotStatus;
import com.bytatech.ayoos.doctor.repository.SlotStatusRepository;
import com.bytatech.ayoos.doctor.repository.search.SlotStatusSearchRepository;
import com.bytatech.ayoos.doctor.service.dto.SlotStatusDTO;
import com.bytatech.ayoos.doctor.service.mapper.SlotStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SlotStatus.
 */
@Service
@Transactional
public class SlotStatusServiceImpl implements SlotStatusService {

    private final Logger log = LoggerFactory.getLogger(SlotStatusServiceImpl.class);

    private final SlotStatusRepository slotStatusRepository;

    private final SlotStatusMapper slotStatusMapper;

    private final SlotStatusSearchRepository slotStatusSearchRepository;

    public SlotStatusServiceImpl(SlotStatusRepository slotStatusRepository, SlotStatusMapper slotStatusMapper, SlotStatusSearchRepository slotStatusSearchRepository) {
        this.slotStatusRepository = slotStatusRepository;
        this.slotStatusMapper = slotStatusMapper;
        this.slotStatusSearchRepository = slotStatusSearchRepository;
    }

    /**
     * Save a slotStatus.
     *
     * @param slotStatusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SlotStatusDTO save(SlotStatusDTO slotStatusDTO) {
        log.debug("Request to save SlotStatus : {}", slotStatusDTO);
        SlotStatus slotStatus = slotStatusMapper.toEntity(slotStatusDTO);
        slotStatus = slotStatusRepository.save(slotStatus);
        SlotStatusDTO result = slotStatusMapper.toDto(slotStatus);
        slotStatusSearchRepository.save(slotStatus);
        return result;
    }

    /**
     * Get all the slotStatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SlotStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SlotStatuses");
        return slotStatusRepository.findAll(pageable)
            .map(slotStatusMapper::toDto);
    }


    /**
     * Get one slotStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SlotStatusDTO> findOne(Long id) {
        log.debug("Request to get SlotStatus : {}", id);
        return slotStatusRepository.findById(id)
            .map(slotStatusMapper::toDto);
    }

    /**
     * Delete the slotStatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SlotStatus : {}", id);
        slotStatusRepository.deleteById(id);
        slotStatusSearchRepository.deleteById(id);
    }

    /**
     * Search for the slotStatus corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SlotStatusDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SlotStatuses for query {}", query);
        return slotStatusSearchRepository.search(queryStringQuery(query), pageable)
            .map(slotStatusMapper::toDto);
    }
}
