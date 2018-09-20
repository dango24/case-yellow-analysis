package com.icarusrises.caseyellowanalysis.controllers;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Slf4j
@Profile("prod")
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private OcrService ocrService;
    private ImageClassifierService imageClassifierService;
    private TextAnalyzerService textAnalyzerService;

    @Autowired
    public AnalysisController(TextAnalyzerService textAnalyzerService, ImageClassifierService imageClassifierService, OcrService ocrService) {
        this.ocrService = ocrService;
        this.textAnalyzerService = textAnalyzerService;
        this.imageClassifierService = imageClassifierService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/health")
    public Payload health() {
        return new Payload("we are all good over here");
    }

    @PostMapping("/is-description-exist")
    public DescriptionMatch isDescriptionExist(@RequestParam("user")String user,
                                               @RequestParam("identifier")String identifier,
                                               @RequestParam("startTest")boolean startTest,
                                               @RequestBody GoogleVisionRequest visionRequest) throws AnalyzeException {
        try {
            if (nonNull(user)) {
                MDC.put("correlation-id", user);
            }

            log.info("Received isDescriptionExist POST request for identifier: " + identifier);
            return textAnalyzerService.isDescriptionExist(user, identifier, startTest, visionRequest);

        } finally {
            MDC.remove("correlation-id");
        }
    }

    @PostMapping("/parse-html")
    public HTMLParserResult retrieveResultFromHtml(@RequestParam("identifier")String identifier, @RequestBody HTMLParserRequest htmlParserRequest) throws AnalyzeException {
        log.info("Received retrieve result from html POST request for identifier: " + identifier);
        return textAnalyzerService.retrieveResultFromHtml(identifier, htmlParserRequest);
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

    @PostMapping("/start-button-successfully-found")
    public void startButtonSuccessfullyFound(@RequestParam("user")String user,
                                             @RequestParam("identifier")String identifier,
                                             @RequestParam("x")int x,
                                             @RequestParam("y")int y, @RequestBody VisionRequest visionRequest) {

        log.info("Received startButtonSuccessfullyFound POST request for identifier: " + identifier);
        textAnalyzerService.startButtonSuccessfullyFound(user, identifier, new Point(x, y), visionRequest);
    }


    @PostMapping("/start-button-failed")
    public void startButtonFailed(@RequestParam("user")String user,
                                  @RequestParam("identifier")String identifier,
                                  @RequestParam("x")int x,
                                  @RequestParam("y")int y, @RequestBody VisionRequest visionRequest) {

        log.info("Received startButtonFailed POST request for identifier: " + identifier);
        textAnalyzerService.startButtonFailed(user, identifier, new Point(x, y), visionRequest);
    }
}
