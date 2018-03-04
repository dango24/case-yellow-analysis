package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;

import java.util.List;

public interface ImageDecisionService {
    ImageClassificationResult generateDecision(List<ImageClassification> imageClassifications, String identifier);
}
