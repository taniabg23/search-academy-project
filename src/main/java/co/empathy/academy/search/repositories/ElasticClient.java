package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.empathy.academy.search.model.Basic;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ElasticClient {

    private ElasticsearchClient client;

    private void createConnection() {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
    }


    public String getClusterName() {
        createConnection();
        try {
            return client.cluster().state().valueBody().toJson().asJsonObject().getString("cluster_name");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createIndex() throws IOException {
        createConnection();
        client.indices().delete(i -> i.index("imdb"));
        client.indices().create(i -> i.index("imdb"));
    }

    public void bulkMovies(List<Basic> movies) {
        BulkRequest.Builder bulkRequest = new BulkRequest.Builder();

        for (Basic b : movies) {
            bulkRequest.operations(o -> o.index(i -> i.index("imdb").id(b.getTconst()).document(b)));
        }

        try {
            client.bulk(bulkRequest.build());
        } catch (IOException e) {
            throw new RuntimeException("Ay mecachis");
        }
    }
}
