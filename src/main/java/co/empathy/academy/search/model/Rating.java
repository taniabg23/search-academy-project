package co.empathy.academy.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@AllArgsConstructor
@Getter
public class Rating {

    //private String tconst;
    private double averageRating;
    private int numVotes;
}
