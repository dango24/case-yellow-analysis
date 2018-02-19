package com.icarusrises.caseyellowanalysis.domain.images.services;

import com.icarusrises.caseyellowanalysis.commons.Utils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.ImageAnalyzerService;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParser;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParserSupplier;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.Image;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.convertToNegative;
import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.createImageBase64Encode;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public abstract class ImageTestParser implements SpeedTestParser {

    private Logger logger = Logger.getLogger(ImageTestParser.class);

    protected static final String NEGATIVE_PARSING = "parseInNegativeMode";

    private ImageAnalyzerService imageAnalyzerService;
    private SpeedTestParserSupplier speedTestParserSupplier;

    @PostConstruct
    private void init() {
        speedTestParserSupplier.addSpeedTestParser(this);
    }

    @Autowired
    public void setImageAnalyzerService(ImageAnalyzerService imageAnalyzerService) {
        this.imageAnalyzerService = imageAnalyzerService;
    }

    @Autowired
    public void setSpeedTestParserSupplier(SpeedTestParserSupplier speedTestParserSupplier) {
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    protected OcrResponse parseImage(GoogleVisionRequest googleVisionRequest, String parseInNegative) throws IOException {
        if (!StringUtils.isEmpty(parseInNegative) && Boolean.valueOf(parseInNegative)) {
            googleVisionRequest = convertImageToNegative(googleVisionRequest);
        }

        return imageAnalyzerService.analyzeImage(googleVisionRequest);
    }

    private GoogleVisionRequest convertImageToNegative(GoogleVisionRequest googleVisionRequest) {
        List<VisionRequest> negativeVisionRequest =
                googleVisionRequest.getRequests()
                                   .stream()
                                   .map(this::convertImageToNegative)
                                   .collect(toList());

        googleVisionRequest.setRequests(negativeVisionRequest);

        return googleVisionRequest;
    }

    private VisionRequest convertImageToNegative(VisionRequest visionRequest) {
        Image image = visionRequest.getImage();
        File tmpFile = Utils.createTmpPNGFile();

        try (FileOutputStream out = new FileOutputStream(tmpFile)) {
            byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(image.getContent());
            out.write(btDataFile);
            out.flush();

        } catch (IOException e) {
            logger.error(String.format("Failed to convert file to negative, error message: %s", e.getMessage(), e));
        }

        try {
            File negativeFile = convertToNegative(tmpFile);
            String negativeImage = new String(createImageBase64Encode(negativeFile.getAbsolutePath()), "UTF-8");
            visionRequest.setImage(new Image(negativeImage));

        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("Failed to convert file to negative, error message: %s", e.getMessage(), e));
        }

        return visionRequest;
    }

    protected void validateData(Map<String, Object> data) throws IOException {
        if (isNull(data) || data.isEmpty() || isNull(data.get("file"))) {
            throw new SpeedTestParserException("Failed to parse img, data is null or empty");
        }

        if (!(data.get("file") instanceof GoogleVisionRequest)) {
            throw new SpeedTestParserException("Failed to parse img, file is not GoogleVisionRequest");
        }
    }

    protected Map<String,Object> addNegativeData(Map<String, Object> data) {
        Map<String, Object> newData = new HashMap<>(data);

        if (data.containsKey(NEGATIVE_PARSING)) {
            newData.put(NEGATIVE_PARSING, "true");
        } else {
            newData.put(NEGATIVE_PARSING, "false");
        }

        return newData;
    }

    protected double handleCountMisMatch(Map<String, Object> data, int identifiersSize) throws IOException {
        if (data.get(NEGATIVE_PARSING).equals("false")) {
            return parseSpeedTest(data);
        }

        throw new IllegalStateException(String.format("The number of found identifiers is not match for identifier: %s  expected: %s , actual: %s", data.get("identifier"), data.get("identifier"), identifiersSize));
    }

}
