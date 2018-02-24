package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
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
    public ImageClassificationStatus generateDecision(List<ImageClassification> imageClassifications) {
        if (CollectionUtils.isEmpty(imageClassifications)) {
            return ImageClassificationStatus.FAILED;
        }

        ImageClassification higherConfidenceImageClassification =
            imageClassifications.stream()
                                .sorted(Comparator.comparing(ImageClassification::getConfidence).reversed())
                                .collect(toList())
                                .get(0);

        return makeDecision(higherConfidenceImageClassification.getLabel().toLowerCase());
    }

    private ImageClassificationStatus makeDecision(String label) {
        if (label.contains("start")) {
            return ImageClassificationStatus.EXIST;
        } else if (label.contains("unready")) {
            return ImageClassificationStatus.RETRY;
        } else {
            return ImageClassificationStatus.FAILED;
        }
    }
}
