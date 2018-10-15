package com.bytatech.ayoos.doctor.repository.search;

import com.bytatech.ayoos.doctor.domain.Workspace;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Workspace entity.
 */
public interface WorkspaceSearchRepository extends ElasticsearchRepository<Workspace, Long> {
}
