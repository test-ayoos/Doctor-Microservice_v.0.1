package com.bytatech.ayoos.doctor.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of DoctorSessionInfoSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class DoctorSessionInfoSearchRepositoryMockConfiguration {

    @MockBean
    private DoctorSessionInfoSearchRepository mockDoctorSessionInfoSearchRepository;

}
