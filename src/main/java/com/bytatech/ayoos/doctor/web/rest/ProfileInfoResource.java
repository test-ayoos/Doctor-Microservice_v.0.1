package com.bytatech.ayoos.doctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.doctor.service.ProfileInfoService;
import com.bytatech.ayoos.doctor.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.doctor.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.doctor.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.doctor.service.dto.ProfileInfoDTO;
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
 * REST controller for managing ProfileInfo.
 */
@RestController
@RequestMapping("/api")
public class ProfileInfoResource {

    private final Logger log = LoggerFactory.getLogger(ProfileInfoResource.class);

    private static final String ENTITY_NAME = "doctorProfileInfo";

    private final ProfileInfoService profileInfoService;

    public ProfileInfoResource(ProfileInfoService profileInfoService) {
        this.profileInfoService = profileInfoService;
    }

    /**
     * POST  /profile-infos : Create a new profileInfo.
     *
     * @param profileInfoDTO the profileInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profileInfoDTO, or with status 400 (Bad Request) if the profileInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profile-infos")
    @Timed
    public ResponseEntity<ProfileInfoDTO> createProfileInfo(@RequestBody ProfileInfoDTO profileInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ProfileInfo : {}", profileInfoDTO);
        if (profileInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfileInfoDTO result = profileInfoService.save(profileInfoDTO);
        return ResponseEntity.created(new URI("/api/profile-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profile-infos : Updates an existing profileInfo.
     *
     * @param profileInfoDTO the profileInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profileInfoDTO,
     * or with status 400 (Bad Request) if the profileInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the profileInfoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profile-infos")
    @Timed
    public ResponseEntity<ProfileInfoDTO> updateProfileInfo(@RequestBody ProfileInfoDTO profileInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ProfileInfo : {}", profileInfoDTO);
        if (profileInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfileInfoDTO result = profileInfoService.save(profileInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profileInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profile-infos : get all the profileInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of profileInfos in body
     */
    @GetMapping("/profile-infos")
    @Timed
    public ResponseEntity<List<ProfileInfoDTO>> getAllProfileInfos(Pageable pageable) {
        log.debug("REST request to get a page of ProfileInfos");
        Page<ProfileInfoDTO> page = profileInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/profile-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /profile-infos/:id : get the "id" profileInfo.
     *
     * @param id the id of the profileInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profileInfoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/profile-infos/{id}")
    @Timed
    public ResponseEntity<ProfileInfoDTO> getProfileInfo(@PathVariable Long id) {
        log.debug("REST request to get ProfileInfo : {}", id);
        Optional<ProfileInfoDTO> profileInfoDTO = profileInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileInfoDTO);
    }

    /**
     * DELETE  /profile-infos/:id : delete the "id" profileInfo.
     *
     * @param id the id of the profileInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profile-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfileInfo(@PathVariable Long id) {
        log.debug("REST request to delete ProfileInfo : {}", id);
        profileInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/profile-infos?query=:query : search for the profileInfo corresponding
     * to the query.
     *
     * @param query the query of the profileInfo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/profile-infos")
    @Timed
    public ResponseEntity<List<ProfileInfoDTO>> searchProfileInfos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProfileInfos for query {}", query);
        Page<ProfileInfoDTO> page = profileInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/profile-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
