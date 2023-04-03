package co.empathy.academy.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Movie {
    String tconst;
    String titleType;
    String primaryTitle;
    String originalTitle;
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

    //List<String> writers;
    //List<Episode> episodes;

}
