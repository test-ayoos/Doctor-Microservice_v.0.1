package com.bytatech.ayoos.doctor.repository.search;

import com.bytatech.ayoos.doctor.domain.PaymentSettings;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentSettings entity.
 */
public interface PaymentSettingsSearchRepository extends ElasticsearchRepository<PaymentSettings, Long> {
}
