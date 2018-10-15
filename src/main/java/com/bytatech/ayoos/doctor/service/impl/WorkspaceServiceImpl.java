package com.bytatech.ayoos.doctor.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytatech.ayoos.doctor.domain.Workspace;
import com.bytatech.ayoos.doctor.repository.WorkspaceRepository;
import com.bytatech.ayoos.doctor.repository.search.WorkspaceSearchRepository;
import com.bytatech.ayoos.doctor.service.WorkspaceService;
import com.bytatech.ayoos.doctor.service.dto.WorkspaceDTO;
import com.bytatech.ayoos.doctor.service.mapper.WorkspaceMapper;

/**
 * Service Implementation for managing Workspace.
 */
@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private final Logger log = LoggerFactory.getLogger(WorkspaceServiceImpl.class);

    private final WorkspaceRepository workspaceRepository;

    private final WorkspaceMapper workspaceMapper;

    private final WorkspaceSearchRepository workspaceSearchRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository, WorkspaceMapper workspaceMapper, WorkspaceSearchRepository workspaceSearchRepository) {
        this.workspaceRepository = workspaceRepository;
        this.workspaceMapper = workspaceMapper;
        this.workspaceSearchRepository = workspaceSearchRepository;
    }

    /**
     * Save a workspace.
     *
     * @param workspaceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WorkspaceDTO save(WorkspaceDTO workspaceDTO) {
        log.debug("Request to save Workspace : {}", workspaceDTO);
        Workspace workspace = workspaceMapper.toEntity(workspaceDTO);
        workspace = workspaceRepository.save(workspace);
        workspace = workspaceRepository.save(workspace);
        WorkspaceDTO result = workspaceMapper.toDto(workspace);
        workspaceSearchRepository.save(workspace);
        return result;
    }

    /**
     * Get all the workspaces.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkspaceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Workspaces");
        return workspaceRepository.findAll(pageable)
            .map(workspaceMapper::toDto);
    }


    /**
     * Get one workspace by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<WorkspaceDTO> findOne(Long id) {
        log.debug("Request to get Workspace : {}", id);
        return workspaceRepository.findById(id)
            .map(workspaceMapper::toDto);
    }

    /**
     * Delete the workspace by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Workspace : {}", id);
        workspaceRepository.deleteById(id);
        workspaceSearchRepository.deleteById(id);
    }

    /**
     * Search for the workspace corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkspaceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Workspaces for query {}", query);
        return workspaceSearchRepository.search(queryStringQuery(query), pageable)
            .map(workspaceMapper::toDto);
    }
}
