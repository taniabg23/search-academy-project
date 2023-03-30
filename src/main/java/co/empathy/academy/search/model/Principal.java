package co.empathy.academy.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@AllArgsConstructor
@Getter
public class Principal {
    //private String tconst;
    private String nconst;
    private String category;
    private String job;
    private String characters;
}
