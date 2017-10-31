package com.icarusrises.caseyellowanalysis.domain.images.services;

import java.io.IOException;
import java.util.Map;

public interface SpeedTestParser {
    double parseSpeedTest(Map<String, String> data) throws IOException;
}