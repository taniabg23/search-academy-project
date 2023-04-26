package co.empathy.academy.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@AllArgsConstructor
@Getter
public class Principal {
    Name name;
    String characters;
}
