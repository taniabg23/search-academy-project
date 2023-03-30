package co.empathy.academy.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value
@AllArgsConstructor
@Getter
public class Aka {
    //private String tconst;
    private String title;
    @Nullable
    private String region;
    @Nullable
    private String language;
    @Nullable
    private List<String> types;
    @Nullable
    private List<String> attributes;
    private boolean isOriginalTitle;
}
