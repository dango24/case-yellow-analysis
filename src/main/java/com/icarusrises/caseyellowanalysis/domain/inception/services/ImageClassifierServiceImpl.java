package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.commons.ImageUtils;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import com.icarusrises.caseyellowanalysis.services.central.CentralService;
import com.icarusrises.caseyellowanalysis.services.central.PreSignedUrl;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.convertBase64ToImage;
import static com.icarusrises.caseyellowanalysis.commons.UploadFileUtils.generateInceptionSnapshotPth;
import static com.icarusrises.caseyellowanalysis.commons.UploadFileUtils.uploadObject;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
@Profile("prod")
public class ImageClassifierServiceImpl implements ImageClassifierService {

    private Logger logger = Logger.getLogger(ImageClassifierServiceImpl.class);

    private static final String INCEPTION_BASE_COMMAND = "/usr/bin/python %s/predict.py %s/models/%s %s";

    @Value("${inception.dir}")
    private String inceptionDir;

    @Value("${inception.model}")
    private String model;

    private CentralService centralService;
    private ImageDecisionService imageDecisionService;

    @Autowired
    public ImageClassifierServiceImpl(ImageDecisionService imageDecisionService, CentralService centralService) {
        this.imageDecisionService = imageDecisionService;
        this.centralService = centralService;
    }

    @Override
    public ImageClassificationResult classifyImage(VisionRequest visionRequest, String identifier) {
        try {
            File imageFile = convertBase64ToImage(visionRequest.getImage());
            String commandOutput = executeInceptionCommand(imageFile.getAbsolutePath());

            List<ImageClassification> imageClassifications =
                Stream.of(commandOutput.split("\n"))
                      .map(this::generateImageClassification)
                      .collect(toList());

            return imageDecisionService.generateDecision(imageClassifications, identifier);

        } catch (IOException e) {
            String errorMessage = String.format("Failed to classify image: %s", e.getMessage());
            logger.error(errorMessage, e);

            throw new AnalyzerException(errorMessage, e);
        }
    }

    private String executeInceptionCommand(String path) throws IOException {
        String inceptionCommand = String.format(INCEPTION_BASE_COMMAND, inceptionDir, inceptionDir, model, path);
        Process process = Runtime.getRuntime().exec(inceptionCommand);

        try (InputStream inputStream = process.getInputStream()) {

            return IOUtils.toString(inputStream, UTF_8);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to read input data from inception service, cause: ", e.getMessage());
            logger.error(errorMessage, e);

            throw new AnalyzerException(errorMessage, e);
        }
    }

    private ImageClassification generateImageClassification(String imageClassificationStr) {
        try {
            int confidenceIndex = imageClassificationStr.lastIndexOf(" ");
            String label = imageClassificationStr.substring(0, confidenceIndex);
            double confidence = Double.valueOf(imageClassificationStr.substring(confidenceIndex + 1));

            return new ImageClassification(label, confidence);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to generate image classification from: %s", imageClassificationStr);
            logger.error(errorMessage);

            throw new AnalyzerException(errorMessage);
        }
    }

    private void uploadImageClassification(File imageFile, ImageClassification highestImageClassification) {
        String label = highestImageClassification.getLabel();
        double confidence = highestImageClassification.getConfidence();
        String md5 = ImageUtils.convertToMD5(imageFile);
        PreSignedUrl preSignedUrl = centralService.generatePreSignedUrl(generateInceptionSnapshotPth(label, confidence, md5));

        uploadObject(preSignedUrl.getPreSignedUrl(), imageFile.getAbsolutePath());
    }

}
