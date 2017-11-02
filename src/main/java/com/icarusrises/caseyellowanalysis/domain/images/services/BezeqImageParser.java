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
import java.util.stream.IntStream;

import static com.icarusrises.caseyellowanalysis.domain.images.services.WordUtils.createWordResult;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;


@Component
public class BezeqImageParser extends ImageTestParser {

    private Logger logger = Logger.getLogger(BezeqImageParser.class);

    @Value("${bezeq_Mb_location}")
    private String bezeqMbLocation;

    @Value("${bezeq_Kb_location}")
    private String bezeqKbLocation;

    @Override
    public double parseSpeedTest(Map<String, String> data) throws IOException {
        validateData(data);
        File imgFile = new File(data.get("file"));
        Map<String, List<WordData>> ooklaIdentifiers;

        try {
            OcrResponse ocrResponse = parseImage(imgFile);
            logger.info("successfully retrieve ocr response");

            ooklaIdentifiers =
                    IntStream.range(0, ocrResponse.getTextAnnotations().size())
                            .filter(index -> ocrResponse.getTextAnnotations().get(index).getDescription().equals(bezeqMbLocation) ||
                                             ocrResponse.getTextAnnotations().get(index).getDescription().equals(bezeqKbLocation))
                            .mapToObj(index -> new WordData(ocrResponse.getTextAnnotations().get(index), index))
                            .collect(groupingBy(WordData::getDescription));

            validateResults(ooklaIdentifiers);

            WordData MbWordData= ooklaIdentifiers.get(bezeqMbLocation).get(0);
            WordData MbResult = ocrResponse.getTextAnnotations().get(MbWordData.getIndex() -1);

            return Double.valueOf(MbResult.getDescription());

        } catch (Exception e) {
            logger.error("Failed to parse image, " + e.getMessage(), e);
            throw new SpeedTestParserException("Failed to parse image, " + e.getMessage(), e);
        }
    }

    private void validateResults(Map<String, List<WordData>> ooklaIdentifiers) {
        if (ooklaIdentifiers.size() != 2) {
            throw new IllegalStateException("The number of found identifiers does not match for identifiers: " +
                                            bezeqMbLocation + " and " + bezeqKbLocation + " result is: " + ooklaIdentifiers);
        }

        if (isNull(ooklaIdentifiers.get(bezeqMbLocation)) || ooklaIdentifiers.get(bezeqMbLocation).size() != 1) {
            throw new SpeedTestParserException(bezeqMbLocation + " is null or not match the amount of correct identifiers, result: " + ooklaIdentifiers.get(bezeqMbLocation));
        }

        if (isNull(ooklaIdentifiers.get(bezeqKbLocation)) || ooklaIdentifiers.get(bezeqKbLocation).size() != 1) {
            throw new SpeedTestParserException(bezeqKbLocation + " is null or not match the amount of correct identifiers, result: " + ooklaIdentifiers.get(bezeqKbLocation));
        }

        WordResult KbWordResult = createWordResult(ooklaIdentifiers.get(bezeqKbLocation).get(0));
        WordResult MbWordResult = createWordResult(ooklaIdentifiers.get(bezeqMbLocation).get(0));

        if (KbWordResult.getCentralizedLocation().getX() > MbWordResult.getCentralizedLocation().getX()) {
            throw new SpeedTestParserException("Failed to parse image, the Kb point is from the right of the Mb point.");
        }
    }
}
