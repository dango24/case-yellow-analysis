package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.commons.ImageUtils;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationResult;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import com.icarusrises.caseyellowanalysis.services.central.CentralService;
import com.icarusrises.caseyellowanalysis.services.central.PreSignedUrl;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.convertBase64ToImage;
import static com.icarusrises.caseyellowanalysis.commons.UploadFileUtils.generateInceptionSnapshotPth;
import static com.icarusrises.caseyellowanalysis.commons.UploadFileUtils.uploadObject;
import static com.icarusrises.caseyellowanalysis.domain.inception.services.ImageDecisionService.getHighestConfidenceImageClassification;

import java.io.File;
import java.util.List;

@Service
public class ImageClassifierServiceImpl implements ImageClassifierService {

    private Logger logger = Logger.getLogger(ImageClassifierServiceImpl.class);

    private CentralService centralService;
    private ImageDecisionService imageDecisionService;
    private ImageInceptionExecutor imageInceptionExecutor;

    @Autowired
    public ImageClassifierServiceImpl(ImageDecisionService imageDecisionService, ImageInceptionExecutor imageInceptionExecutor, CentralService centralService) {
        this.centralService = centralService;
        this.imageDecisionService = imageDecisionService;
        this.imageInceptionExecutor = imageInceptionExecutor;
    }

    @Override
    public ImageClassificationResult classifyImage(VisionRequest visionRequest, String identifier) {
        try {
            File imageFile = convertBase64ToImage(visionRequest.getImage());
            String commandOutput = imageInceptionExecutor.executeInceptionCommand(imageFile.getAbsolutePath());
            List<ImageClassification> imageClassifications = ImageUtils.parseInceptionCommandOutput(commandOutput);

            uploadImageClassification(imageFile, getHighestConfidenceImageClassification(imageClassifications));

            return imageDecisionService.generateDecision(imageClassifications, identifier);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to classify image: %s", e.getMessage());
            logger.error(errorMessage, e);

            throw new AnalyzerException(errorMessage, e);
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
