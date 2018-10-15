package com.bytatech.ayoos.doctor.repository.search;

import com.bytatech.ayoos.doctor.domain.Doctor;
import com.bytatech.ayoos.doctor.domain.DoctorSessionInfo;

import java.util.Set;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Doctor entity.
 */
public interface DoctorSearchRepository extends ElasticsearchRepository<Doctor, Long> {
	 @Query("{\"bool\" : {\"must\" : [{\"match\" : {\"doctorSessionInfos.sessionName\" : \"?0\"}}]}}")
	 Set<DoctorSessionInfo> findBySpec(String sessionName);

	/* @Query("{\"bool\": {\"must\": [{\"match\": {\"authors.name\": \"?0\"}}]}}")
	 Set<Doctor> findBySpeciay(String name, Pageable pageable);*/

}
