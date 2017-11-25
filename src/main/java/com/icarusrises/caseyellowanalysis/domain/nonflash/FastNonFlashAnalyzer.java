package com.icarusrises.caseyellowanalysis.domain.nonflash;

import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParser;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParserSupplier;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Component
public class FastNonFlashAnalyzer implements SpeedTestParser {

    private SpeedTestParserSupplier speedTestParserSupplier;

    @Autowired
    public FastNonFlashAnalyzer(SpeedTestParserSupplier speedTestParserSupplier) {
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    @PostConstruct
    private void init() {
        speedTestParserSupplier.addSpeedTestParser(this);
    }

    @Override
    public double parseSpeedTest(Map<String, String> data) throws IOException {
        try {
            return Double.valueOf(data.get("nonFlashResult"));

        } catch (Exception e) {
            throw new AnalyzerException("Failed to analyze fast result with nonFlashResult: " + data.get("nonFlashResult"));
        }
    }

    @Override
    public String getIdentifier() {
        return "fast";
    }
}
