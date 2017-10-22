package com.icarusrises.caseyellowanalysis.domain.analyzer.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.domain.images.SpeedTestParser;
import com.icarusrises.caseyellowanalysis.domain.images.SpeedTestParserSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class AnalyzerServiceImpl implements AnalyzerService {

    private SpeedTestParserSupplier speedTestParserSupplier;

    @Autowired
    public void setSpeedTestParserSupplier(SpeedTestParserSupplier speedTestParserSupplier) {
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    @Override
    public AnalyzedImage analyzeImage(String identifier, Map<String, String> data) throws IOException {
        SpeedTestParser speedTestParser = speedTestParserSupplier.getSpeedTestParser(identifier);
        double result = speedTestParser.parseSpeedTest(data);

        return new AnalyzedImage(result);
    }
}
