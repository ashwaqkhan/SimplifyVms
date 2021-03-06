package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.InterviewInformation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link InterviewInformation} entity.
 */
public interface InterviewInformationSearchRepository extends ElasticsearchRepository<InterviewInformation, Long> {}
