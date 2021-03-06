package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ImageDecisionServiceImpl implements ImageDecisionService {

    private Logger logger = Logger.getLogger(ImageDecisionServiceImpl.class);

    @Override
    public ImageClassificationResult generateDecision(List<ImageClassification> imageClassifications, String identifier) {
        ImageClassificationStatus status;
        ImageClassification highestConfidenceImageClassification;

        if (CollectionUtils.isEmpty(imageClassifications)) {
            logger.warn(String.format("There is no imageClassifications for identifier: %s", identifier));
            return new ImageClassificationResult(ImageClassificationStatus.FAILED);
        }

        logger.info(String.format("produce image classification from: %s", imageClassifications));

        highestConfidenceImageClassification = ImageDecisionService.getHighestConfidenceImageClassification(imageClassifications);
        status = makeDecision(highestConfidenceImageClassification.getLabel().toLowerCase(), identifier);

        return new ImageClassificationResult(status);
    }

    private ImageClassificationStatus makeDecision(String label, String identifier) {
        logger.info(String.format("The decision label: %s", label));

        if (label.contains(identifier)) {

            return Stream.of(ImageClassificationStatus.values())
                         .filter(status -> label.contains(status.name().toLowerCase()))
                         .findFirst()
                         .orElse(ImageClassificationStatus.FAILED);
        }

        return ImageClassificationStatus.FAILED;
    }
}
