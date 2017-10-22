package com.icarusrises.caseyellowanalysis.controllers;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.AnalyzerService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private AnalyzerService analyzerService;

    @Autowired
    public void setAnalyzerService(AnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/analyze-image")
    public AnalyzedImage analyzeImage(@RequestParam("payload") String payload, @NotEmpty MultipartRequest request) {
        try {
            return analyzerService.analyzeImage(null, null);

        } catch (Exception e) {
            return new AnalyzedImage(false);
        }
    }
}
