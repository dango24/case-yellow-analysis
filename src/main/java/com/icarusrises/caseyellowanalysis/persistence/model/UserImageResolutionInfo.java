package com.icarusrises.caseyellowanalysis.persistence.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "image-resolution-info")
public class UserImageResolutionInfo {

    @DynamoDBHashKey
    private String user;

    @DynamoDBAttribute
    private Map<String, ImageResolutionInfo> imageResolutionInfo;
}
