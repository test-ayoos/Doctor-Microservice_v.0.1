package com.bytatech.ayoos.doctor.repository.search;

import com.bytatech.ayoos.doctor.domain.ProfileInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProfileInfo entity.
 */
public interface ProfileInfoSearchRepository extends ElasticsearchRepository<ProfileInfo, Long> {
}
