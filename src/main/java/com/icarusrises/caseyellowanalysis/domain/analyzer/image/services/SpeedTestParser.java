package com.icarusrises.caseyellowanalysis.domain.analyzer.image.services;

import java.io.IOException;
import java.util.Map;

public interface SpeedTestParser {
    double parseSpeedTest(Map<String, Object> data) throws IOException;
    String getIdentifier();
}
