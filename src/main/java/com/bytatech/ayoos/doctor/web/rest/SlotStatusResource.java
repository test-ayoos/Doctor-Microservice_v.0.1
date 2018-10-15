package com.bytatech.ayoos.doctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.doctor.service.SlotStatusService;
import com.bytatech.ayoos.doctor.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.doctor.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.doctor.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.doctor.service.dto.SlotStatusDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SlotStatus.
 */
@RestController
@RequestMapping("/api")
public class SlotStatusResource {

    private final Logger log = LoggerFactory.getLogger(SlotStatusResource.class);

    private static final String ENTITY_NAME = "doctorSlotStatus";

    private final SlotStatusService slotStatusService;

    public SlotStatusResource(SlotStatusService slotStatusService) {
        this.slotStatusService = slotStatusService;
    }

    /**
     * POST  /slot-statuses : Create a new slotStatus.
     *
     * @param slotStatusDTO the slotStatusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new slotStatusDTO, or with status 400 (Bad Request) if the slotStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/slot-statuses")
    @Timed
    public ResponseEntity<SlotStatusDTO> createSlotStatus(@RequestBody SlotStatusDTO slotStatusDTO) throws URISyntaxException {
        log.debug("REST request to save SlotStatus : {}", slotStatusDTO);
        if (slotStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new slotStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SlotStatusDTO result = slotStatusService.save(slotStatusDTO);
        return ResponseEntity.created(new URI("/api/slot-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /slot-statuses : Updates an existing slotStatus.
     *
     * @param slotStatusDTO the slotStatusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated slotStatusDTO,
     * or with status 400 (Bad Request) if the slotStatusDTO is not valid,
     * or with status 500 (Internal Server Error) if the slotStatusDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/slot-statuses")
    @Timed
    public ResponseEntity<SlotStatusDTO> updateSlotStatus(@RequestBody SlotStatusDTO slotStatusDTO) throws URISyntaxException {
        log.debug("REST request to update SlotStatus : {}", slotStatusDTO);
        if (slotStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SlotStatusDTO result = slotStatusService.save(slotStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, slotStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /slot-statuses : get all the slotStatuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of slotStatuses in body
     */
    @GetMapping("/slot-statuses")
    @Timed
    public ResponseEntity<List<SlotStatusDTO>> getAllSlotStatuses(Pageable pageable) {
        log.debug("REST request to get a page of SlotStatuses");
        Page<SlotStatusDTO> page = slotStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/slot-statuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /slot-statuses/:id : get the "id" slotStatus.
     *
     * @param id the id of the slotStatusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the slotStatusDTO, or with status 404 (Not Found)
     */
    @GetMapping("/slot-statuses/{id}")
    @Timed
    public ResponseEntity<SlotStatusDTO> getSlotStatus(@PathVariable Long id) {
        log.debug("REST request to get SlotStatus : {}", id);
        Optional<SlotStatusDTO> slotStatusDTO = slotStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(slotStatusDTO);
    }

    /**
     * DELETE  /slot-statuses/:id : delete the "id" slotStatus.
     *
     * @param id the id of the slotStatusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/slot-statuses/{id}")
    @Timed
    public ResponseEntity<Void> deleteSlotStatus(@PathVariable Long id) {
        log.debug("REST request to delete SlotStatus : {}", id);
        slotStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/slot-statuses?query=:query : search for the slotStatus corresponding
     * to the query.
     *
     * @param query the query of the slotStatus search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/slot-statuses")
    @Timed
    public ResponseEntity<List<SlotStatusDTO>> searchSlotStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SlotStatuses for query {}", query);
        Page<SlotStatusDTO> page = slotStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/slot-statuses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
