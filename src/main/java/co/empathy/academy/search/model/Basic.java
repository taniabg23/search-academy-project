package co.empathy.academy.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Basic {
    String tconst;
    String titleType;
    String primaryTitle;
    String originalTitle;
    boolean isAdult;
    int startYear;
    int endYear;
    int runTimeMinutos;
    List<String> genres;
    List<Aka> akas;
    List<Principal> principals;
    List<String> directors;
    List<String> writers;
    List<Episode> episodes;
    double averageRating;
    int numVotes;

    public String getTconst() {
        return this.tconst;
    }
}
