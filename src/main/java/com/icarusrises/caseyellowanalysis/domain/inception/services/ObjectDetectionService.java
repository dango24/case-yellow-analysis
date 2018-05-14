package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;

public interface ObjectDetectionService {
    DescriptionMatch detectedObject(String identifier, VisionRequest visionRequest);
}
