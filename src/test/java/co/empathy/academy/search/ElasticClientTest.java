package co.empathy.academy.search;

import co.empathy.academy.search.repositories.ElasticClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElasticClientTest {

    @InjectMocks
    private ElasticClient elasticClient;

    @BeforeEach
    void setUP() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenNothing_whenGetClusterName_thenReturnClusterName() throws IOException {
        assertEquals("docker-cluster", elasticClient.getClusterName());
    }
}
