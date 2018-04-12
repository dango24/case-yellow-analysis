package com.icarusrises.caseyellowanalysis.controllers;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.ImageAnalyzerService;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.HTMLParserRequest;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.HTMLParserResult;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.services.TextAnalyzerService;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;
import com.icarusrises.caseyellowanalysis.domain.inception.services.ImageClassifierService;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.OcrService;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.createData;
import static java.util.Objects.nonNull;

@Slf4j
@Profile("prod")
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private OcrService ocrService;
    private ImageAnalyzerService imageAnalyzerService;
    private ImageClassifierService imageClassifierService;
    private TextAnalyzerService textAnalyzerService;

    @Autowired
    public AnalysisController(TextAnalyzerService textAnalyzerService, ImageAnalyzerService imageAnalyzerService, ImageClassifierService imageClassifierService, OcrService ocrService) {
        this.ocrService = ocrService;
        this.imageAnalyzerService = imageAnalyzerService;
        this.textAnalyzerService = textAnalyzerService;
        this.imageClassifierService = imageClassifierService;
    }

    @PostMapping("/is-description-exist")
    public DescriptionMatch isDescriptionExist(@RequestParam("identifier")String identifier,
                                               @RequestParam("startTest")boolean startTest,
                                               @RequestBody GoogleVisionRequest visionRequest) throws AnalyzeException {

        log.info("Received isDescriptionExist POST request for identifier: " + identifier);
        return textAnalyzerService.isDescriptionExist(identifier, startTest, visionRequest);
    }

    @PostMapping("/parse-html")
    public HTMLParserResult retrieveResultFromHtml(@RequestParam("identifier")String identifier, @RequestBody HTMLParserRequest htmlParserRequest) throws AnalyzeException {
        log.info("Received isDescriptionExist POST request for identifier: " + identifier);
        return textAnalyzerService.retrieveResultFromHtml(identifier, htmlParserRequest);
    }

    @PostMapping("/analyze-image")
    public AnalyzedImage analyzeImage(@RequestParam("identifier")String identifier, @RequestParam("md5")String md5, @RequestBody GoogleVisionRequest googleVisionRequest) {
        try {
            if (nonNull(md5)) {
                MDC.put("correlation-id", md5);
            }
            log.info("Received analyzeImage POST request for identifier: " + identifier);

            Map<String, Object> data = createData(identifier, googleVisionRequest);

            return imageAnalyzerService.analyzeImage(data);

        } catch (Exception e) {
            log.error("Failed to analyze image, " + e.getMessage(), e);
            return AnalyzedImage.AnalyzedImageFailure("Failed to analyze image, " + e.getMessage());
        } finally {
            MDC.remove("correlation-id");
        }
    }

    @PostMapping("/classify-image")
    public ImageClassificationResult classifyImage(@RequestParam("md5") String md5, @RequestParam("identifier")String identifier, @RequestBody VisionRequest visionRequest)  {
        try {
            if (nonNull(md5)) {
                MDC.put("correlation-id", md5);
            }
            log.info(String.format("Received classifyImage GET request for identifier: %s, image: %s", identifier, visionRequest));

            return imageClassifierService.classifyImage(visionRequest, identifier);

        } finally {
            MDC.remove("correlation-id");
        }
    }

    @PostMapping("/ocr_request")
    public OcrResponse ocrRequest(@RequestBody GoogleVisionRequest googleVisionRequest) throws IOException {
        log.info("Received ocrRequest POST request");
        return ocrService.parseImage(googleVisionRequest);
    }
}
