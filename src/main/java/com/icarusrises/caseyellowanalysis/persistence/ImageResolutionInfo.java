package com.icarusrises.caseyellowanalysis.persistence;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "image-resolution-info")
public class ImageResolutionInfo {

    @DynamoDBHashKey
    private String user;

    @DynamoDBAttribute
    private String identifier;

    @DynamoDBAttribute
    private int count;
}
