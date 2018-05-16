package com.icarusrises.caseyellowanalysis.domain.inception.services;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;

//@Primary
@Service
public class ImageInceptionExecutorMockImpl implements ImageInceptionExecutor {

    private static final String INCEPTION_MOCK_LOCATION = "/mock_inception";

    private String inceptionOutput;

    @PostConstruct
    private void init() throws IOException {
        inceptionOutput = IOUtils.toString(ImageInceptionExecutorMockImpl.class.getResourceAsStream(INCEPTION_MOCK_LOCATION), Charset.forName("UTF-8"));
    }

    @Override
    public String executeInceptionCommand(String path) {
        return inceptionOutput;
    }
}
