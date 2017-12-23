package com.icarusrises.caseyellowanalysis.domain.analyzer.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.AnalyzedImage;

import java.io.IOException;
import java.util.Map;

public interface AnalyzerService {
    AnalyzedImage analyzeImage(Map<String, Object> data) throws IOException;
}
