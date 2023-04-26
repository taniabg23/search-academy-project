package co.empathy.academy.search.controllers;

import co.empathy.academy.search.services.SearchService;
import co.empathy.academy.search.util.Bucket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/search")
public class QueriesIMDbController {

    @Autowired
    private SearchService searchService;

    @Operation(summary = "Returns a list of movies filters by the filters applied")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "A problem with Elasticsearch has occurred")
    })
    @Parameters(value = {
            @Parameter(name = "yearMin", required = false, description = "Minimum startYear of the movie"),
            @Parameter(name = "yearMax", required = false, description = "Maximum startYear of the movie"),
            @Parameter(name = "ratingMin", required = false, description = "Minimum rating of the movie"),
            @Parameter(name = "ratingMax", required = false, description = "Maximum rating of the movie"),
            @Parameter(name = "minutesMin", required = false, description = "Minimum minutes of the movie"),
            @Parameter(name = "minutesMax", required = false, description = "Maximum minutes of the movie"),
            @Parameter(name = "type", required = false, description = "Type of the movie"),
            @Parameter(name = "genres", required = false, description = "Genres of the movie"),
            @Parameter(name = "values", required = false, description = "Title of the movie")
    })
    @GetMapping("")
    public ResponseEntity<List<Object>> searchByFilters(
            @RequestParam Optional<Integer> yearMin, @RequestParam Optional<Integer> yearMax,
            @RequestParam Optional<Double> ratingMin, @RequestParam Optional<Double> ratingMax,
            @RequestParam Optional<Integer> minutesMin, @RequestParam Optional<Integer> minutesMax,
            @RequestParam Optional<String> type, @RequestParam Optional<String> genres,
            @RequestParam Optional<String> values) {
        try {
            System.out.println(1);
            return ResponseEntity.status(HttpStatus.OK).body(searchService.getListMoviesSearchAllFilters(yearMin, yearMax, ratingMin, ratingMax, minutesMin, minutesMax, type, genres, values));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Returns the movies that match the values for the filter applied")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "A problem with Elasticsearch has occurred")
    })
    @Parameters(value = {
            @Parameter(name = "field", required = true, description = "Field of the values applied"),
            @Parameter(name = "values", required = true, description = "Values that we are looking for")
    })
    @GetMapping("/terms")
    public ResponseEntity<List<Object>> searchByTerms(
            @RequestParam String field, @RequestParam String values) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(searchService.getListMoviesTerms(field, values));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Returns the genres that exist for the movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "A problem with Elasticsearch has occurred")
    })
    @GetMapping("/genres")
    public ResponseEntity<List<Bucket>> getGenres() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(searchService.getGenres());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
