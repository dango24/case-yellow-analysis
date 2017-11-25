package com.icarusrises.caseyellowanalysis.domain.nonflash;

import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParser;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParserSupplier;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SpeedofNonFlashAnalyzer implements SpeedTestParser {

    private SpeedTestParserSupplier speedTestParserSupplier;

    @Autowired
    public SpeedofNonFlashAnalyzer(SpeedTestParserSupplier speedTestParserSupplier) {
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    @PostConstruct
    private void init() {
        speedTestParserSupplier.addSpeedTestParser(this);
    }

    @Override
    public double parseSpeedTest(Map<String, String> data) throws IOException {
        String nonFlashResult = data.get("nonFlashResult");

        try {
            String patternString = "\\d+(:?\\.\\d+)?";

            Pattern pattern = Pattern.compile(patternString);

            Matcher matcher = pattern.matcher(nonFlashResult);

            if (matcher.find()) {
                return Double.valueOf(matcher.group());

            } else {
                throw new AnalyzerException("Failed to analyze fast result with nonFlashResult: " + nonFlashResult);
            }

        } catch (Exception e) {
            throw new AnalyzerException("Failed to analyze fast result with nonFlashResult: " + nonFlashResult);
        }
    }

    @Override
    public String getIdentifier() {
        return "speedof";
    }
}
