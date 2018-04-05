package com.icarusrises.caseyellowanalysis.domain.analyzer.image.services;

import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
public class SpeedTestParserSupplier {

    private Map<String, SpeedTestParser> speedTestParserMap;

    public SpeedTestParserSupplier() {
        this.speedTestParserMap = new HashMap<>();
    }

    public SpeedTestParser getSpeedTestParser(String identifier) {
        SpeedTestParser speedTestParser = speedTestParserMap.get(identifier);

        if (isNull(speedTestParser)) {
            throw new SpeedTestParserException("There is no speed test parser for " + identifier + " identifier");
        }

        return speedTestParser;
    }

   public void addSpeedTestParser(SpeedTestParser speedTestParser) {
        speedTestParserMap.put(speedTestParser.getIdentifier(), speedTestParser);
   }

}
