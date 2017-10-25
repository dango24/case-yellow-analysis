package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.ImageAnalyzerService;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.util.Objects.isNull;

public abstract class ImageTestParser implements SpeedTestParser {

    private Logger logger = Logger.getLogger(ImageTestParser.class);

    private ImageAnalyzerService imageAnalyzerService;

    @Autowired
    private void setImageAnalyzerService(ImageAnalyzerService imageAnalyzerService) {
        this.imageAnalyzerService = imageAnalyzerService;
    }

    protected double parseSpeedTestByPostLocation(String postLocationIdentifier, Map<String, String> data) throws IOException {
        int resultPostLocationIndex;
        validateData(data);
        File imgFile = new File(data.get("file"));

        try {
            OcrResponse ocrResponse = parseImage(imgFile);
            logger.info("successfully retrieve ocr response");
            resultPostLocationIndex = ocrResponse.getTextAnnotations().indexOf(new WordData(postLocationIdentifier));

            if (resultPostLocationIndex <= 0) {
                throw new SpeedTestParserException("Failed to locate location identifier for postLocationIdentifier: " + postLocationIdentifier);
            }

            return Double.valueOf(ocrResponse.getTextAnnotations().get(resultPostLocationIndex -1).getDescription());

        } catch (Exception e) {
            logger.error("Failed to parse image, " + e.getMessage(), e);
            throw new SpeedTestParserException("Failed to parse image, " + e.getMessage(), e);
        }
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
