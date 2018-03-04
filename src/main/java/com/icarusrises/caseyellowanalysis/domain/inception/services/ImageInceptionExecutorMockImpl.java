package com.icarusrises.caseyellowanalysis.domain.inception.services;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;

@Service
@Profile("dev")
public class ImageInceptionExecutorMockImpl implements ImageInceptionExecutor {

    private Logger logger = Logger.getLogger(ImageInceptionExecutorMockImpl.class);

    private static final String INCEPTION_MOCK_LOCATION = "/mock_inception";

    private String inceptionOutput;
    private ImageDecisionService imageDecisionService;

    @Autowired
    public ImageInceptionExecutorMockImpl(ImageDecisionService imageDecisionService) {
        this.imageDecisionService = imageDecisionService;
    }

    @PostConstruct
    private void init() throws IOException {
        inceptionOutput = IOUtils.toString(ImageInceptionExecutorMockImpl.class.getResourceAsStream(INCEPTION_MOCK_LOCATION), Charset.forName("UTF-8"));
    }


    @Override
    public String executeInceptionCommand(String path) {
        return inceptionOutput;
    }
}
