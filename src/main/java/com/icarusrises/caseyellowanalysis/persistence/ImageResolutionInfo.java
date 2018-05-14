package com.icarusrises.caseyellowanalysis.persistence;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "User-image-resolution")
public class ImageResolutionInfo {

    private String User;
    private String identifier;

    public ImageResolutionInfo(String user, String identifier) {
        this.User = user;
        this.identifier = identifier;
    }

    @DynamoDBHashKey
    public String getUser() {
        return User;
    }

    @DynamoDBAttribute
    public void setUser(String user) {
        this.User = user;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
