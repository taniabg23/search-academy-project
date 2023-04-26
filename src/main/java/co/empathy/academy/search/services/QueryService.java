package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.Optional;

public interface QueryService {
    /**
     * Method that creates a bool should term query for the parameter field with the parameter values
     *
     * @param field  field of the query
     * @param values values we want to match in the query
     * @return a bool should term query
     */
    Query queryTermsBoolShould(String field, String values);

    /**
     * Method that creates a query for multiple bool should terms queries and range queries for all the fields
     * of a movie. Is not necessary to specify all the filters.
     *
     * @param yearMin    Minimum startYear of the movie
     * @param yearMax    Maximum startYear of the movie
     * @param ratingMin  Minimum rating of the movie
     * @param ratingMax  Maximum rating of the movie
     * @param minutesMin Minimum minutes of the movie
     * @param minutesMax Maximum minutes of the movie
     * @param type       Type of the movie
     * @param genres     Genres of the movie
     * @param values     Title of the movie
     * @return an all filters query
     */
    Query allFiltersQuery(Optional<Integer> yearMin, Optional<Integer> yearMax, Optional<Double> ratingMin, Optional<Double> ratingMax, Optional<Integer> minutesMin, Optional<Integer> minutesMax, Optional<String> type, Optional<String> genres, Optional<String> values);

    /**
     * Method that returns all the existing movie genres
     *
     * @return an aggregation of all the existing movie genres
     */
    Aggregation getGenres();
}
