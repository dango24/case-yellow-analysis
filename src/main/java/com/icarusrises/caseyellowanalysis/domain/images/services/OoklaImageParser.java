package com.icarusrises.caseyellowanalysis.domain.images.services;

import com.icarusrises.caseyellowanalysis.commons.ImageUtils;
import com.icarusrises.caseyellowanalysis.commons.WordUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.images.model.PinnedWord;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

@Component
public class OoklaImageParser extends ImageTestParser {

    private Logger logger = Logger.getLogger(OoklaImageParser.class);

    private static final String NEGATIVE_FLAG = "parseInNegativeMode";

    @Value("${ookla_identifier}")
    private String ooklaIdentifier;

    @Value("${ookla_identifier_count}")
    private int ooklaIdentifierCount;

    @Override
    public double parseSpeedTest(Map<String, String> data) throws IOException {
        validateData(data);
        File imgFile = new File(data.get("file"));
        List<WordData> ooklaIdentifiers;
        data = addOoklaData(data);

        try {
            OcrResponse ocrResponse = parseImage(ImageUtils.convertToNegative(imgFile));
            logger.info("successfully retrieve ocr response");

            ooklaIdentifiers =
                IntStream.range(0, ocrResponse.getTextAnnotations().size())
                         .filter(index -> ocrResponse.getTextAnnotations().get(index).getDescription().equals(ooklaIdentifier))
                         .mapToObj(index -> ocrResponse.getTextAnnotations().get(index))
                         .collect(Collectors.toList());

            if (ooklaIdentifiers.size() != ooklaIdentifierCount) {
                return handleCountMisMatch(ooklaIdentifiers, data);
            }

            PinnedWord pinnedWord = retrieveLeftResult(ooklaIdentifiers.get(0), ooklaIdentifiers.get(1));

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

        return Double.valueOf(floatLocationsInText.get(0).getDescription()); // retrieve the closest word to ookla identifier

        } catch (Exception e) {
            logger.error("Failed to parse image, " + e.getMessage(), e);
            throw new SpeedTestParserException("Failed to parse image, " + e.getMessage(), e);
        }
    }

    private Map<String,String> addOoklaData(Map<String, String> data) {
        Map<String, String> newData = new HashMap<>(data);

        if (data.containsKey(NEGATIVE_FLAG)) {
            newData.put(NEGATIVE_FLAG, "true");
        } else {
            newData.put(NEGATIVE_FLAG, "false");
        }

        return newData;
    }

    private OcrResponse parseImage(File imgFile, Map<String, String> data) throws IOException {
        if (data.get(NEGATIVE_FLAG).equals("true")) {
            return parseImage(ImageUtils.convertToNegative(imgFile));
        } else {
            return parseImage(imgFile);
        }
    }

    private double handleCountMisMatch(List<WordData> ooklaIdentifiers, Map<String, String> data) throws IOException {
        if (data.get(NEGATIVE_FLAG).equals("false")) {
            return parseSpeedTest(data);
        }

        throw new IllegalStateException("The number of found identifiers is not match for identifier: " +
                                        ooklaIdentifier + " expected: " + ooklaIdentifierCount +
                                        ", actual: " + ooklaIdentifiers.size());
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
