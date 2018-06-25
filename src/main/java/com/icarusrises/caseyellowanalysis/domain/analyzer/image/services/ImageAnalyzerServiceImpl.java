package com.icarusrises.caseyellowanalysis.domain.analyzer.image.services;

import com.icarusrises.caseyellowanalysis.commons.FileUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.AnalyzedImage;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.queues.model.ImageDetails;
import com.icarusrises.caseyellowanalysis.queues.services.MessageProducerService;
import com.icarusrises.caseyellowanalysis.queues.model.MessageType;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.services.OcrService;
import com.icarusrises.caseyellowanalysis.services.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.createData;
import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.logger;

@Service
public class ImageAnalyzerServiceImpl implements ImageAnalyzerService {

    private OcrService ocrService;
    private StorageService storageService;
    private MessageProducerService messageProducerService;
    private SpeedTestParserSupplier speedTestParserSupplier;

    @Autowired
    public ImageAnalyzerServiceImpl(OcrService ocrService,
                                    StorageService storageService,
                                    MessageProducerService messageProducerService,
                                    SpeedTestParserSupplier speedTestParserSupplier) {

        this.ocrService = ocrService;
        this.storageService = storageService;
        this.messageProducerService = messageProducerService;
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    @Override
    public void analyzeImage(ImageDetails imageDetails) throws AnalyzeException {
        File imageFile = null;

        try {
            imageFile = storageService.getFile(imageDetails.getPath());
            GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(imageFile.getAbsolutePath());
            Map<String, Object> data = createData(imageDetails.getIdentifier(), googleVisionRequest);
            AnalyzedImage analyzedImage = analyzeImage(data);

            if (analyzedImage.isAnalyzed()) {
                analyzedImage.setPath(imageDetails.getPath());
                analyzedImage.setMd5(imageDetails.getMd5());
                messageProducerService.send(MessageType.SNAPSHOT_ANALYZED, analyzedImage);

            } else {
                throw new AnalyzeException(String.format("Failed to analyze image: %s", imageDetails.getPath()));
            }

        } catch (Exception e) {
            String errorMessage = String.format("Failed to analyze image: %s, cause: %s", imageDetails.getPath(), e.getMessage());
            logger.error(errorMessage);
            throw new AnalyzeException(errorMessage, e);

        } finally {
            FileUtils.deleteFile(imageFile);
        }
    }

    @Override
    public OcrResponse analyzeImage(GoogleVisionRequest googleVisionRequest) throws IOException {
        return ocrService.parseImage(googleVisionRequest);
    }

    @Override
    public AnalyzedImage analyzeImage(Map<String, Object> data) throws IOException {
        try {
            SpeedTestParser speedTestParser = speedTestParserSupplier.getSpeedTestParser(String.valueOf(data.get("identifier")));
            double result = speedTestParser.parseSpeedTest(data);

            return new AnalyzedImage(result);

        } catch (IllegalArgumentException | SpeedTestParserException e) {
            logger.error(String.format("Failed to analyze image: %s", e.getMessage()), e);
            return AnalyzedImage.AnalyzedImageFailure(e.getMessage());
        }
    }
}
