package com.icarusrises.caseyellowanalysis.controllers;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.AnalyzerService;
import com.icarusrises.caseyellowanalysis.domain.inception.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.ImageClassifierService;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.OcrService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private Logger logger = Logger.getLogger(AnalysisController.class);

    private OcrService ocrService;
    private AnalyzerService analyzerService;
    private ImageClassifierService imageClassifierService;

    @Autowired
    public AnalysisController(AnalyzerService analyzerService, ImageClassifierService imageClassifierService, OcrService ocrService) {
        this.analyzerService = analyzerService;
        this.ocrService = ocrService;
        this.imageClassifierService = imageClassifierService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/analyze-image")
    public AnalyzedImage analyzeImage(@RequestParam("identifier")String identifier, @RequestBody GoogleVisionRequest googleVisionRequest) {
        logger.info("Received analyzeImage POST request for identifier: " + identifier);

        try {
            Map<String, Object> data = createData(identifier, googleVisionRequest);

            return analyzerService.analyzeImage(data);

        } catch (Exception e) {
            logger.error("Failed to analyze image, " + e.getMessage(), e);
            return AnalyzedImage.AnalyzedImageFailure("Failed to analyze image, " + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/classify-image")
    public List<ImageClassification> classifyImage(@RequestBody VisionRequest visionRequest)  {
        logger.info(String.format("Received classifyImage GET request for image: %s", visionRequest));
        return imageClassifierService.classifyImage(visionRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/ocr_request")
    public OcrResponse ocrRequest(@RequestBody GoogleVisionRequest googleVisionRequest) throws IOException {
        logger.info("Received ocrRequest POST request");
        return ocrService.parseImage(googleVisionRequest);
    }

    private Map<String, Object> createData(String identifier, GoogleVisionRequest googleVisionRequest) {
        Map<String,Object> data = new HashMap<>();
        data.put("identifier", identifier);
        data.put("file", googleVisionRequest);

        return data;
    }
}
