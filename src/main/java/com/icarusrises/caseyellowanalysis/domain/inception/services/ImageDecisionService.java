package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationStatus;

import java.util.List;

public interface ImageDecisionService {
    ImageClassificationStatus generateDecision(List<ImageClassification> imageClassifications, String identifier);
}
