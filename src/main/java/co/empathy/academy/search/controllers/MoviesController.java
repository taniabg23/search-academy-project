package co.empathy.academy.search.controllers;

import co.empathy.academy.search.model.Basic;
import co.empathy.academy.search.services.MoviesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/imdb")
public class MoviesController {

    @Autowired
    private MoviesService moviesService;

    @Operation(summary = "Create new users with the data stored in a json file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "A problem with the file has occurred")
    })
    @Parameter(name = "file", required = true, description = "File with the movies' data")
    @PostMapping("")
    public ResponseEntity<List<Basic>> leerTsv(@RequestParam MultipartFile basics, @RequestParam MultipartFile akas,
                                               @RequestParam MultipartFile principals, @RequestParam MultipartFile ratings,
                                               @RequestParam MultipartFile crew, @RequestParam MultipartFile episodes) throws IOException {
        List<Basic> dataBasic = moviesService.readData(basics, akas, principals, ratings, crew, episodes);
        return ResponseEntity.status(HttpStatus.OK).body(dataBasic);
    }
}
