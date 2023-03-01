package co.empathy.academy.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;

public class ElasticClient {
    private RestClient restClient;
    private ElasticsearchTransport transport;
    private ElasticsearchClient client;

    public ElasticClient() {
        // Create the low-level client
        restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();

        // Create the transport with a Jackson mapper
        transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
    }


    public String getClusterName() throws IOException {
        return client.cluster().state().valueBody().toJson().asJsonObject().getString("cluster_name");
    }
}
