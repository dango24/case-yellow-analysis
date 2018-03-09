package com.icarusrises.caseyellowanalysis.controllers;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.AnalyzerService;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;
import com.icarusrises.caseyellowanalysis.domain.inception.services.ImageClassifierService;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.OcrService;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;


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
    public AnalyzedImage analyzeImage(@RequestParam("identifier")String identifier, @RequestParam("md5")String md5, @RequestBody GoogleVisionRequest googleVisionRequest) {
        try {
            if (nonNull(md5)) {
                MDC.put("correlation-id", md5);
            }
            logger.info("Received analyzeImage POST request for identifier: " + identifier);

            Map<String, Object> data = createData(identifier, googleVisionRequest);

            return analyzerService.analyzeImage(data);

        } catch (Exception e) {
            logger.error("Failed to analyze image, " + e.getMessage(), e);
            return AnalyzedImage.AnalyzedImageFailure("Failed to analyze image, " + e.getMessage());
        } finally {
            MDC.remove("correlation-id");
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/classify-image")
    public ImageClassificationResult classifyImage(@RequestParam("md5") String md5, @RequestParam("identifier")String identifier, @RequestBody VisionRequest visionRequest)  {
        try {
            if (nonNull(md5)) {
                MDC.put("correlation-id", md5);
            }
            logger.info(String.format("Received classifyImage GET request for identifier: %s, image: %s", identifier, visionRequest));

            return imageClassifierService.classifyImage(visionRequest, identifier);

        } finally {
            MDC.remove("correlation-id");
        }
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
