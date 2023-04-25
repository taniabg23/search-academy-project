package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SearchService {
    String getJsonQueryAndClusterName(String query);

    List<Object> getListMoviesTerms(String field, String values) throws IOException;

    List<Object> getListMoviesSearchAllFilters(Optional<Integer> yearMin, Optional<Integer> yearMax, Optional<Double> ratingMin, Optional<Double> ratingMax, Optional<Integer> minutesMin, Optional<Integer> minutesMax, Optional<String> type, Optional<String> genres, Optional<String> values) throws IOException;

    List<StringTermsBucket> getGenres() throws IOException;
}
