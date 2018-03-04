package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@Profile("dev")
public class ImageClassifierServiceMockImpl implements ImageClassifierService {

    private Logger logger = Logger.getLogger(ImageClassifierServiceMockImpl.class);

    private static final String INCEPTION_MOCK_LOCATION = "/mock_inception";


    private String inceptionOutput;
    private ImageDecisionService imageDecisionService;

    @Autowired
    public ImageClassifierServiceMockImpl(ImageDecisionService imageDecisionService) {
        this.imageDecisionService = imageDecisionService;
    }

    @PostConstruct
    private void init() throws IOException {
        inceptionOutput = IOUtils.toString(ImageClassifierServiceMockImpl.class.getResourceAsStream(INCEPTION_MOCK_LOCATION), Charset.forName("UTF-8"));
    }

    @Override
    public ImageClassificationResult classifyImage(VisionRequest visionRequest, String identifier) {
        try {
            List<ImageClassification> imageClassifications =
                Stream.of(inceptionOutput.split("\n"))
                      .map(String::trim)
                      .map(this::generateImageClassification)
                      .collect(toList());

            return imageDecisionService.generateDecision(imageClassifications, identifier);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to classify image: %s", e.getMessage());
            logger.error(errorMessage, e);

            throw new AnalyzerException(errorMessage, e);
        }
    }

    private ImageClassification generateImageClassification(String imageClassificationStr) {
        try {
            int confidenceIndex = imageClassificationStr.lastIndexOf(" ");
            String label = imageClassificationStr.substring(0, confidenceIndex);
            double confidence = Double.valueOf(imageClassificationStr.substring(confidenceIndex + 1));

            return new ImageClassification(label, confidence);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to generate image classification from: %s", imageClassificationStr);
            logger.error(errorMessage);

            throw new AnalyzerException(errorMessage);
        }
    }

}
