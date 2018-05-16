package com.icarusrises.caseyellowanalysis.persistence.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class ImageResolutionCoordinate {

    private String resolution;
    private Point point;
    private Point objectDetectionPoint;
}
