package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
@Profile("prod")
public class ImageInceptionExecutorImpl implements ImageInceptionExecutor {

    private static final String INCEPTION_BASE_COMMAND = "/usr/bin/python %s/predict.py %s/models/%s %s";

    @Value("${inception.dir}")
    private String inceptionDir;

    @Value("${inception.model}")
    private String model;

    @PostConstruct
    private void init() {
        log.info(String.format("Image inception executor model: %s", model));
    }

    @Override
    public String executeInceptionCommand(String path) {
        try {
            String inceptionCommand = String.format(INCEPTION_BASE_COMMAND, inceptionDir, inceptionDir, model, path);
            Process process = Runtime.getRuntime().exec(inceptionCommand);

            return executeInceptionCommandInner(process);

        } catch (IOException e) {
            String errorMessage = String.format("Failed to execute inception command, cause: ", e.getMessage());
            log.error(errorMessage, e);

            throw new AnalyzerException(errorMessage, e);
        }
    }

    private String executeInceptionCommandInner(Process process) {
        try (InputStream inputStream = process.getInputStream()) {

            return IOUtils.toString(inputStream, UTF_8);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to read input data from inception service, cause: ", e.getMessage());
            log.error(errorMessage, e);

            throw new AnalyzerException(errorMessage, e);
        }
    }
}
