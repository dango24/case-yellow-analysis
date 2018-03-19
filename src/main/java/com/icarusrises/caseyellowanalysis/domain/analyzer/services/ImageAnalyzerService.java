package com.icarusrises.caseyellowanalysis.domain.analyzer.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;

import java.io.IOException;
import java.util.Map;

public interface ImageAnalyzerService {
    OcrResponse analyzeImage(GoogleVisionRequest googleVisionRequest) throws IOException;
    AnalyzedImage analyzeImage(Map<String, Object> data) throws IOException;
}
