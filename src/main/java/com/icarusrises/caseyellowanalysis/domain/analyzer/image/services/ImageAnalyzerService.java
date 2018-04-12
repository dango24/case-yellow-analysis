package com.icarusrises.caseyellowanalysis.domain.analyzer.image.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;

import java.io.IOException;
import java.util.Map;

public interface ImageAnalyzerService {
    void analyzeImage(String identifier, String imagePath) throws AnalyzeException;
    OcrResponse analyzeImage(GoogleVisionRequest googleVisionRequest) throws IOException;
    AnalyzedImage analyzeImage(Map<String, Object> data) throws IOException;
}
