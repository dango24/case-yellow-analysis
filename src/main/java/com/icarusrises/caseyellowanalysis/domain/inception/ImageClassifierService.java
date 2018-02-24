package com.icarusrises.caseyellowanalysis.domain.inception;

import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;

import java.util.List;

public interface ImageClassifierService {
    List<ImageClassification> classifyImage(VisionRequest visionRequest);
}
