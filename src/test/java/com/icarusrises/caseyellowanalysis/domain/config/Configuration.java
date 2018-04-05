package com.icarusrises.caseyellowanalysis.domain.config;

import com.icarusrises.caseyellowanalysis.configuration.ConfigurationManager;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.GoogleVisionService;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.OcrService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    @Profile("dev")
    public ConfigurationManager configurationManager() {
        return new ConfigurationManager() {
            @Override
            public String awsAccessKeyID() {
                return null;
            }

            @Override
            public String awsSecretAccessKey() {
                return null;
            }

            @Override
            public String googleVisionKey() {
                return null;
            }
        };
    }

    @Bean
    @Profile("dev")
    public OcrService ocrService() {
        return new GoogleVisionService(configurationManager());
    }
}
