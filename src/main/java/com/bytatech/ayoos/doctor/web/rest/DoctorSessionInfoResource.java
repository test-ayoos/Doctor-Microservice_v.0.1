package com.bytatech.ayoos.doctor.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bytatech.ayoos.doctor.domain.DoctorSessionInfo;
import com.bytatech.ayoos.doctor.repository.search.DoctorSessionInfoSearchRepository;
import com.bytatech.ayoos.doctor.service.DoctorSessionInfoService;
import com.bytatech.ayoos.doctor.web.rest.errors.BadRequestAlertException;
import com.bytatech.ayoos.doctor.web.rest.util.HeaderUtil;
import com.bytatech.ayoos.doctor.web.rest.util.PaginationUtil;
import com.bytatech.ayoos.doctor.service.dto.DoctorSessionInfoDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing DoctorSessionInfo.
 */
@RestController
@RequestMapping("/api")
public class DoctorSessionInfoResource {

    private final Logger log = LoggerFactory.getLogger(DoctorSessionInfoResource.class);

    private static final String ENTITY_NAME = "doctorDoctorSessionInfo";
    
    
    @Autowired
    DoctorSessionInfoSearchRepository doctorSessionInfoSearchRepository;

    private final DoctorSessionInfoService doctorSessionInfoService;

    public DoctorSessionInfoResource(DoctorSessionInfoService doctorSessionInfoService) {
        this.doctorSessionInfoService = doctorSessionInfoService;
    }

    
    @PostMapping("/doctorsession")
    
    public void createDoctorSession(@RequestBody List<DoctorSessionInfoDTO> doctorSessionInfoList){
    LocalDate date;
    	for(DoctorSessionInfoDTO sessionsList : doctorSessionInfoList) {
			log.debug("REST request to save Session : {}", sessionsList);
			date=sessionsList.getDate();
			
			try {
				createDoctorSessionInfo(sessionsList);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	
    }
    
    
    
    /**
     * POST  /doctor-session-infos : Create a new doctorSessionInfo.
     *
     * @param doctorSessionInfoDTO the doctorSessionInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doctorSessionInfoDTO, or with status 400 (Bad Request) if the doctorSessionInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/doctor-session-infos")
    @Timed
    public ResponseEntity<DoctorSessionInfoDTO> createDoctorSessionInfo(@RequestBody DoctorSessionInfoDTO doctorSessionInfoDTO) throws URISyntaxException {
        log.debug("REST request to save DoctorSessionInfo : {}", doctorSessionInfoDTO);
        if (doctorSessionInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new doctorSessionInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DoctorSessionInfoDTO result = doctorSessionInfoService.save(doctorSessionInfoDTO);
        return ResponseEntity.created(new URI("/api/doctor-session-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /doctor-session-infos : Updates an existing doctorSessionInfo.
     *
     * @param doctorSessionInfoDTO the doctorSessionInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doctorSessionInfoDTO,
     * or with status 400 (Bad Request) if the doctorSessionInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the doctorSessionInfoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/doctor-session-infos")
    @Timed
    public ResponseEntity<DoctorSessionInfoDTO> updateDoctorSessionInfo(@RequestBody DoctorSessionInfoDTO doctorSessionInfoDTO) throws URISyntaxException {
        log.debug("REST request to update DoctorSessionInfo : {}", doctorSessionInfoDTO);
        if (doctorSessionInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DoctorSessionInfoDTO result = doctorSessionInfoService.save(doctorSessionInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doctorSessionInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /doctor-session-infos : get all the doctorSessionInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of doctorSessionInfos in body
     */
    @GetMapping("/doctor-session-infos")
    @Timed
    public ResponseEntity<List<DoctorSessionInfoDTO>> getAllDoctorSessionInfos(Pageable pageable) {
        log.debug("REST request to get a page of DoctorSessionInfos");
        Page<DoctorSessionInfoDTO> page = doctorSessionInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/doctor-session-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /doctor-session-infos/:id : get the "id" doctorSessionInfo.
     *
     * @param id the id of the doctorSessionInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doctorSessionInfoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/doctor-session-infos/{id}")
    @Timed
    public ResponseEntity<DoctorSessionInfoDTO> getDoctorSessionInfo(@PathVariable Long id) {
        log.debug("REST request to get DoctorSessionInfo : {}", id);
        Optional<DoctorSessionInfoDTO> doctorSessionInfoDTO = doctorSessionInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorSessionInfoDTO);
    }

    /**
     * DELETE  /doctor-session-infos/:id : delete the "id" doctorSessionInfo.
     *
     * @param id the id of the doctorSessionInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/doctor-session-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDoctorSessionInfo(@PathVariable Long id) {
        log.debug("REST request to delete DoctorSessionInfo : {}", id);
        doctorSessionInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/doctor-session-infos?query=:query : search for the doctorSessionInfo corresponding
     * to the query.
     *
     * @param query the query of the doctorSessionInfo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/doctor-session-infos")
    @Timed
    public ResponseEntity<List<DoctorSessionInfoDTO>> searchDoctorSessionInfos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DoctorSessionInfos for query {}", query);
        Page<DoctorSessionInfoDTO> page = doctorSessionInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/doctor-session-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/_searchs/{profileName}")
    public Set<DoctorSessionInfo> getSpec(@PathVariable String profileName){
    	
    	
    	System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++hello");
    	return  doctorSessionInfoSearchRepository.findBySpec2(profileName); 
    }
    
    

}
