package co.empathy.academy.search.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isOriginalTitle")
    private boolean isOriginalTitle;
}
