package com.icarusrises.caseyellowanalysis.queues.model;

import lombok.Value;

@Value
public class ImageDetails {

    private String path;
    private String user;
    private String identifier;
    private String md5;
}
