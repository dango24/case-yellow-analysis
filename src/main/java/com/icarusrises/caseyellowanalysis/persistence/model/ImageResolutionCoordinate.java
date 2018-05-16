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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ImageResolutionCoordinate that = (ImageResolutionCoordinate) o;

        return resolution != null ? resolution.equals(that.resolution) : that.resolution == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (resolution != null ? resolution.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "resolution='" + resolution + '\'' +
                ", point=" + point +
                ", objectDetectionPoint=" + objectDetectionPoint +
                '}';
    }
}
