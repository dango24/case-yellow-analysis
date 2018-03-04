package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ImageDecisionServiceImpl implements ImageDecisionService {

    private Logger logger = Logger.getLogger(ImageDecisionServiceImpl.class);

    @Override
    public ImageClassificationResult generateDecision(List<ImageClassification> imageClassifications, String identifier) {
        ImageClassificationStatus status;

        if (CollectionUtils.isEmpty(imageClassifications)) {
            logger.warn(String.format("There is no imageClassifications for identifier: %s", identifier));
            return new ImageClassificationResult(ImageClassificationStatus.FAILED);
        }

        logger.info(String.format("produce image classification from: %s", imageClassifications));

        ImageClassification higherConfidenceImageClassification =
            imageClassifications.stream()
                                .sorted(Comparator.comparing(ImageClassification::getConfidence).reversed())
                                .collect(toList())
                                .get(0);

        status = makeDecision(higherConfidenceImageClassification.getLabel().toLowerCase(), identifier);

        return new ImageClassificationResult(status);
    }

    private ImageClassificationStatus makeDecision(String label, String identifier) {
        logger.info(String.format("The decision label: %s", label));

        if (label.contains("start") && label.contains(identifier)) {
            return ImageClassificationStatus.START_EXIST;
        } else if (label.contains("end") && label.contains(identifier)) {
            return ImageClassificationStatus.END_EXIST;
        } else if (label.contains("unready") && label.contains(identifier)) {
            return ImageClassificationStatus.RETRY;
        } else {
            return ImageClassificationStatus.FAILED;
        }
    }
}
