package com.bytatech.ayoos.doctor.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytatech.ayoos.doctor.domain.DoctorSessionInfo;
import com.bytatech.ayoos.doctor.repository.DoctorRepository;
import com.bytatech.ayoos.doctor.repository.DoctorSessionInfoRepository;
import com.bytatech.ayoos.doctor.repository.search.DoctorSessionInfoSearchRepository;
import com.bytatech.ayoos.doctor.service.DoctorSessionInfoService;
import com.bytatech.ayoos.doctor.service.ReservedSlotService;
import com.bytatech.ayoos.doctor.service.dto.DoctorSessionInfoDTO;
import com.bytatech.ayoos.doctor.service.mapper.DoctorSessionInfoMapper;

/**
 * Service Implementation for managing DoctorSessionInfo.
 */
@Service
@Transactional
public class DoctorSessionInfoServiceImpl implements DoctorSessionInfoService {

    private final Logger log = LoggerFactory.getLogger(DoctorSessionInfoServiceImpl.class);
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    ReservedSlotService reservedSlotService;
    private final DoctorSessionInfoRepository doctorSessionInfoRepository;

    private final DoctorSessionInfoMapper doctorSessionInfoMapper;

    private final DoctorSessionInfoSearchRepository doctorSessionInfoSearchRepository;

    public DoctorSessionInfoServiceImpl(DoctorSessionInfoRepository doctorSessionInfoRepository, DoctorSessionInfoMapper doctorSessionInfoMapper, DoctorSessionInfoSearchRepository doctorSessionInfoSearchRepository) {
        this.doctorSessionInfoRepository = doctorSessionInfoRepository;
        this.doctorSessionInfoMapper = doctorSessionInfoMapper;
        this.doctorSessionInfoSearchRepository = doctorSessionInfoSearchRepository;
    }

    /**
     * Save a doctorSessionInfo.
     *
     * @param doctorSessionInfoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DoctorSessionInfoDTO save(DoctorSessionInfoDTO doctorSessionInfoDTO) {
        log.debug("Request to save DoctorSessionInfo : {}", doctorSessionInfoDTO);
        DoctorSessionInfo doctorSessionInfo = doctorSessionInfoMapper.toEntity(doctorSessionInfoDTO);
        doctorSessionInfo = doctorSessionInfoRepository.save(doctorSessionInfo);
        doctorSessionInfo = doctorSessionInfoRepository.save(doctorSessionInfo);
        DoctorSessionInfoDTO result = doctorSessionInfoMapper.toDto(doctorSessionInfo);
        doctorSessionInfoSearchRepository.save(doctorSessionInfo);
        return result;
    }

    /**
     * Get all the doctorSessionInfos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorSessionInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DoctorSessionInfos");
        return doctorSessionInfoRepository.findAll(pageable)
            .map(doctorSessionInfoMapper::toDto);
    }


    /**
     * Get one doctorSessionInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DoctorSessionInfoDTO> findOne(Long id) {
        log.debug("Request to get DoctorSessionInfo : {}", id);
        return doctorSessionInfoRepository.findById(id)
            .map(doctorSessionInfoMapper::toDto);
    }

    /**
     * Delete the doctorSessionInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DoctorSessionInfo : {}", id);
        doctorSessionInfoRepository.deleteById(id);
        doctorSessionInfoSearchRepository.deleteById(id);
    }

    /**
     * Search for the doctorSessionInfo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DoctorSessionInfoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DoctorSessionInfos for query {}", query);
        return doctorSessionInfoSearchRepository.search(queryStringQuery(query), pageable)
            .map(doctorSessionInfoMapper::toDto);
    }

	@Override
	public void setBusySession(String profileName,LocalDate date) {
		/*Set<DoctorSessionInfo> result =doctorRepository.findSessions(profileName, date);
   	 ZonedDateTime startTime;
   	 ZonedDateTime endTime;
   	 DoctorSessionInfo testInfo = new  DoctorSessionInfo();
   	
   		TreeSet<DoctorSessionInfo> treeSet= new TreeSet<DoctorSessionInfo>();
   		
   		for(DoctorSessionInfo si:result) {
   			 //System.out.println( "##############################################################STARTTIME:"+si.getStartTime()+"STARTTIME.HOUR:"+si.getStartTime().getHour());
       		 
   			 testInfo =si;
   			 //startTime=  ZonedDateTime .of( date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 00, 00, 00, 00, si.getStartTime().getZone() ); 
       		// System.out.println( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ENDTIME:"+si.getEndTime()+"ENDTIME.HOUR:"+si.getEndTime().getHour());
   			 treeSet.add(si);
   			 
   		}
   		startTime=  ZonedDateTime .of( date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 00, 00, 00, 00, testInfo.getStartTime().getZone() ); 
   		
   		endTime =  ZonedDateTime .of( date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 23, 00, 00, 00, testInfo.getStartTime().getZone() ); 
   		//  System.out.println("zoned date time testInfo.starttime:"+startTime);
   		 
   	
   	
   		 
   		
   		ReservedSlotDTO		reservedSlotDTO = new ReservedSlotDTO();
   		
   
		for(DoctorSessionInfo sinfo: treeSet) {
   			 reservedSlotDTO.setDate(date);
   			// System.out.println("condition 1,startTime:"+startTime+"compare with sinfo startTime:"+sinfo.getStartTime()+"ttt"+startTime.isBefore(sinfo.getStartTime()));
   			 
   			 
   			if(startTime.isBefore(sinfo.getStartTime())) {
   			
   				reservedSlotDTO.setStartTime(startTime);
   				reservedSlotDTO.setEndTime(sinfo.getStartTime());
   				//reservedSlotDTO.getSlotStatusId();
   				
   				System.out.println("############################"+reservedSlotDTO.getStartTime());
   				System.out.println("############################"+reservedSlotDTO.getEndTime());
   				
   				
   				startTime=sinfo.getEndTime();
   				System.out.println("condition1 starttime"+startTime);
   				reservedSlotService.save(reservedSlotDTO);
   			}
   		
   			 
   	}
   		 
   		 if(startTime.isBefore(endTime)){
				reservedSlotDTO.setStartTime(startTime);
				reservedSlotDTO.setEndTime(endTime);
				reservedSlotService.save(reservedSlotDTO);
			}
			 
			 */
	}
}
