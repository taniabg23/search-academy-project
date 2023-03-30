package co.empathy.academy.search.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Episode {
    private String tconst;
    private int seasonNumber;
    private int episodeNumber;
}
