package com.icarusrises.caseyellowanalysis.domain.images;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OoklaImageParser extends ImageTestParser {

    @Value("${ookla_location}")
    private String ooklaPostLocationIdentifier;

    @Override
    public double parseSpeedTest(Map<String, String> data) throws IOException {
        return parseSpeedTestByPostLocation(ooklaPostLocationIdentifier, data);
    }
}
