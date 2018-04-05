package com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.parsers;

import com.icarusrises.caseyellowanalysis.commons.WordUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.PinnedWord;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

@Component
public class HotImageParser extends ImageTestParser {

    private Logger logger = Logger.getLogger(HotImageParser.class);

    @Value("${hot_identifier}")
    private String hotIdentifier;

    @Value("${hot_identifier_count}")
    private int hotIdentifierCount;

    @Override
    public double parseSpeedTest(Map<String, Object> data) throws IOException {
        validateData(data);
        double result;
        GoogleVisionRequest googleVisionRequest = (GoogleVisionRequest)data.get("file");
        List<WordData> hotIdentifiers;
        data = addNegativeData(data);

        try {
            OcrResponse ocrResponse = parseImage(googleVisionRequest, String.valueOf(data.get(NEGATIVE_PARSING)));
            logger.info("successfully retrieve ocr response");

            hotIdentifiers =
                    IntStream.range(0, ocrResponse.getTextAnnotations().size())
                            .filter(index -> ocrResponse.getTextAnnotations().get(index).getDescription().equals(hotIdentifier))
                            .mapToObj(index -> ocrResponse.getTextAnnotations().get(index))
                            .collect(Collectors.toList());

            if (hotIdentifiers.size() != hotIdentifierCount) {
                handleCountMisMatch(data, hotIdentifiers.size());
            }

            PinnedWord pinnedWord = retrieveLeftResult(hotIdentifiers.get(0), hotIdentifiers.get(1));

            List<PinnedWord> floatLocationsInText =
                    ocrResponse.getTextAnnotations()
                            .stream()
                            .filter(word -> isCreatable(word.getDescription()))
                            .map(WordUtils::createPinnedWord)
                            .sorted(comparing(word -> WordUtils.euclideanDistance(word.getCentralizedLocation(), pinnedWord.getCentralizedLocation())))
                            .collect(Collectors.toList());

            if (floatLocationsInText.isEmpty()) {
                throw new SpeedTestParserException("Failed to parse image, No numbers found");
            }

            result = Double.valueOf(floatLocationsInText.get(0).getDescription()); // retrieve the closest word to hot identifier

            if (result <= 0) {
                throw new IllegalArgumentException(String.format("Bezeq image parser result is less than 0, result: %s", result));
            }

            return result;

        } catch (Exception e) {
            logger.error("Failed to parse image, " + e.getMessage(), e);
            throw new SpeedTestParserException("Failed to parse image, " + e.getMessage(), e);
        }
    }

    @Override
    public String getIdentifier() {
        return "hot";
    }

    private PinnedWord retrieveLeftResult(WordData leftWordResult, WordData rightWordResult) {
        PinnedWord firstPinnedWord = WordUtils.createPinnedWord(leftWordResult);
        PinnedWord lastPinnedWord = WordUtils.createPinnedWord(rightWordResult);

        if (firstPinnedWord.getCentralizedLocation().getX() > lastPinnedWord.getCentralizedLocation().getX()) {
            return retrieveLeftResult(rightWordResult, leftWordResult);
        }

        return firstPinnedWord;
    }

}
