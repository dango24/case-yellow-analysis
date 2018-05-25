package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;

public interface UserImageResolutionInfoService {
    boolean isImageResolutionCoordinateExist(String user, String identifier, VisionRequest visionRequest);
    DescriptionMatch getImageResolutionCoordinate(String user, String identifier, VisionRequest visionRequest);
    void addImageCenterPointToCache(String user, String identifier, Point imageCenterPoint, VisionRequest visionRequest);
    void removeImageCenterPointFromCache(String user, String identifier, Point imageCenterPoint, VisionRequest visionRequest);
}
