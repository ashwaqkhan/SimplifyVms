package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.BasicDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link BasicDetails} entity.
 */
public interface BasicDetailsSearchRepository extends ElasticsearchRepository<BasicDetails, Long> {}
