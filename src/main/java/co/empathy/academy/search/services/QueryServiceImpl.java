package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.json.JsonData;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class QueryServiceImpl implements QueryService {

    @Override
    public Query queryTermsBoolShould(String field, String values) {
        String[] valuesTerm = values.split(",");
        List<Query> queries = new LinkedList<>();

        for (String value : valuesTerm) {
            queries.add(TermQuery.of(t -> t.
                            value(value.toLowerCase()).
                            field(field)).
                    _toQuery());
        }

        Query boolQuery = BoolQuery.of(b -> b.should(queries))._toQuery();
        return boolQuery;
    }

    /**
     * Method that creates a range Query fot the parameter field with the parameter values
     *
     * @param field field of the range
     * @param low   minimum value of the field
     * @param high  maximum value of the field
     * @return a range query
     */
    private Query queryRange(String field, int low, int high) {
        Query queryRange = RangeQuery.of(r -> r.
                        field(field).
                        gte(JsonData.of(low)).
                        lte(JsonData.of(high))).
                _toQuery();
        return queryRange;
    }

    @Override
    public Query allFiltersQuery(Optional<Integer> yearMin, Optional<Integer> yearMax, Optional<Double> ratingMin, Optional<Double> ratingMax, Optional<Integer> minutesMin, Optional<Integer> minutesMax, Optional<String> type, Optional<String> genres, Optional<String> values) {
        List<Query> queries = new LinkedList<>();

        int yearMinValue, yearMaxValue, minutesMinValue, minutesMaxValue;
        double ratingMinValue, ratingMaxValue;

        if (values.isPresent()) {
            queries.add(queryTermsBoolShould("primaryTitle", values.get()));
        }

        if (type.isPresent()) {
            queries.add(queryTermsBoolShould("titleType", type.get()));
        }

        if (genres.isPresent()) {
            queries.add(queryTermsBoolShould("genres", genres.get()));
        }

        if (yearMin.isPresent() || yearMax.isPresent()) {
            yearMinValue = (yearMin.isPresent())
                    ? yearMin.get()
                    : 0;
            yearMaxValue = (yearMax.isPresent())
                    ? yearMax.get()
                    : Integer.MAX_VALUE;
            queries.add(queryRange("startYear", yearMinValue, yearMaxValue));
        }

        if (minutesMin.isPresent() || minutesMax.isPresent()) {
            minutesMinValue = (minutesMin.isPresent())
                    ? minutesMin.get()
                    : 0;
            minutesMaxValue = (minutesMax.isPresent())
                    ? minutesMax.get()
                    : Integer.MAX_VALUE;
            queries.add(queryRange("runtimeMinutes", minutesMinValue, minutesMaxValue));
        }

        if (ratingMin.isPresent() || ratingMax.isPresent()) {
            ratingMinValue = (ratingMin.isPresent())
                    ? ratingMin.get()
                    : 0.0;
            ratingMaxValue = (ratingMax.isPresent())
                    ? ratingMax.get()
                    : Double.MAX_VALUE;
            queries.add(RangeQuery.of(r -> r.
                            field("averageRating").
                            gte(JsonData.of(ratingMinValue)).
                            lte(JsonData.of(ratingMaxValue))).
                    _toQuery());
        }


        Query mustQuery = BoolQuery.of(b -> b.must(queries))._toQuery();
        return mustQuery;
    }

    @Override
    public Aggregation getGenres() {
        Aggregation aggs = Aggregation.of(a -> a.terms(TermsAggregation.of(t -> t.field("genres"))));
        return aggs;
    }
}
