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
    String title;
    @Nullable
    String region;
    @Nullable
    String language;
    @JsonProperty("isOriginalTitle")
    boolean isOriginalTitle;
}
