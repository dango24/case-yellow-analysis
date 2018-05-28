package com.icarusrises.caseyellowanalysis.persistence.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class ImageResolutionInfo {

    private String identifier;
    private List<ImageResolutionCoordinate> imageResolutionInfo;

    public ImageResolutionInfo(String identifier) {
        this.identifier = identifier;
        this.imageResolutionInfo = new ArrayList<>();
    }

    public boolean isImageResolutionCoordinateExist(String resolution) {
        return imageResolutionInfo.stream()
                                  .anyMatch(imageResolutionCoordinate -> imageResolutionCoordinate.getResolution().equals(resolution));
    }

    public ImageResolutionCoordinate getImageResolutionCoordinate(String resolution) {
        return imageResolutionInfo.stream()
                                  .filter(info -> info.getResolution().equals(resolution))
                                  .findFirst()
                                  .orElseGet(null);
    }

    public void addImageResolutionCoordinate(ImageResolutionCoordinate imageResolutionCoordinate) {
        imageResolutionInfo.add(imageResolutionCoordinate);
    }

    @Override
    public String toString() {
        return "{" +
                "identifier='" + identifier + '\'' +
                ", imageResolutionInfo=" + imageResolutionInfo +
                '}';
    }
}
