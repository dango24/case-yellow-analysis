package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.commons.ImageUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.domain.inception.services.ObjectDetectionService;
import com.icarusrises.caseyellowanalysis.persistence.model.ImageResolutionCoordinate;
import com.icarusrises.caseyellowanalysis.persistence.model.ImageResolutionInfo;
import com.icarusrises.caseyellowanalysis.persistence.model.UserImageResolutionInfo;
import com.icarusrises.caseyellowanalysis.persistence.repositories.UserImageResolutionInfoRepository;
import com.icarusrises.caseyellowanalysis.queues.model.MessageType;
import com.icarusrises.caseyellowanalysis.queues.services.MessageProducerService;
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
    private MessageProducerService messageProducerService;
    private UserImageResolutionInfoRepository userImageResolutionInfoRepository;

    @Autowired
    public UserImageResolutionInfoServiceImpl(ObjectDetectionService objectDetectionService,
                                              UserImageResolutionInfoRepository userImageResolutionInfoRepository,
                                              MessageProducerService messageProducerService) {

        this.objectDetectionService = objectDetectionService;
        this.messageProducerService = messageProducerService;
        this.userImageResolutionInfoRepository = userImageResolutionInfoRepository;
    }

    @Override
    public DescriptionMatch getDescriptionMatchFromCache(String user, String identifier, Point descriptionMatchPoint, VisionRequest visionRequest) {
        Point objectDetectionPoint = null;
        ImageResolutionInfo imageResolutionInfo = null;
        UserImageResolutionInfo userImageResolutionInfo = null;
        ImageResolutionCoordinate newImageResolutionCoordinate = null;
        String imageResolution = ImageUtils.getImageResolution(visionRequest);
        DescriptionMatch descriptionMatch = objectDetectionService.detectedObject(identifier, visionRequest);

        if (descriptionMatch.isMatchedDescription()) {
            objectDetectionPoint = descriptionMatch.getDescriptionLocation().getCenter();
        }

        newImageResolutionCoordinate = new ImageResolutionCoordinate(imageResolution, descriptionMatchPoint, objectDetectionPoint);
        userImageResolutionInfo = userImageResolutionInfoRepository.findByUser(user);

        if (isNull(userImageResolutionInfo)) {
            userImageResolutionInfo = new UserImageResolutionInfo(user);
            log.info(String.format("Add new UserImageResolutionInfo for user: %s", user));
        }

        imageResolutionInfo = userImageResolutionInfo.getImageResolutionInfo(identifier);

        if (isNull(imageResolutionInfo)) {
            imageResolutionInfo = new ImageResolutionInfo(identifier);
        }

        if (imageResolutionInfo.isImageResolutionCoordinateExist(imageResolution)) {
            checkDiffPointsCoordinates(user, imageResolution, imageResolutionInfo.getImageResolutionCoordinate(imageResolution), newImageResolutionCoordinate);
        }

        imageResolutionInfo.addImageResolutionCoordinate(newImageResolutionCoordinate);
        userImageResolutionInfo.putImageResolutionInfo(identifier, imageResolutionInfo);
        userImageResolutionInfoRepository.save(userImageResolutionInfo);
        log.info(String.format("Retrieve description match from cache for user: %s, with info: %s", user, userImageResolutionInfo));

        return new DescriptionMatch(String.format("%s start button", identifier), descriptionMatchPoint);
    }

    private void checkDiffPointsCoordinates(String user, String resolution,
                                            ImageResolutionCoordinate originResolutionCoordinate,
                                            ImageResolutionCoordinate newImageResolutionCoordinate) {

        try {
            String message;
            log.info(String.format("ImageResolutionCoordinate origin: %s, new: %s", originResolutionCoordinate, newImageResolutionCoordinate));

            if (!originResolutionCoordinate.getPoint().equals(newImageResolutionCoordinate.getPoint())) {
                message = String.format("Found google vision image resolution coordinate miss match origin: %s, new: %s", originResolutionCoordinate.getPoint(), newImageResolutionCoordinate.getPoint());
                log.error(message);
                messageProducerService.send(MessageType.IMAGE_COORDINATE_MISS_MATCH, message);
            }

            if (!originResolutionCoordinate.getObjectDetectionPoint().equals(newImageResolutionCoordinate.getObjectDetectionPoint())) {
                message = String.format("Found Object detection resolution coordinate miss match origin: %s, new: %s", originResolutionCoordinate.getObjectDetectionPoint(), newImageResolutionCoordinate.getObjectDetectionPoint());
                log.error(message);
                messageProducerService.send(MessageType.IMAGE_COORDINATE_MISS_MATCH, message);
            }

        } catch (Exception e) {
            log.error(String.format("Failure accrue while checking Diff between points coordinates, %s", e.getMessage()), e);
        }
    }
}
