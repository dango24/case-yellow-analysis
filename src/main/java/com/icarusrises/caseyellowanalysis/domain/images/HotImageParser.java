package com.icarusrises.caseyellowanalysis.domain.images;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class HotImageParser extends ImageTestParser {

    @Value("${hot_location}")
    private String hotPostLocationIdentifier;

    @Override
    public double parseSpeedTest(Map<String, String> data) throws IOException {
      return parseSpeedTestByPostLocation(hotPostLocationIdentifier, data);
    }

}
