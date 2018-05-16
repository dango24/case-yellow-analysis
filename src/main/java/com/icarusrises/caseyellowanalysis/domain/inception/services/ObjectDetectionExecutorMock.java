package com.icarusrises.caseyellowanalysis.domain.inception.services;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;

@Service
//@Primary
public class ObjectDetectionExecutorMock implements ObjectDetectionExecutor {

    private static final String OBJECT_DETECTION_MOCK_LOCATION = "/mock_object_detection";

    private String ObjectDetectionOutput;

    @PostConstruct
    private void init() throws IOException {
        ObjectDetectionOutput = IOUtils.toString(ObjectDetectionExecutorMock.class.getResourceAsStream(OBJECT_DETECTION_MOCK_LOCATION), Charset.forName("UTF-8"));
    }

    @Override
    public String executeDetectedObject(String path) {
        return ObjectDetectionOutput;
    }
}
