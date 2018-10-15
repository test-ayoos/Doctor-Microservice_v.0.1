package com.bytatech.ayoos.doctor.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SlotStatusSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SlotStatusSearchRepositoryMockConfiguration {

    @MockBean
    private SlotStatusSearchRepository mockSlotStatusSearchRepository;

}
