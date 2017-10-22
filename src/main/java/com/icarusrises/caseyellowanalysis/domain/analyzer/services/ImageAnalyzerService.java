package com.icarusrises.caseyellowanalysis.domain.analyzer.services;

import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;

import java.io.File;
import java.io.IOException;

public interface ImageAnalyzerService {
    OcrResponse analyzeImage(File img) throws IOException;
}
