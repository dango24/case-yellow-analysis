package com.icarusrises.caseyellowanalysis.domain.images.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.services.ImageAnalyzerService;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParser;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParserSupplier;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

public abstract class ImageTestParser implements SpeedTestParser {

    private Logger logger = Logger.getLogger(ImageTestParser.class);

    private static final String NEGATIVE_PARSING = "parseInNegativeMode";

    private ImageAnalyzerService imageAnalyzerService;
    private SpeedTestParserSupplier speedTestParserSupplier;

    @PostConstruct
    private void init() {
        speedTestParserSupplier.addSpeedTestParser(this);
    }

    @Autowired
    public void setImageAnalyzerService(ImageAnalyzerService imageAnalyzerService) {
        this.imageAnalyzerService = imageAnalyzerService;
    }

    @Autowired
    public void setSpeedTestParserSupplier(SpeedTestParserSupplier speedTestParserSupplier) {
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    protected OcrResponse parseImage(GoogleVisionRequest googleVisionRequest) throws IOException {
        return imageAnalyzerService.analyzeImage(googleVisionRequest);
    }

    protected void validateData(Map<String, Object> data) throws IOException {
        if (isNull(data) || data.isEmpty() || isNull(data.get("file"))) {
            throw new SpeedTestParserException("Failed to parse img, data is null or empty");
        }

        if (!(data.get("file") instanceof GoogleVisionRequest)) {
            throw new SpeedTestParserException("Failed to parse img, file is not GoogleVisionRequest");
        }
    }

    protected Map<String,Object> addNegativeData(Map<String, Object> data) {
        Map<String, Object> newData = new HashMap<>(data);

        if (data.containsKey(NEGATIVE_PARSING)) {
            newData.put(NEGATIVE_PARSING, "true");
        } else {
            newData.put(NEGATIVE_PARSING, "false");
        }

        return newData;
    }

    protected double handleCountMisMatch(Map<String, Object> data, int identifiersSize) throws IOException {
        if (data.get(NEGATIVE_PARSING).equals("false")) {
            return parseSpeedTest(data);
        }

        throw new IllegalStateException(String.format("The number of found identifiers is not match for identifier: %s  expected: %s , actual: %s", data.get("identifier"), data.get("identifier"), identifiersSize));
    }

}
