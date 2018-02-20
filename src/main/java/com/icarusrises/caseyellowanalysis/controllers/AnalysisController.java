package com.icarusrises.caseyellowanalysis.controllers;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.AnalyzerService;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.GoogleVisionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private Logger logger = Logger.getLogger(AnalysisController.class);

    private AnalyzerService analyzerService;
    private GoogleVisionService googleVisionService;

    @Autowired
    public AnalysisController(AnalyzerService analyzerService, GoogleVisionService googleVisionService) {
        this.analyzerService = analyzerService;
        this.googleVisionService = googleVisionService;
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
    @PostMapping("/ocr_request")
    public OcrResponse ocrRequest(@RequestBody GoogleVisionRequest googleVisionRequest) throws IOException {
        logger.info("Received ocrRequest POST request");
        return googleVisionService.parseImage(googleVisionRequest);
    }

    private Map<String, Object> createData(String identifier, GoogleVisionRequest googleVisionRequest) {
        Map<String,Object> data = new HashMap<>();
        data.put("identifier", identifier);
        data.put("file", googleVisionRequest);

        return data;
    }
}
