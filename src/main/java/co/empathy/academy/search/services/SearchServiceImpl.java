package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.empathy.academy.search.repositories.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticClient elastic;
    private QueryServiceImpl queryService = new QueryServiceImpl();

    @Override
    public String getJsonQueryAndClusterName(String query) {
        String ret = "{\n";
        ret += "\t\"query\": \"" + query + "\",\n";
        ret += "\t\"clusterName\": \"" + elastic.getClusterName() + "\"\n";
        ret += "}";
        return ret;
    }

    @Override
    public List<Object> getListMoviesTerms(String field, String values) throws IOException {
        return elastic.executeQuery(queryService.queryTermsBoolShould(field, values), 100);
    }

    @Override
    public List<Object> getListMoviesSearchAllFilters(Optional<Integer> yearMin, Optional<Integer> yearMax, Optional<Double> ratingMin, Optional<Double> ratingMax, Optional<Integer> minutesMin, Optional<Integer> minutesMax, Optional<String> type, Optional<String> genres, Optional<String> values) throws IOException {
        return elastic.executeQuery(queryService.allFiltersQuery(yearMin, yearMax, ratingMin, ratingMax, minutesMin, minutesMax, type, genres, values), 100);
    }

    @Override
    public List<StringTermsBucket> getGenres() throws IOException {
        return elastic.executeAggs(queryService.getGenres());
    }

}
