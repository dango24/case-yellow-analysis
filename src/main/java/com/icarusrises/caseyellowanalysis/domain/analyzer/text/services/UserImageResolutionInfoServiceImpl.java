package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.commons.ImageUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.domain.inception.services.ObjectDetectionService;
import com.icarusrises.caseyellowanalysis.persistence.model.ImageResolutionCoordinate;
import com.icarusrises.caseyellowanalysis.persistence.repositories.UserImageResolutionInfoRepository;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("prod")
public class UserImageResolutionInfoServiceImpl implements UserImageResolutionInfoService {

    private ObjectDetectionService objectDetectionService;
    private UserImageResolutionInfoRepository userImageResolutionInfoRepository;

    @Autowired
    public UserImageResolutionInfoServiceImpl(ObjectDetectionService objectDetectionService, UserImageResolutionInfoRepository userImageResolutionInfoRepository) {
        this.objectDetectionService = objectDetectionService;
        this.userImageResolutionInfoRepository = userImageResolutionInfoRepository;
    }

    @Override
    public void foo(String user, String identifier, Point descriptionMatchPoint, VisionRequest visionRequest) {

        if (true) {
            return;
        }

        Point objectDetectionPoint = null;
        String imageResolution = ImageUtils.getImageResolution(visionRequest);

        DescriptionMatch descriptionMatch2 = objectDetectionService.detectedObject(identifier, visionRequest);

        if (descriptionMatch2.isMatchedDescription()) {
            objectDetectionPoint = descriptionMatch2.getDescriptionLocation().getCenter();
        }

        ImageResolutionCoordinate imageResolutionCoordinate =
            new ImageResolutionCoordinate(imageResolution, descriptionMatchPoint, objectDetectionPoint);

        log.info(String.format("Dan ! Dan ! descriptionMatch origin: %s, descriptionMatch new: %s", descriptionMatchPoint, objectDetectionPoint));
    }
}