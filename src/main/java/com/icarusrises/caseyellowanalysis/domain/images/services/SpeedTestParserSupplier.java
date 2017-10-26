package com.icarusrises.caseyellowanalysis.domain.images.services;

import com.icarusrises.caseyellowanalysis.commons.ImgParsersIdentifiers;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

@Component
public class SpeedTestParserSupplier {

    private ApplicationContext appContext;
    private Map<String, SpeedTestParser> imgParsers;

    @Autowired
    private void setAppContext(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @PostConstruct
    private void init() {
        buildImgParsers();
    }

    public SpeedTestParser getSpeedTestParser(String identifier) {
        SpeedTestParser speedTestParser = imgParsers.get(identifier);

        if (isNull(speedTestParser)) {
            throw new SpeedTestParserException("There is no speed test parser for " + identifier + " identifier");
        }

        return speedTestParser;
    }

    private void buildImgParsers() {
        ImgParsersIdentifiers imgParsersIdentifiers = (ImgParsersIdentifiers)appContext.getBean("imgParsersIdentifiers");

        imgParsers =
                imgParsersIdentifiers.getImgParsersIdentifiers()
                                     .entrySet()
                                     .stream()
                                     .filter(imgParse -> nonNull(appContext.getBean(imgParse.getValue())))
                                     .collect(toMap(Map.Entry::getKey, imgParse -> (SpeedTestParser)appContext.getBean(imgParse.getValue())));
    }

}
