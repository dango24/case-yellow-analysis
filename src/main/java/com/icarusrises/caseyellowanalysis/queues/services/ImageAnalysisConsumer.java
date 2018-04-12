package com.icarusrises.caseyellowanalysis.queues.services;

import com.amazonaws.services.s3.event.S3EventNotification;
import com.icarusrises.caseyellowanalysis.queues.model.ImagePathDetails;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.ImageAnalyzerService;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@Component
@ConfigurationProperties(prefix = "image_analyze")
public class ImageAnalysisConsumer {

    @Value("${snapshot_dir}")
    private String snapshotDir;

    @Getter
    @Setter
    private List<String> identifiers;

    private ImageAnalyzerService imageAnalyzerService;

    @Autowired
    public ImageAnalysisConsumer(ImageAnalyzerService imageAnalyzerService) {
        this.imageAnalyzerService = imageAnalyzerService;
    }

    @JmsListener(destination = "${sqs.analyze_snapshot.queue}")
    public void processMessage(@Payload S3EventNotification s3EventNotification) {

        if (isValidNotificationMessage(s3EventNotification)) {

            s3EventNotification.getRecords()
                               .stream()
                               .filter(this::isValidRecord)
                               .map(S3EventNotification.S3EventNotificationRecord::getS3)
                               .map(S3EventNotification.S3Entity::getObject)
                               .map(S3EventNotification.S3ObjectEntity::getKey)
                               .map(this::buildImagePathDetails)
                               .filter(this::isImageQualifiedForAnalyze)
                               .peek(this::putMDC)
                               .forEach(this::analyzeImage);
        }
    }

    private void analyzeImage(ImagePathDetails imagePathDetails) throws AnalyzeException {
        log.info(String.format("Start analyzing image for path: %s", imagePathDetails.getPath()));

        try {
            imageAnalyzerService.analyzeImage(imagePathDetails.getIdentifier(), imagePathDetails.getPath());

        } finally {
            MDC.remove("correlation-id");
        }
    }

    private boolean isImageQualifiedForAnalyze(ImagePathDetails imagePathDetails) {
        return identifiers.contains(imagePathDetails.getIdentifier());
    }

    private boolean isValidNotificationMessage(S3EventNotification s3EventNotification) {
        return nonNull(s3EventNotification) && nonNull(s3EventNotification.getRecords()) && !s3EventNotification.getRecords().isEmpty();
    }

    private boolean isValidRecord(S3EventNotification.S3EventNotificationRecord eventNotificationRecord) {

        return nonNull(eventNotificationRecord) &&
               nonNull(eventNotificationRecord.getS3()) &&
               nonNull(eventNotificationRecord.getS3().getObject()) &&
               StringUtils.isNotEmpty(eventNotificationRecord.getS3().getObject().getKey());
    }

    private void putMDC(ImagePathDetails imagePathDetails) {
        MDC.put("correlation-id", imagePathDetails.getMd5());
    }

    private ImagePathDetails buildImagePathDetails(String path) {
        String[] pathArgs = getPathArguments(path);

        if (pathArgs.length != 4) { // Total numbers of valid arguments
            String errorMessage = String.format("Failed to generate ImagePathDetails, path args is not in length 4 for path: %s", path);
            log.error(errorMessage);
            throw new AnalyzeException(errorMessage);
        }

        String user = pathArgs[0];
        String md5 = pathArgs[2];
        String identifier = getIdentifierFromPathArgs(pathArgs);

        return new ImagePathDetails(path, user, identifier, md5);
    }

    private String[] getPathArguments(String path) {
        return path.replaceAll(snapshotDir, "").split("-");
    }

    private String getIdentifierFromPathArgs(String[] pathArgs) {
        String identifier = pathArgs[pathArgs.length -1];

        return identifier.replaceAll(".png", "")
                         .replaceAll(".PNG", "");
    }
}
