package com.icarusrises.caseyellowanalysis.domain.analyzer.image.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserImageResolutionInfo {

    private String user;
    private Map<String, ImageResolutionInfo> imageResolutionInfo;

    @Data
    private class ImageResolutionInfo {

        private String identifier;
        List<ImageResolutionCoordinate> imageResolutionInfo;
    }

    @Data
    private class ImageResolutionCoordinate {

        private String resolution;
        private Point point;
        private int counter;
    }
}
