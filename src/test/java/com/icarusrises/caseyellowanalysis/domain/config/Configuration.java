package com.icarusrises.caseyellowanalysis.domain.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.icarusrises.caseyellowanalysis.configuration.ConfigurationManager;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.services.UserImageResolutionInfoService;
import com.icarusrises.caseyellowanalysis.queues.model.MessageType;
import com.icarusrises.caseyellowanalysis.queues.services.MessageProducerService;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.GoogleVisionService;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.OcrService;
import com.icarusrises.caseyellowanalysis.services.storage.StorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.jms.JMSException;
import java.io.File;

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
    public StorageService storageService() {
        return new StorageService() {
            @Override
            public File getFile(String identifier) {
                return null;
            }
        };
    }

    @Bean
    @Profile("dev")
    public MessageProducerService messageProducerService() {
        return new MessageProducerService() {
            @Override
            public <T> void send(MessageType type, T payload) throws JMSException {

            }
        };
    }

    @Bean
    @Profile("dev")
    public OcrService ocrService() {
        return new GoogleVisionService(configurationManager());
    }

    @Bean
    @Profile("dev")
    public UserImageResolutionInfoService userImageResolutionInfoService() {
        return new UserImageResolutionInfoService() {
            @Override
            public DescriptionMatch getDescriptionMatchFromCache(String user, String identifier, Point descriptionMatchPoint, VisionRequest visionRequest) {
                return null;
            }
        };
    }
}
