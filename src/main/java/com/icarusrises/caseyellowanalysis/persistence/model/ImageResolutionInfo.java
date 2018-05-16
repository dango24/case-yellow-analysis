package com.icarusrises.caseyellowanalysis.persistence.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class ImageResolutionInfo {

    private String identifier;
    List<ImageResolutionCoordinate> imageResolutionInfo;
}
