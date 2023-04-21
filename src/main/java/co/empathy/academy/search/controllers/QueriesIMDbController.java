package co.empathy.academy.search.controllers;

import co.empathy.academy.search.services.SearchService;
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

    @GetMapping("")
    public ResponseEntity<List<Object>> searchByFilters(
            @RequestParam Optional<Integer> yearMin, @RequestParam Optional<Integer> yearMax,
            @RequestParam Optional<Double> ratingMin, @RequestParam Optional<Double> ratingMax,
            @RequestParam Optional<Integer> minutesMin, @RequestParam Optional<Integer> minutesMax,
            @RequestParam Optional<String> type, @RequestParam Optional<String> genres,
            @RequestParam Optional<String> sortRating) {
        try {
            System.out.println(1);
            return ResponseEntity.status(HttpStatus.OK).body(searchService.getListMoviesSearchAllFilters(yearMin, yearMax, ratingMin, ratingMax, minutesMin, minutesMax, type, genres, sortRating));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/terms")
    public ResponseEntity<List<Object>> searchByTerms(
            @RequestParam String field, @RequestParam String values) {
        try {
            System.out.println(1);
            return ResponseEntity.status(HttpStatus.OK).body(searchService.getListMoviesTerms(field, values));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
