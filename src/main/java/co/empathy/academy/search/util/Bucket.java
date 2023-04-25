package co.empathy.academy.search.util;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Bucket {
    private String key;
    private long doc_count;
}
