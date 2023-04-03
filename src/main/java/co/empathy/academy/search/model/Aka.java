package co.empathy.academy.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import javax.annotation.Nullable;

@Value
@AllArgsConstructor
@Getter
public class Aka {
    private String title;
    @Nullable
    private String region;
    @Nullable
    private String language;
    private boolean isOriginalTitle;
}
