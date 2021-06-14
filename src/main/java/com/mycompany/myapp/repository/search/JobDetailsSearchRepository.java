package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.JobDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link JobDetails} entity.
 */
public interface JobDetailsSearchRepository extends ElasticsearchRepository<JobDetails, Long> {}
