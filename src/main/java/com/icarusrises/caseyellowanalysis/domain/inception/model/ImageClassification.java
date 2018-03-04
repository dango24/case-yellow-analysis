package com.icarusrises.caseyellowanalysis.domain.inception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageClassification {

    private String label;
    private double confidence;

    @Override
    public String toString() {
        return "{" +
                "label='" + label + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
