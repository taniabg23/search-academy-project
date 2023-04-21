package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.empathy.academy.search.model.Movie;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ElasticClient {

    private ElasticsearchClient client;
    private static final String INDEX = "imdb";

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
        boolean exists = client.indices().exists(ExistsRequest.of(e -> e.index(INDEX))).value();
        if (exists) {
            client.indices().delete(i -> i.index(INDEX));
        }
        client.indices().create(i -> i.index(INDEX));
        setAnalyzer();
        setMapping();
    }

    private void setAnalyzer() {
        try {
            client.indices().close(c -> c.index(INDEX));
            InputStream jsonAnalyzer = new ClassPathResource("custom_analyzer.json").getInputStream();
            client.indices().putSettings(s -> s.index(INDEX).withJson(jsonAnalyzer));
            client.indices().open(o -> o.index(INDEX));
        } catch (IOException e) {
            throw new RuntimeException("Error en la lectura del analyzer");
        }
    }

    private void setMapping() {
        try {
            InputStream jsonMapping = new ClassPathResource("mapping.json").getInputStream();
            client.indices().putMapping(p -> p.index(INDEX).withJson(jsonMapping));
        } catch (IOException e) {
            throw new RuntimeException("Error en la lectura del mapping");
        }

    }

    public void bulkMovies(List<Movie> movies) {
        BulkRequest.Builder bulkRequest = new BulkRequest.Builder();

        for (Movie b : movies) {
            bulkRequest.operations(o -> o.index(i -> i.index(INDEX).id(b.getTconst()).document(b)));
        }

        try {
            client.bulk(bulkRequest.build());
        } catch (IOException e) {
            throw new RuntimeException("Ay mecachis");
        }
    }

    //////////  QUERIES

    public List<Object> executeQuery(Query query, int size) throws IOException {
        createConnection();

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX)
                .query(query)
                .size(size));
        System.out.println(searchRequest.toString());

        SearchResponse<Object> response = client.search(searchRequest, Object.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }
}
