package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Primary
@Service
public class ObjectDetectionExecutorImpl implements ObjectDetectionExecutor {

    private static final String OBJECT_DETECTION_COMMAND = "python %s %s";

    @Value("${detect.object.model}")
    private String detectObjectModel;

    @Override
    public String executeDetectedObject(String path) {
        try {
            String inceptionCommand = String.format(OBJECT_DETECTION_COMMAND, detectObjectModel, path);
            Process process = Runtime.getRuntime().exec(inceptionCommand);

            return executeInceptionCommandInner(process);

        } catch (IOException e) {
            String errorMessage = String.format("Failed to execute detected object, cause: ", e.getMessage());
            log.error(errorMessage, e);

            throw new AnalyzeException(errorMessage, e);
        }
    }

    private String executeInceptionCommandInner(Process process) {
        try (InputStream inputStream = process.getInputStream()) {

            return IOUtils.toString(inputStream, UTF_8);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to read input data from object detection service, cause: ", e.getMessage());
            log.error(errorMessage, e);

            throw new AnalyzeException(errorMessage, e);
        }
    }
}
