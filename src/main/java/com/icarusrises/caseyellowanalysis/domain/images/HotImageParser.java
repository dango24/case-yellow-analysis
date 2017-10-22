package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class HotImageParser extends ImageTestParser {

    @Value("${hot_location}")
    private String hotPostLocationIdentifier;

    @Override
    public double parseSpeedTest(Map<String, String> data) throws IOException {
        int resultPreLocationIndex;
        validateData(data);
        File imgFile = new File(data.get("file"));

        try {
            OcrResponse ocrResponse = parseImage(imgFile);
            resultPreLocationIndex = ocrResponse.getTextAnnotations().indexOf(new WordData(hotPostLocationIdentifier));

            return Double.valueOf(ocrResponse.getTextAnnotations().get(resultPreLocationIndex -1).getDescription());

        } catch (Exception e) {
            throw new SpeedTestParserException("Failed to parse hot image, " + e.getMessage(), e);
        }
    }

}
