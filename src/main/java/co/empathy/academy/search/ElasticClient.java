package co.empathy.academy.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
}
