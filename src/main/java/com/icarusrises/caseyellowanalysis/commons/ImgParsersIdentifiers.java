package com.icarusrises.caseyellowanalysis.commons;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties
public class ImgParsersIdentifiers {

    private Map<String, String> imgParsersIdentifiers = new HashMap<>();

    public ImgParsersIdentifiers() { }

    public Map<String, String> getImgParsersIdentifiers() {
        return imgParsersIdentifiers;
    }

    public void setImgParsersIdentifiers(Map<String, String> imgParsersIdentifiers) {
        this.imgParsersIdentifiers = imgParsersIdentifiers;
    }
}
