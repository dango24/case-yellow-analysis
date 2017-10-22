package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.ImageAnalyzerService;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.util.Objects.isNull;

public abstract class ImageTestParser implements SpeedTestParser {

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
            resultPostLocationIndex = ocrResponse.getTextAnnotations().indexOf(new WordData(postLocationIdentifier));

            return Double.valueOf(ocrResponse.getTextAnnotations().get(resultPostLocationIndex -1).getDescription());

        } catch (Exception e) {
            throw new SpeedTestParserException("Failed to parse bezeq image, " + e.getMessage(), e);
        }
    }

    protected OcrResponse parseImage(File imgFile) throws IOException {
        return imageAnalyzerService.analyzeImage(imgFile);
    }

    protected void validateData(Map<String, String> data) throws IOException {
        if (isNull(data) || data.isEmpty() || isNull(data.get("file"))) {
            throw new SpeedTestParserException("Failed to parse hot img, data is null or empty");
        }
        File file = new File(data.get("file"));

        if (isNull(file) || !file.exists()) {
            throw new SpeedTestParserException("Failed to parse hot img, file is null or not exist");
        }
    }

}
