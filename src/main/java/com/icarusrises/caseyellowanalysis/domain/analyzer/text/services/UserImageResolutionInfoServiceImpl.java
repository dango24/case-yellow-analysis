package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.commons.ImageUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.domain.inception.services.ObjectDetectionService;
import com.icarusrises.caseyellowanalysis.persistence.model.ImageResolutionCoordinate;
import com.icarusrises.caseyellowanalysis.persistence.model.ImageResolutionInfo;
import com.icarusrises.caseyellowanalysis.persistence.model.UserImageResolutionInfo;
import com.icarusrises.caseyellowanalysis.persistence.repositories.UserImageResolutionInfoRepository;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

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
        Point objectDetectionPoint = null;
        UserImageResolutionInfo userImageResolutionInfo = null;
        String imageResolution = ImageUtils.getImageResolution(visionRequest);
        DescriptionMatch descriptionMatch = objectDetectionService.detectedObject(identifier, visionRequest);

        if (descriptionMatch.isMatchedDescription()) {
            objectDetectionPoint = descriptionMatch.getDescriptionLocation().getCenter();
        }

        ImageResolutionCoordinate newImageResolutionCoordinate =
            new ImageResolutionCoordinate(imageResolution, descriptionMatchPoint, objectDetectionPoint);

        userImageResolutionInfo = userImageResolutionInfoRepository.findByUser(user);

        if (isNull(userImageResolutionInfo)) {
            userImageResolutionInfo = new UserImageResolutionInfo(user);
        }

        ImageResolutionInfo imageResolutionInfo = userImageResolutionInfo.getImageResolutionInfo(identifier);

        if (isNull(imageResolutionInfo)) {
            imageResolutionInfo = new ImageResolutionInfo(identifier);
        }

        if (imageResolutionInfo.isImageResolutionCoordinateExist(imageResolution)) {
            checkDiffPointsCoordinates(imageResolutionInfo.getImageResolutionCoordinate(imageResolution), newImageResolutionCoordinate);
        } else {
            imageResolutionInfo.addImageResolutionCoordinate(newImageResolutionCoordinate);
        }

        userImageResolutionInfo.putImageResolutionInfo(identifier, imageResolutionInfo);
        userImageResolutionInfoRepository.save(userImageResolutionInfo);


        log.info(String.format("DescriptionMatch origin: %s, descriptionMatch new: %s", descriptionMatchPoint, objectDetectionPoint));
    }

    private void checkDiffPointsCoordinates(ImageResolutionCoordinate originResolutionCoordinate,
                                            ImageResolutionCoordinate newImageResolutionCoordinate) {

    }
}
