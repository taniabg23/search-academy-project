package co.empathy.academy.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Movie {
    String tconst;
    String titleType;
    String primaryTitle;
    String originalTitle;
    @JsonProperty("isAdult")
    boolean isAdult;
    int startYear;
    int endYear;
    int runtimeMinutes;
    List<String> genres;
    double averageRating;
    int numVotes;
    List<Aka> akas;
    List<Director> directors;
    List<Principal> starring;
}
