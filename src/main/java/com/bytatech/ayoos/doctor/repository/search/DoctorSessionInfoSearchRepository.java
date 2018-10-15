package com.bytatech.ayoos.doctor.repository.search;

import com.bytatech.ayoos.doctor.domain.Doctor;
import com.bytatech.ayoos.doctor.domain.DoctorSessionInfo;

import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DoctorSessionInfo entity.
 */
public interface DoctorSessionInfoSearchRepository extends ElasticsearchRepository<DoctorSessionInfo, Long> {
	/* @Query("{\"bool\" : {\"must\" : [{\"match\" : {\"specialisation\" : \"?0\"}}]}}")
	 Set<DoctorSessionInfo> findBySessionInfo(String specialisation);
*/
	/*{
		  "query": {
		    "bool": {
		      "must": [{"term" :{"sessionName":"morning"}},{"term":{"doctor.profileInfo.profileName":"ajith"}}
		      ]
		      
		    }
		  }
		}*/
	
	/* @Query("{\"bool\" : {\"must\" : [{\"match\" : {\"sessionName\" : \"?0\"}}]}}")
	 Set<DoctorSessionInfo> findBySpec(String sessionName);*/
	 
	 
	 
	/* @Query("{\"bool\" : {\"must\" : [{\"term\" : {\"doctorSessionInfos.sessionName\" : \"?0\"}},{\"term\" : {\"profileInfo.profileName\" : \"?1\"}}]}}")
	 Set<DoctorSessionInfo> findBySpec(String sessionName,String profileName);*/
	 
	 
	 
	 @Query("{\"bool\" : {\"must\" : [{\"term\" : {\"doctor.profileInfo.profileName\" : \"?0\"}}]}}")
	 Set<DoctorSessionInfo> findBySpec2(String profileName);
	 
}
