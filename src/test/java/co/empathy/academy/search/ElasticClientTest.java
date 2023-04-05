package co.empathy.academy.search;

import co.empathy.academy.search.repositories.ElasticClient;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class ElasticClientTest {

    @InjectMocks
    private ElasticClient elasticClient;

    @BeforeEach
    void setUP() {
        MockitoAnnotations.openMocks(this);
    }

    /*
    @Test
    void givenNothing_whenGetClusterName_thenReturnClusterName() throws IOException {
        assertEquals("docker-cluster", elasticClient.getClusterName());
    }
     */
}
