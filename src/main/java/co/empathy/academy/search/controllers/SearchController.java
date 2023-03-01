package co.empathy.academy.search.controllers;

import co.empathy.academy.search.services.SearchService;
import co.empathy.academy.search.services.SearchServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private SearchService searchService;

    public SearchController() {
        this.searchService = new SearchServiceImpl();
    }

    @RequestMapping("/search")
    public String search(@RequestParam String query) {
        return this.searchService.getJsonQueryAndClusterName(query);
    }
}
