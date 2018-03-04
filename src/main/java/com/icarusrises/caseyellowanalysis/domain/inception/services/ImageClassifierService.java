package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;

public interface ImageClassifierService {
    ImageClassificationResult classifyImage(VisionRequest visionRequest, String identifier);
}
