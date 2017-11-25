package com.icarusrises.caseyellowanalysis.domain.images.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.services.ImageAnalyzerService;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParser;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParserSupplier;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.util.Objects.isNull;

public abstract class ImageTestParser implements SpeedTestParser {

    private Logger logger = Logger.getLogger(ImageTestParser.class);

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

    protected OcrResponse parseImage(File imgFile) throws IOException {
        return imageAnalyzerService.analyzeImage(imgFile);
    }

    protected void validateData(Map<String, String> data) throws IOException {
        if (isNull(data) || data.isEmpty() || isNull(data.get("file"))) {
            throw new SpeedTestParserException("Failed to parse img, data is null or empty");
        }
        File file = new File(data.get("file"));

        if (isNull(file) || !file.exists()) {
            throw new SpeedTestParserException("Failed to parse img, file is null or not exist");
        }
    }

}
