package com.icarusrises.caseyellowanalysis.domain.analyzer.services;

import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;

import java.io.IOException;

public interface ImageAnalyzerService {
    OcrResponse analyzeImage(GoogleVisionRequest googleVisionRequest) throws IOException;
}
