package com.icarusrises.caseyellowanalysis.controllers;

import com.icarusrises.caseyellowanalysis.commons.Utils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.AnalyzerService;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.icarusrises.caseyellowanalysis.commons.Utils.retrieveSinglePart;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private Logger logger = Logger.getLogger(AnalysisController.class);

    private AnalyzerService analyzerService;

    @Autowired
    public void setAnalyzerService(AnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/analyze-image")
    public AnalyzedImage analyzeImage(@RequestParam("identifier") String identifier, @NotEmpty MultipartRequest request) {
        logger.info("Received analyzeImage request for identifier: " + identifier);

        try {
            MultipartFile multipartFile = retrieveSinglePart(request);
            File imgFile = Utils.writeToFile(identifier + "_" + multipartFile.getOriginalFilename(), multipartFile);
            Map<String, String> data = createData(identifier, imgFile);

            return analyzerService.analyzeImage(data);

        } catch (Exception e) {
            logger.error("Failed to analyze image, " + e.getMessage(), e);
            return AnalyzedImage.AnalyzedImageFailure("Failed to analyze image, " + e.getMessage());
        }
    }

    private Map<String,String> createData(String identifier, File imgFile) {
        Map<String,String> data = new HashMap<>();
        data.put("identifier", identifier);
        data.put("file", imgFile.getAbsolutePath());

        return data;
    }
}
