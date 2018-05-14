package com.icarusrises.caseyellowanalysis.domain.inception.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectDetectionData {

    @JsonProperty("centroid")
    private List<Integer> centroid;

    @JsonProperty("normalized_centroid")
    private List<Double> normalizedCentroid;
}
