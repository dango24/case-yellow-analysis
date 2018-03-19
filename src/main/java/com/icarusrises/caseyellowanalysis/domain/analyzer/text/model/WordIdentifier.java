package com.icarusrises.caseyellowanalysis.domain.analyzer.text.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordIdentifier implements Comparable<WordIdentifier> {

    private String identifier;
    private int count; // -1 indicates there is no appearance in the given text.

    @Override
    public int compareTo(WordIdentifier other) {
        return identifier.compareTo(other.identifier);
    }
}
