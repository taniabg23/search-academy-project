package co.empathy.academy.search.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value
@AllArgsConstructor
public class Basic {
    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private boolean isAdult;
    @Nullable
    private int startYear;
    @Nullable
    private int endYear;
    @Nullable
    private int runTimeMinutos;
    private List<String> genres;
}
