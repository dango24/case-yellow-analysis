package com.icarusrises.caseyellowanalysis.domain.analyzer.services;

import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageAnalyzerServiceImpl implements ImageAnalyzerService {

    private OcrService ocrService;

    @Autowired
    private void setOcrService(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @Override
    public OcrResponse analyzeImage(GoogleVisionRequest googleVisionRequest) throws IOException {
        return ocrService.parseImage(googleVisionRequest);
    }
}
