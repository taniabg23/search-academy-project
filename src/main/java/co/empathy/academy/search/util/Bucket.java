package co.empathy.academy.search.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Bucket {
    String key;
    @JsonProperty("doc_count")
    long docCount;
}
