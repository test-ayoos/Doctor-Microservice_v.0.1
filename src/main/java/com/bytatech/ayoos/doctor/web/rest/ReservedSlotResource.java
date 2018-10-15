package com.bytatech.ayoos.doctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.doctor.service.ReservedSlotService;
import com.bytatech.ayoos.doctor.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.doctor.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.doctor.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.doctor.service.dto.ReservedSlotDTO;
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
 * REST controller for managing ReservedSlot.
 */
@RestController
@RequestMapping("/api")
public class ReservedSlotResource {

    private final Logger log = LoggerFactory.getLogger(ReservedSlotResource.class);

    private static final String ENTITY_NAME = "doctorReservedSlot";

    private final ReservedSlotService reservedSlotService;

    public ReservedSlotResource(ReservedSlotService reservedSlotService) {
        this.reservedSlotService = reservedSlotService;
    }

    /**
     * POST  /reserved-slots : Create a new reservedSlot.
     *
     * @param reservedSlotDTO the reservedSlotDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reservedSlotDTO, or with status 400 (Bad Request) if the reservedSlot has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reserved-slots")
    @Timed
    public ResponseEntity<ReservedSlotDTO> createReservedSlot(@RequestBody ReservedSlotDTO reservedSlotDTO) throws URISyntaxException {
        log.debug("REST request to save ReservedSlot : {}", reservedSlotDTO);
        if (reservedSlotDTO.getId() != null) {
            throw new BadRequestAlertException("A new reservedSlot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReservedSlotDTO result = reservedSlotService.save(reservedSlotDTO);
        return ResponseEntity.created(new URI("/api/reserved-slots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reserved-slots : Updates an existing reservedSlot.
     *
     * @param reservedSlotDTO the reservedSlotDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reservedSlotDTO,
     * or with status 400 (Bad Request) if the reservedSlotDTO is not valid,
     * or with status 500 (Internal Server Error) if the reservedSlotDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reserved-slots")
    @Timed
    public ResponseEntity<ReservedSlotDTO> updateReservedSlot(@RequestBody ReservedSlotDTO reservedSlotDTO) throws URISyntaxException {
        log.debug("REST request to update ReservedSlot : {}", reservedSlotDTO);
        if (reservedSlotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReservedSlotDTO result = reservedSlotService.save(reservedSlotDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reservedSlotDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reserved-slots : get all the reservedSlots.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of reservedSlots in body
     */
    @GetMapping("/reserved-slots")
    @Timed
    public ResponseEntity<List<ReservedSlotDTO>> getAllReservedSlots(Pageable pageable) {
        log.debug("REST request to get a page of ReservedSlots");
        Page<ReservedSlotDTO> page = reservedSlotService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reserved-slots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reserved-slots/:id : get the "id" reservedSlot.
     *
     * @param id the id of the reservedSlotDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reservedSlotDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reserved-slots/{id}")
    @Timed
    public ResponseEntity<ReservedSlotDTO> getReservedSlot(@PathVariable Long id) {
        log.debug("REST request to get ReservedSlot : {}", id);
        Optional<ReservedSlotDTO> reservedSlotDTO = reservedSlotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservedSlotDTO);
    }

    /**
     * DELETE  /reserved-slots/:id : delete the "id" reservedSlot.
     *
     * @param id the id of the reservedSlotDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reserved-slots/{id}")
    @Timed
    public ResponseEntity<Void> deleteReservedSlot(@PathVariable Long id) {
        log.debug("REST request to delete ReservedSlot : {}", id);
        reservedSlotService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reserved-slots?query=:query : search for the reservedSlot corresponding
     * to the query.
     *
     * @param query the query of the reservedSlot search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reserved-slots")
    @Timed
    public ResponseEntity<List<ReservedSlotDTO>> searchReservedSlots(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReservedSlots for query {}", query);
        Page<ReservedSlotDTO> page = reservedSlotService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reserved-slots");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
