package com.icarusrises.caseyellowanalysis.domain.analyzer.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.AnalyzedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyzerServiceImpl implements AnalyzerService {

    private SpeedTestParserSupplier speedTestParserSupplier;

    @Autowired
    public void setSpeedTestParserSupplier(SpeedTestParserSupplier speedTestParserSupplier) {
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    @Override
    public AnalyzedImage analyzeImage(Map<String, String> data) throws IOException {
        SpeedTestParser speedTestParser = speedTestParserSupplier.getSpeedTestParser(data.get("identifier"));
        double result = speedTestParser.parseSpeedTest(data);

        return new AnalyzedImage(result);
    }
}
