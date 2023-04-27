package co.empathy.academy.search.services;

import co.empathy.academy.search.repositories.ElasticClient;
import co.empathy.academy.search.util.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticClient elastic;
    private QueryService queryService = new QueryServiceImpl();

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
    public List<Object> getListMoviesSearchAllFilters(Optional<String> genres, Optional<String> type,
                                                      Optional<Integer> yearMax, Optional<Integer> yearMin,
                                                      Optional<Integer> minutesMax, Optional<Integer> minutesMin,
                                                      Optional<Double> ratingMax, Optional<Double> ratingMin,
                                                      Optional<Integer> size, Optional<String> sort,
                                                      Optional<String> values) throws IOException {
        int sizeToUse;
        if (size.isPresent()) {
            sizeToUse = size.get();
        } else {
            sizeToUse = 100;
        }
        if (sort.isPresent()) {
            return elastic.executeQuery(queryService.allFiltersQuery(genres, type, yearMax, yearMin, minutesMax, minutesMin, ratingMax, ratingMin, values), sort.get(), sizeToUse);
        } else {
            return elastic.executeQuery(queryService.allFiltersQuery(genres, type, yearMax, yearMin, minutesMax, minutesMin, ratingMax, ratingMin, values), sizeToUse);
        }
    }

    @Override
    public List<Bucket> getGenres() throws IOException {
        return elastic.executeAggs(queryService.getGenres());
    }

}
