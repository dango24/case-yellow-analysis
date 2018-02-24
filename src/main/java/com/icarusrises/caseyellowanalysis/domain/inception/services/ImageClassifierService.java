package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationStatus;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;

public interface ImageClassifierService {
    ImageClassificationStatus classifyImage(VisionRequest visionRequest);
}
