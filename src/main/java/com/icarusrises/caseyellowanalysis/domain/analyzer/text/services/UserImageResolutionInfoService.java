package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;

public interface UserImageResolutionInfoService {
    void foo(String user, String identifier, Point descriptionMatchPoint, VisionRequest visionRequest);
}
