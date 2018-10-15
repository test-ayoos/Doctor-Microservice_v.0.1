package com.bytatech.ayoos.doctor.repository.search;

import com.bytatech.ayoos.doctor.domain.SlotStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SlotStatus entity.
 */
public interface SlotStatusSearchRepository extends ElasticsearchRepository<SlotStatus, Long> {
}
