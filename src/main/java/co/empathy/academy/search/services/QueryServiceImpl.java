package co.empathy.academy.search.services;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.json.JsonData;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class QueryServiceImpl {

    public Query queryTermsBoolShould(String field, String values) {
        String[] valuesTerm = values.split(",");
        List<Query> queries = new LinkedList<>();

        for (String value : valuesTerm) {
            queries.add(TermQuery.of(t -> t.
                            value(value.toLowerCase()).
                            field(field)).
                    _toQuery());
        }

        System.out.println(3);
        Query boolQuery = BoolQuery.of(b -> b.should(queries))._toQuery();
        System.out.println(boolQuery.toString());
        return boolQuery;
    }

    public Query queryRange(String field, String value, int low, int high) {
        Query queryRange = RangeQuery.of(r -> r.
                        field(field).
                        gte(JsonData.of(low)).
                        gte(JsonData.of(high))).
                _toQuery();
        return queryRange;
    }

    public Query allFiltersQuery(Optional<Integer> yearMin, Optional<Integer> yearMax, Optional<Double> ratingMin, Optional<Double> ratingMax, Optional<Integer> minutesMin, Optional<Integer> minutesMax, Optional<String> type, Optional<String> genres, Optional<String> sortRating) {
        List<Query> queries = new LinkedList<>();

        int yearMinValue, yearMaxValue, minutesMinValue, minutesMaxValue;
        double ratingMinValue, ratingMaxValue;
        String typeValue, genresValue, sortRatingValue;

        if (yearMin.isPresent() || yearMax.isPresent()) {
            yearMinValue = (yearMin.isPresent())
                    ? yearMin.get()
                    : 0;
            yearMaxValue = (yearMax.isPresent())
                    ? yearMax.get()
                    : Integer.MAX_VALUE;
            queries.add(RangeQuery.of(r -> r.
                            field("startYear").
                            gte(JsonData.of(yearMinValue)).
                            gte(JsonData.of(yearMaxValue))).
                    _toQuery());
        }

        if (minutesMin.isPresent() || minutesMax.isPresent()) {
            minutesMinValue = (minutesMin.isPresent())
                    ? minutesMin.get()
                    : 0;
            minutesMaxValue = (minutesMax.isPresent())
                    ? minutesMax.get()
                    : Integer.MAX_VALUE;
            queries.add(RangeQuery.of(r -> r.
                            field("runtimeMinutes").
                            gte(JsonData.of(minutesMinValue)).
                            gte(JsonData.of(minutesMaxValue))).
                    _toQuery());
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
                            gte(JsonData.of(ratingMaxValue))).
                    _toQuery());
        }


        Query mustQuery = BoolQuery.of(b -> b.must(queries))._toQuery();
        return mustQuery;
    }
}
