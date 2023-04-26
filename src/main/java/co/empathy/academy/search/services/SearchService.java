package co.empathy.academy.search.services;

import co.empathy.academy.search.util.Bucket;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SearchService {
    /**
     * Method that returns json formed string with the parameter query and the Elasticsearch clustername
     *
     * @param query The query you want to use
     * @return a json formed string
     */
    String getJsonQueryAndClusterName(String query);

    /**
     * Method that returns a list of objects that match the parameter field with the parameter values
     *
     * @param field  field of the values
     * @param values values we want to match
     * @return a list of the movies that match
     * @throws IOException
     */
    List<Object> getListMoviesTerms(String field, String values) throws IOException;

    /**
     * Method that return a list of objects that match all the field values
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
     * @return a list of the movies that match
     * @throws IOException
     */
    List<Object> getListMoviesSearchAllFilters(Optional<Integer> yearMin, Optional<Integer> yearMax, Optional<Double> ratingMin, Optional<Double> ratingMax, Optional<Integer> minutesMin, Optional<Integer> minutesMax, Optional<String> type, Optional<String> genres, Optional<String> values) throws IOException;

    /**
     * Method that return a list of buckets of the genres of the movies
     *
     * @return a list of buckets of the genres of the movies
     * @throws IOException
     */
    List<Bucket> getGenres() throws IOException;
}
