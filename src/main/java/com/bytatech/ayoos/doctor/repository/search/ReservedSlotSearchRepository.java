package com.bytatech.ayoos.doctor.repository.search;

import com.bytatech.ayoos.doctor.domain.ReservedSlot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReservedSlot entity.
 */
public interface ReservedSlotSearchRepository extends ElasticsearchRepository<ReservedSlot, Long> {
}
