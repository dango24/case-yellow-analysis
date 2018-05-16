package com.icarusrises.caseyellowanalysis.domain.analyzer.image.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DynamoDBDocument
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    private int x;
    private int y;

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }
}
