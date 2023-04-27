package co.empathy.academy.search.repositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
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
import co.empathy.academy.search.util.Bucket;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@Component
public class ElasticClient {

    private ElasticsearchClient client;
    private static final String INDEX = "imdb";

    /**
     * Method that connects to the Elasticsearch API Client
     */
    private void createConnection() {
        // Create the low-level client
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200),
                new HttpHost("elasticsearch", 9200)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
    }


    /**
     * Method that return the Elasticsearch cluster name
     *
     * @return Elasticsearch cluster name
     */
    public String getClusterName() {
        createConnection();
        try {
            return client.cluster().state().valueBody().toJson().asJsonObject().getString("cluster_name");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method that creates a new index or deletes that it's called the same way and creates a new one.
     * It also sets the analyzer and the mapping to the new index.
     *
     * @throws IOException
     */
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

    /**
     * Method that sets the analyzer to the index
     */
    private void setAnalyzer() {
        try {
            client.indices().close(c -> c.index(INDEX));
            InputStream jsonAnalyzer = new ClassPathResource("custom_analyzer.json").getInputStream();
            client.indices().putSettings(s -> s.index(INDEX).withJson(jsonAnalyzer));
            client.indices().open(o -> o.index(INDEX));
        } catch (IOException e) {
            throw new RuntimeException("Error reading the analyzer file");
        }
    }

    /**
     * Method that sets the mapping to the index
     */
    private void setMapping() {
        try {
            InputStream jsonMapping = new ClassPathResource("mapping.json").getInputStream();
            client.indices().putMapping(p -> p.index(INDEX).withJson(jsonMapping));
        } catch (IOException e) {
            throw new RuntimeException("Error reading the mapping file");
        }

    }

    /**
     * Method that bulks all the movies of the parameter list to the Elasticsearch index
     *
     * @param movies list of movies we want to bulk
     */
    public void bulkMovies(List<Movie> movies) {
        BulkRequest.Builder bulkRequest = new BulkRequest.Builder();

        for (Movie b : movies) {
            bulkRequest.operations(o -> o.index(i -> i.index(INDEX).id(b.getTconst()).document(b)));
        }

        try {
            client.bulk(bulkRequest.build());
        } catch (IOException e) {
            throw new RuntimeException("Error in bulk");
        }
    }

    /**
     * Method that executes the parameter query and returns max the parameter size objects
     *
     * @param query query we want the movies to match
     * @param size  max movies we want to get
     * @return a list of movies that match with the query
     * @throws IOException
     */
    public List<Object> executeQuery(Query query, int size) throws IOException {
        createConnection();

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX)
                .query(query)
                .size(size));

        SearchResponse<Object> response = client.search(searchRequest, Object.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }

    /**
     * Method that executes the parameter query and returns max the parameter size objects and sort the results
     *
     * @param query query we want the movies to match
     * @param sort  wich field they are sorted by
     * @param size  max movies we want to get
     * @return a list of movies that match with the query
     * @throws IOException
     */
    public List<Object> executeQuery(Query query, String sort, int size) throws IOException {
        createConnection();

        SortOptions sortOptions = SortOptions.of(
                s -> s.field(
                        FieldSort.of(
                                f -> f.field(sort).
                                        order(SortOrder.Desc))));

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX)
                .query(query)
                .sort(sortOptions)
                .size(size));

        SearchResponse<Object> response = client.search(searchRequest, Object.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }

    /**
     * Method that executes the parameter aggregation
     *
     * @param aggs aggregation we want to execute
     * @return a list of buckets of the aggregation
     * @throws IOException
     */
    public List<Bucket> executeAggs(Aggregation aggs) throws IOException {
        createConnection();

        SearchRequest searchRequest = SearchRequest.of(s -> s.
                index(INDEX).
                aggregations("genres", aggs));

        SearchResponse<Object> response = client.search(searchRequest, Object.class);

        List<StringTermsBucket> buckets = response.aggregations().get("genres").sterms().buckets().array();
        List<Bucket> bucketList = new LinkedList<>();

        for (StringTermsBucket bucket : buckets) {
            bucketList.add(new Bucket(bucket.key().stringValue(), bucket.docCount()));
        }

        return bucketList;
    }
}
