package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;

public interface UserImageResolutionInfoService {
    DescriptionMatch getDescriptionMatchFromCache(String user, String identifier, Point descriptionMatchPoint, VisionRequest visionRequest);
}
