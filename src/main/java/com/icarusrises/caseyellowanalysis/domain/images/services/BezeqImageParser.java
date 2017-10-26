package com.icarusrises.caseyellowanalysis.domain.images.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class BezeqImageParser extends ImageTestParser {

    @Value("${bezeq_location}")
    private String bezeqPostLocationIdentifier;

    @Override
    public double parseSpeedTest(Map<String, String> data) throws IOException {
       return parseSpeedTestByPostLocation(bezeqPostLocationIdentifier, data);
    }
}
