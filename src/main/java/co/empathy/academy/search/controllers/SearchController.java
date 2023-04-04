package co.empathy.academy.search.controllers;

import co.empathy.academy.search.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Operation(summary = "Get a json with your query and the cluster name.")
    @Parameter(name = "query", required = true, description = "The query you want to use")
    @GetMapping("/search")
    public String search(@RequestParam String query) {
        return this.searchService.getJsonQueryAndClusterName(query);
    }
}