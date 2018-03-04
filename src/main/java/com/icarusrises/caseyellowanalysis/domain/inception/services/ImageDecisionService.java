package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public interface ImageDecisionService {
    ImageClassificationResult generateDecision(List<ImageClassification> imageClassifications, String identifier);

    static ImageClassification getHighestConfidenceImageClassification(List<ImageClassification> imageClassifications) {

        return imageClassifications.stream()
                                   .sorted(Comparator.comparing(ImageClassification::getConfidence).reversed())
                                   .collect(toList())
                                   .get(0);
    }
}
