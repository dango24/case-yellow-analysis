package com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.parsers;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.ImageAnalyzerService;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.SpeedTestParser;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.SpeedTestParserSupplier;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static com.icarusrises.caseyellowanalysis.commons.FileUtils.deleteFile;
import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.convertBase64ToImage;
import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.convertToNegative;
import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.createImageBase64Encode;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public abstract class ImageTestParser implements SpeedTestParser {

    private Logger logger = Logger.getLogger(ImageTestParser.class);

    protected static final String NEGATIVE_PARSING = "parseInNegativeMode";
    protected static final String MIS_PAIRING = "misParing";

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

    protected List<WordData> retrieveWordData(GoogleVisionRequest googleVisionRequest, Map<String, Object> data) throws IOException {
        return parseImage(googleVisionRequest, String.valueOf(data.get(NEGATIVE_PARSING))).getTextAnnotations();
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
        File tmpFile = convertBase64ToImage(image);
        File negativeFile = convertToNegative(tmpFile);

        try {
            String negativeImage = new String(createImageBase64Encode(negativeFile.getAbsolutePath()), "UTF-8");
            visionRequest.setImage(new Image(negativeImage));

        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("Failed to convert file to negative, error message: %s", e.getMessage(), e));

        } finally {
            deleteFile(tmpFile);
            deleteFile(negativeFile);
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

    protected Map<String,Object> addExtraData(Map<String, Object> data) {
        addNegativeData(data);
        addMisParingData(data);

        return data;
    }

    private void addMisParingData(Map<String, Object> data) {
        if (!data.containsKey(MIS_PAIRING)) {
            data.put(MIS_PAIRING, "disable");
        }
    }

    private void addNegativeData(Map<String, Object> data) {
        if (data.containsKey(NEGATIVE_PARSING)) {
            data.put(NEGATIVE_PARSING, "true");
        } else {
            data.put(NEGATIVE_PARSING, "false");
        }
    }

    protected double handleCountMisMatch(Map<String, Object> data, int identifiersSize) throws IOException {
        if (data.get(MIS_PAIRING).equals("disable")) {
            data.put(MIS_PAIRING, "enable");
            data.remove(NEGATIVE_PARSING);
            return parseSpeedTest(data);
        }

        if (data.get(NEGATIVE_PARSING).equals("false")) {
            return parseSpeedTest(data);
        }

        throw new IllegalArgumentException(String.format("The number of found identifiers is not match for identifier: %s  expected: %s , actual: %s", data.get("identifier"), data.get("identifier"), identifiersSize));
    }

}
