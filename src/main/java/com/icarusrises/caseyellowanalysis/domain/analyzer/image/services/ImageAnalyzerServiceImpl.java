package com.icarusrises.caseyellowanalysis.domain.analyzer.image.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageAnalyzerServiceImpl implements ImageAnalyzerService {

    private OcrService ocrService;
    private SpeedTestParserSupplier speedTestParserSupplier;

    @Autowired
    public ImageAnalyzerServiceImpl(OcrService ocrService, SpeedTestParserSupplier speedTestParserSupplier) {
        this.ocrService = ocrService;
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    @Override
    public OcrResponse analyzeImage(GoogleVisionRequest googleVisionRequest) throws IOException {
        return ocrService.parseImage(googleVisionRequest);
    }

    @Override
    public AnalyzedImage analyzeImage(Map<String, Object> data) throws IOException {
        SpeedTestParser speedTestParser = speedTestParserSupplier.getSpeedTestParser(String.valueOf(data.get("identifier")));
        double result = speedTestParser.parseSpeedTest(data);

        return new AnalyzedImage(result);
    }
}
