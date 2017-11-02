package com.icarusrises.caseyellowanalysis.domain.images.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.images.model.WordResult;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class HotImageParser extends ImageTestParser {

    private Logger logger = Logger.getLogger(HotImageParser.class);

    @Value("${hot_identifier}")
    private String hotIdentifier;

    @Value("${hot_identifier_count}")
    private int hotIdentifierCount;

    @Override
    public double parseSpeedTest(Map<String, String> data) throws IOException {
        validateData(data);
        File imgFile = new File(data.get("file"));
        List<WordData> ooklaIdentifiers;

        try {
            OcrResponse ocrResponse = parseImage(imgFile);
            logger.info("successfully retrieve ocr response");

            ooklaIdentifiers =
                    IntStream.range(0, ocrResponse.getTextAnnotations().size())
                            .filter(index -> ocrResponse.getTextAnnotations().get(index).getDescription().equals(hotIdentifier))
                            .mapToObj(index -> ocrResponse.getTextAnnotations().get(index - 1))
                            .collect(Collectors.toList());

            if (ooklaIdentifiers.size() != hotIdentifierCount) {
                throw new IllegalStateException("The number of found identifiers is not match for identifier: " +
                        hotIdentifier + " expected: " + hotIdentifierCount +
                        ", actual: " + ooklaIdentifiers.size());
            }

            WordResult firstWordResult = WordUtils.createWordResult(ooklaIdentifiers.get(0));
            WordResult lastWordResult = WordUtils.createWordResult(ooklaIdentifiers.get(1));

            validateResults(firstWordResult, lastWordResult);

            return Double.valueOf(firstWordResult.getDescription());

        } catch (Exception e) {
            logger.error("Failed to parse image, " + e.getMessage(), e);
            throw new SpeedTestParserException("Failed to parse image, " + e.getMessage(), e);
        }
    }

    private void validateResults(WordResult firstWordResult, WordResult lastWordResult) {
        if (firstWordResult.getCentralizedLocation().getX() > lastWordResult.getCentralizedLocation().getX()) {
            throw new SpeedTestParserException("Failed to parse image, the download rate point is right from the upload rate.");
        }

        double firstResult = Double.valueOf(firstWordResult.getDescription());
        double lastResult = Double.valueOf(firstWordResult.getDescription());

        if (Math.abs(firstWordResult.getCentralizedLocation().getX() -
                lastWordResult.getCentralizedLocation().getX()) < 100) {

            throw new SpeedTestParserException("Failed to parse image, the distance between the two point is suspiciously close.");

        } else if (firstResult < lastResult) {
            throw new SpeedTestParserException("Failed to parse image, the download rate is less than upload speed, " +
                    "Download WordResult: " + firstWordResult + ", Upload WordResult: " + lastWordResult);
        }
    }
}
