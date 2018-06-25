package com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.parsers;

import com.icarusrises.caseyellowanalysis.commons.ImageUtils;
import com.icarusrises.caseyellowanalysis.commons.WordUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.PinnedWord;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
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
    public String getIdentifier() {
        return "hot";
    }

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

            PinnedWord leftPinnedWord = retrievePinnedWord(hotIdentifiers.get(0), hotIdentifiers.get(1), (leftX, rightX) -> leftX > rightX);
            PinnedWord rightPinnedWord = retrievePinnedWord(hotIdentifiers.get(0), hotIdentifiers.get(1), (leftX, rightX) -> leftX < rightX);

            Pairing leftPairing = createPairing(leftPinnedWord, ocrResponse.getTextAnnotations());
            Pairing rightPairing = createPairing(rightPinnedWord, ocrResponse.getTextAnnotations());

            validatePairing(leftPairing, rightPairing, googleVisionRequest.getRequests().get(0));

            result = Double.valueOf(leftPairing.getValue().getDescription()); // retrieve the closest word to hot identifier

            if (result <= 0) {
                throw new IllegalArgumentException(String.format("Hot image parser result is less than 0, result: %s", result));
            }

            return result;

        } catch (Exception e) {
            logger.error("Failed to parse image, " + e.getMessage(), e);
            throw new SpeedTestParserException("Failed to parse image, " + e.getMessage(), e);
        }
    }

    private void validatePairing(Pairing leftPairing, Pairing rightPairing, VisionRequest visionRequest) {
        if (leftPairing.getValue().equals(rightPairing.getValue())) {
            String errorMessage = "Failed to parse image both pairs values are the same";
            logger.error(errorMessage);
            throw new SpeedTestParserException(errorMessage);
        }

        String imageResolution = ImageUtils.getImageResolution(visionRequest);
        int X = Integer.valueOf(imageResolution.split("_")[0]);
        int Y = Integer.valueOf(imageResolution.split("_")[1]);

        double leftX = leftPairing.getMbps().getCentralizedLocation().getX() - leftPairing.getValue().getCentralizedLocation().getX();
        double rightX = rightPairing.getMbps().getCentralizedLocation().getX() - rightPairing.getValue().getCentralizedLocation().getX();
        double normalLeftX = leftX/X;
        double normalRightX = rightX/X;

        double leftY = leftPairing.getMbps().getCentralizedLocation().getY() - leftPairing.getValue().getCentralizedLocation().getY();
        double rightY = rightPairing.getMbps().getCentralizedLocation().getY() - rightPairing.getValue().getCentralizedLocation().getY();
        double normalLeftY = leftY/Y;
        double normalRightY = rightY/Y;

        double diffX = Math.abs(normalLeftX - normalRightX);
        double diffY = Math.abs(normalLeftY - normalRightY);

        if (diffX > 0.01 || diffY > 0.01) {
            String errorMessage = String.format("Failed to parse image diffX or DiffY are too big, DiffX: %s DiffY: %s", diffX, diffY);
            logger.error(errorMessage);

            throw new SpeedTestParserException(errorMessage);
        }
    }

    private Pairing createPairing(PinnedWord pinnedWord , List<WordData> wordData) {

        List<PinnedWord> floatLocationsInText =
                wordData.stream()
                        .filter(word -> isCreatable(word.getDescription()))
                        .map(WordUtils::createPinnedWord)
                        .sorted(comparing(word -> WordUtils.euclideanDistance(word.getCentralizedLocation(), pinnedWord.getCentralizedLocation())))
                        .collect(Collectors.toList());

        if (floatLocationsInText.size() < 2) {
            throw new SpeedTestParserException(String.format("Failed to parse image, found only %s numbers", floatLocationsInText.size()));
        }

        return new Pairing(pinnedWord, floatLocationsInText.get(0));
    }

    private PinnedWord retrievePinnedWord(WordData leftWordResult, WordData rightWordResult, BiFunction<Integer, Integer, Boolean> compareFunction) {
        PinnedWord firstPinnedWord = WordUtils.createPinnedWord(leftWordResult);
        PinnedWord lastPinnedWord = WordUtils.createPinnedWord(rightWordResult);

        if (compareFunction.apply(firstPinnedWord.getCentralizedLocation().getX(), lastPinnedWord.getCentralizedLocation().getX())) {
            return retrievePinnedWord(rightWordResult, leftWordResult, compareFunction);
        }

        return firstPinnedWord;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Pairing {

        private PinnedWord mbps;
        private PinnedWord value;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            Pairing pairing = (Pairing) o;

            if (mbps != null ? !mbps.equals(pairing.mbps) : pairing.mbps != null) return false;
            return value != null ? value.equals(pairing.value) : pairing.value == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (mbps != null ? mbps.hashCode() : 0);
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }
    }

}
