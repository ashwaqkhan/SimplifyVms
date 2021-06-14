package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Apply;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Apply} entity.
 */
public interface ApplySearchRepository extends ElasticsearchRepository<Apply, Long> {}
