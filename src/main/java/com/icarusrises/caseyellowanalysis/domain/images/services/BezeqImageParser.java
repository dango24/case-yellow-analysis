package com.icarusrises.caseyellowanalysis.domain.images.services;

import com.icarusrises.caseyellowanalysis.commons.WordUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.images.model.PinnedWord;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
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

import static com.icarusrises.caseyellowanalysis.commons.WordUtils.createPinnedWord;
import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;
import static org.apache.commons.lang3.math.NumberUtils.isCreatable;


@Component
public class BezeqImageParser extends ImageTestParser {

    private Logger logger = Logger.getLogger(BezeqImageParser.class);

    @Value("${bezeq_Mb_location}")
    private String bezeqMbLocation;

    @Value("${bezeq_Kb_location}")
    private String bezeqKbLocation;

    @Override
    public double parseSpeedTest(Map<String, Object> data) throws IOException {
        validateData(data);
        GoogleVisionRequest googleVisionRequest = (GoogleVisionRequest)data.get("file");
        Map<String, List<WordData>> bezeqIdentifiers;
        data = addNegativeData(data);

        try {
            OcrResponse ocrResponse = parseImage(googleVisionRequest, String.valueOf(data.get(NEGATIVE_PARSING)));
            logger.info("successfully retrieve ocr response");

            bezeqIdentifiers =
                    IntStream.range(0, ocrResponse.getTextAnnotations().size())
                            .filter(index -> ocrResponse.getTextAnnotations().get(index).getDescription().equals(bezeqMbLocation) ||
                                             ocrResponse.getTextAnnotations().get(index).getDescription().equals(bezeqKbLocation))
                            .mapToObj(index -> new WordData(ocrResponse.getTextAnnotations().get(index), index))
                            .collect(groupingBy(WordData::getDescription));

            if (bezeqIdentifiers.size() != 2) {
                return handleCountMisMatch(data, bezeqIdentifiers.size());
            }

            validateResults(bezeqIdentifiers);

            PinnedWord mbPinnedWord = createPinnedWord(bezeqIdentifiers.get(bezeqMbLocation).get(0));

            List<PinnedWord> floatLocationsInText =
                    ocrResponse.getTextAnnotations()
                            .stream()
                            .filter(word -> isCreatable(word.getDescription()))
                            .map(WordUtils::createPinnedWord)
                            .sorted(comparing(word -> WordUtils.euclideanDistance(word.getCentralizedLocation(), mbPinnedWord.getCentralizedLocation())))
                            .collect(Collectors.toList());

            if (floatLocationsInText.isEmpty()) {
                throw new SpeedTestParserException("Failed to parse image, No numbers found");
            }

            return Double.valueOf(floatLocationsInText.get(0).getDescription()); // retrieve the closest word to bezeqmb identifier


        } catch (Exception e) {
            logger.error("Failed to parse image, " + e.getMessage(), e);
            throw new SpeedTestParserException("Failed to parse image, " + e.getMessage(), e);
        }
    }

    @Override
    public String getIdentifier() {
        return "bezeq";
    }

    private void validateResults(Map<String, List<WordData>> bezeqIdentifiers) {

        if (isNull(bezeqIdentifiers.get(bezeqMbLocation)) || bezeqIdentifiers.get(bezeqMbLocation).size() != 1) {
            throw new SpeedTestParserException(bezeqMbLocation + " is null or not match the amount of correct identifiers, result: " + bezeqIdentifiers.get(bezeqMbLocation));
        }

        if (isNull(bezeqIdentifiers.get(bezeqKbLocation)) || bezeqIdentifiers.get(bezeqKbLocation).size() != 1) {
            throw new SpeedTestParserException(bezeqKbLocation + " is null or not match the amount of correct identifiers, result: " + bezeqIdentifiers.get(bezeqKbLocation));
        }

        PinnedWord kbPinnedWord = createPinnedWord(bezeqIdentifiers.get(bezeqKbLocation).get(0));
        PinnedWord mbPinnedWord = createPinnedWord(bezeqIdentifiers.get(bezeqMbLocation).get(0));

        if (kbPinnedWord.getCentralizedLocation().getX() > mbPinnedWord.getCentralizedLocation().getX()) {
            throw new SpeedTestParserException("Failed to parse image, the Kb point is from the right of the Mb point.");
        }
    }
}
