package co.empathy.academy.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class ElasticClientTest {

    @Mock
    private ElasticClient elasticClient;

    @BeforeEach
    void setUP() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenNothing_whenGetClusterName_thenReturnClusterName() throws IOException {
        when(elasticClient.getClusterName()).thenReturn("docker-cluster");
    }
}
