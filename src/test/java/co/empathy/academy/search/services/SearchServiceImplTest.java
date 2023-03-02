package co.empathy.academy.search.services;

import co.empathy.academy.search.ElasticClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SearchServiceImplTest {

    @Mock
    private ElasticClient ec;
    @InjectMocks
    private SearchServiceImpl searchService;

    @BeforeEach
    void setUP() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenQuery_whenSearch_thenReturnJson() {
        when(ec.getClusterName()).thenReturn("docker-cluster");
        assertEquals("{\n\t\"query\": \"abc\",\n\t\"clusterName\": \"docker-cluster\"\n}", searchService.getJsonQueryAndClusterName("abc"));
    }


}
